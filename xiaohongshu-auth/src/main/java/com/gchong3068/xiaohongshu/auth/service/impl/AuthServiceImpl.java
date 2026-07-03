package com.gchong3068.xiaohongshu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.gchong3068.framework.biz.context.holder.LoginUserContextHolder;
import com.gchong3068.framework.common.exception.BizException;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.auth.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.auth.enums.LoginTypeEnum;
import com.gchong3068.xiaohongshu.auth.enums.ResponseCodeEnum;
import com.gchong3068.xiaohongshu.auth.model.vo.user.UpdatePasswordReqVO;
import com.gchong3068.xiaohongshu.auth.model.vo.user.UserLoginReqVO;
import com.gchong3068.xiaohongshu.auth.rpc.UserRpcService;
import com.gchong3068.xiaohongshu.auth.service.AuthService;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO;
import com.google.common.base.Preconditions;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author gc
 * @date 2024年05月30日 23:18
 */
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserRpcService userRpcService;

    /**
     * 登录与注册
     * @author gc
     * @date 2024/5/30 23:21
     * @param userLoginReqVO 用户登录VO
     * @return com.gchong3068.framework.common.response.Response<java.lang.String>
     */
    @Override
    public Response<String> loginAndRegister(UserLoginReqVO userLoginReqVO) {
        String phone = userLoginReqVO.getPhone();
        Integer type = userLoginReqVO.getType();

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        // 未知的登录类型
        if (Objects.isNull(loginTypeEnum)){
            throw new BizException(ResponseCodeEnum.LOGIN_TYPE_ERROR);
        }

        Long userId = null;
        //判读登录类型
        switch (Objects.requireNonNull(loginTypeEnum)) {
            case VERIFICATION_CODE:  //验证码登录
                String verificationCode = userLoginReqVO.getCode();

                Preconditions.checkArgument(StringUtils.isNoneBlank(verificationCode),"验证码不能为空");

                //构建验证码 Redis Key
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);

                String sentCode = (String)  redisTemplate.opsForValue().get(key);
                //判断提交验证码
                if(!StringUtils.equals(verificationCode,sentCode)){
                    throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }
                // RPC: 调用用户服务，注册用户
                Long userIdTmp = userRpcService.registerUser(phone);

                if (Objects.isNull(userIdTmp)){
                    throw new BizException(ResponseCodeEnum.LOGIN_FAIL);
                }
                userId = userIdTmp;
                break;
            case PASSWORD:  //密码登录
                String password = userLoginReqVO.getPassword();

                FindUserByPhoneRspDTO findUserByPhoneRspDTO = userRpcService.findUserByPhoneRspDTO(phone);


                //判断是否注册
                if (Objects.isNull(findUserByPhoneRspDTO)){
                    throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
                }

                String encodePassword = findUserByPhoneRspDTO.getPassword();

                boolean isPasswordCorrect = passwordEncoder.matches(password, encodePassword);

                if (!isPasswordCorrect){
                    throw  new BizException(ResponseCodeEnum.PHONE_OR_PASSWORD_ERROR);
                }

                userId = findUserByPhoneRspDTO.getId();
                break;
            default:
                break;

        }
        //SaToken 登录用户 ，入参为Id
        StpUtil.login(userId);

        //获取Token
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 登出
     * @Auther: gchong3068
     * @Date: 2024/6/21 17:52
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();
        log.info("==> 用户退出登录, userId: {}", userId);

        threadPoolTaskExecutor.submit(()->{
            Long userId2 = LoginUserContextHolder.getUserId();
            log.info("==> 异步线程中获取 userId: {}", userId2);
        });
        //退出登录
        StpUtil.logout(userId);
        return Response.success();
    }



    /**
     * 修改密码
     * @Auther: gchong3068
     * @Date: 2024/6/25 18:34
     * @param updatePasswordReqVO 修改密码VO
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> UpdatePassword(UpdatePasswordReqVO updatePasswordReqVO) {
        String newPassword = updatePasswordReqVO.getNewPassword();

        //加密
        String encodePassword = passwordEncoder.encode(newPassword);

        userRpcService.updatePassword(encodePassword);

        return Response.success();
    }
}
