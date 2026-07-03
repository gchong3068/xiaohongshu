package com.gchong3068.xiaohongshu.user.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.nacos.shaded.com.google.common.base.Preconditions;
import com.gchong3068.framework.biz.context.holder.LoginUserContextHolder;
import com.gchong3068.framework.common.enums.DeletedEnum;
import com.gchong3068.framework.common.enums.StatusEnum;
import com.gchong3068.framework.common.exception.BizException;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.framework.common.util.DateUtils;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.framework.common.util.NumberUtils;
import com.gchong3068.framework.common.util.ParamUtils;
import com.gchong3068.xiaohongshu.count.dto.FindUserCountByIdRspDTO;
import com.gchong3068.xiaohongshu.user.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.user.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.user.biz.constant.RoleConstants;
import com.gchong3068.xiaohongshu.user.biz.domain.dataobject.RoleDO;
import com.gchong3068.xiaohongshu.user.biz.domain.dataobject.UserDO;
import com.gchong3068.xiaohongshu.user.biz.domain.dataobject.UserRoleDO;
import com.gchong3068.xiaohongshu.user.biz.domain.mapper.RoleDOMapper;
import com.gchong3068.xiaohongshu.user.biz.domain.mapper.UserDOMapper;
import com.gchong3068.xiaohongshu.user.biz.domain.mapper.UserRoleDOMapper;
import com.gchong3068.xiaohongshu.user.biz.enums.ResponseCodeEnum;
import com.gchong3068.xiaohongshu.user.biz.enums.SexEnum;
import com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileReqVO;
import com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileRspVO;
import com.gchong3068.xiaohongshu.user.biz.model.vo.UpdateUserInfoReqVO;
import com.gchong3068.xiaohongshu.user.biz.rpc.CountRpcService;
import com.gchong3068.xiaohongshu.user.biz.rpc.DistributedIdGeneratorRpcService;
import com.gchong3068.xiaohongshu.user.biz.rpc.OssRpcService;
import com.gchong3068.xiaohongshu.user.biz.service.UserService;
import com.gchong3068.xiaohongshu.user.dto.req.*;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Description: 用户服务实现
 * @Author: gchong3068
 * @Date: 2024/7/3 19:07
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private OssRpcService ossRpcService;
    @Resource
    private UserRoleDOMapper userRoleDOMapper;
    @Resource
    private RoleDOMapper roleDOMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private DistributedIdGeneratorRpcService distributedIdGeneratorRpcService;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    private UserDOMapper  userMapper;
    @Resource
    private CountRpcService countRpcService;
    @Resource
    private RocketMQTemplate rocketMQTemplate;


    /**
     * 用户主页信息本地缓存
     * @Auther: gchong3068
     * @Date: 2025/6/10 17:21 
     * @param null
     * @return null
     **/
    private static final Cache<Long, FindUserProfileRspVO> PROFILE_LOCAL_CACHE = Caffeine.newBuilder()
            .initialCapacity(10000) // 设置初始容量为 10000 个条目
            .maximumSize(10000) // 设置缓存的最大容量为 10000 个条目
            .expireAfterWrite(5, TimeUnit.MINUTES) // 设置缓存条目在写入后 5 分钟过期
            .build();

    private static final Cache<Long ,FindUserByIdRspDTO> LOCAL_CACHE = Caffeine.newBuilder()
            .initialCapacity(10000) // 设置初始容量为 10000 个条目
            .maximumSize(10000) // 设置缓存的最大容量为 10000 个条目
            .expireAfterWrite(1, TimeUnit.HOURS) // 设置缓存条目在写入后 1 小时过期
            .build();

    /**
     * 修改用户信息
     * @Auther: gchong3068
     * @Date: 2024/7/6 14:05
 	 * @param updateUserInfoReqVO 用户信息VO
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> updateUserInfo(UpdateUserInfoReqVO updateUserInfoReqVO) {
        // 被更新的用户 ID
        Long userId = updateUserInfoReqVO.getUserId();
        // 当前登录的用户 ID
        Long loginUserId = LoginUserContextHolder.getUserId();

        // 非号主本人，无法修改其个人信息
        if (!Objects.equals(loginUserId, userId)) {
            throw new BizException(ResponseCodeEnum.CANT_UPDATE_OTHER_USER_PROFILE);
        }

        UserDO userDO = new UserDO();
        userDO.setId(userId);
        // 标识位：是否需要更新
        boolean needUpdate = false;

        //头像
        MultipartFile avatarFile = updateUserInfoReqVO.getAvatar();
        if (Objects.nonNull(avatarFile)){
            String avatar = ossRpcService.uploadFile(avatarFile);
            log.info("==> 调用 oss 服务成功，上传头像，url：{}", avatar);

            if (StringUtils.isBlank(avatar)){
                throw new BizException(ResponseCodeEnum.UPLOAD_AVATAR_FAIL);
            }

            userDO.setAvatar(avatar);
            needUpdate = true;
        }

        // 昵称
        String nickname = updateUserInfoReqVO.getNickname();
        if (StringUtils.isNotBlank(nickname)){
            Preconditions.checkArgument(ParamUtils.checkNickname(nickname), ResponseCodeEnum.NICK_NAME_VALID_FAIL);

            userDO.setNickname(nickname);
            needUpdate = true;
        }

        String xiaohongshuId = updateUserInfoReqVO.getXiaohongshuId();
        if (StringUtils.isNotBlank(xiaohongshuId)) {
            Preconditions.checkArgument(ParamUtils.checkXiaohongshuId(xiaohongshuId), ResponseCodeEnum.XIAOHONGSHU_ID_VALID_FAIL);

            userDO.setXiaohongshuId(xiaohongshuId);
            needUpdate = true;
        }

        // 性别
        Integer sex = updateUserInfoReqVO.getSex();
        if (Objects.nonNull(sex)) {
            Preconditions.checkArgument(SexEnum.isValid(sex), ResponseCodeEnum.SEX_VALID_FAIL);

            userDO.setSex(sex);
            needUpdate = true;
        }

        // 生日
        LocalDate birthday = updateUserInfoReqVO.getBirthday();
        if (Objects.nonNull(birthday)) {

            userDO.setBirthday(birthday);
            needUpdate = true;
        }

        // 个人简介
        String introduction = updateUserInfoReqVO.getIntroduction();
        if (StringUtils.isNotBlank(introduction)) {
            Preconditions.checkArgument(ParamUtils.checkLength(introduction, 100), ResponseCodeEnum.INTRODUCTION_VALID_FAIL);

            userDO.setIntroduction(introduction);
            needUpdate = true;
        }

        // 背景图
        MultipartFile backgroundImgFile = updateUserInfoReqVO.getBackgroundImg();
        if (Objects.nonNull(backgroundImgFile)) {
            String backgroundImg  = ossRpcService.uploadFile(backgroundImgFile);
            log.info("==> 调用 oss 服务成功，上传背景图，url：{}", backgroundImg);

            if (StringUtils.isBlank(backgroundImg)){
                throw new BizException(ResponseCodeEnum.UPLOAD_BACKGROUND_IMG_FAIL);
            }

            userDO.setBackgroundImg(backgroundImg);
            needUpdate = true;
        }

        if (needUpdate){
            // 删除用户缓存
            deleteUserRedisCache(userId);
            //更新用户信息
            userDO.setUpdateTime(LocalDateTime.now());
            userDOMapper.updateByPrimaryKeySelective(userDO);

            // 延时双删
            sendDelayDeleteUserRedisCacheMQ(userId);
        }
        return Response.success();
    }

    /**
     * 删除 Redis 中的用户缓存
     * @param userId
     */
    private void deleteUserRedisCache(Long userId) {
        // 构建 Redis Key
        String userInfoRedisKey = RedisKeyConstants.buildUserInfoKey(userId);
        String userProfileRedisKey = RedisKeyConstants.buildUserProfileKey(userId);

        // 批量删除
        redisTemplate.delete(Arrays.asList(userInfoRedisKey, userProfileRedisKey));
    }


    /**
     * 异步发送延时消息
     * @param userId
     */
    private void sendDelayDeleteUserRedisCacheMQ(Long userId) {
        Message<String> message = MessageBuilder.withPayload(String.valueOf(userId))
                .build();

        rocketMQTemplate.asyncSend(MQConstants.TOPIC_DELAY_DELETE_USER_REDIS_CACHE, message,
                new SendCallback() {
                    @Override
                    public void onSuccess(SendResult sendResult) {
                        log.info("## 延时删除 Redis 用户缓存消息发送成功...");
                    }

                    @Override
                    public void onException(Throwable e) {
                        log.error("## 延时删除 Redis 用户缓存消息发送失败...", e);
                    }
                },
                3000, // 超时时间
                1 // 延迟级别，1 表示延时 1s
        );
    }

    /**
     * 用户注册
     * @Auther: gchong3068
     * @Date: 2024/7/8 23:08
     * @param registerUserReqDTO 用户注册请求DTO
     * @return com.gchong3068.framework.common.response.Response<java.lang.Long>
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response<Long> register(RegisterUserReqDTO registerUserReqDTO) {
        String phone = registerUserReqDTO.getPhone();

        UserDO userDO1 = userDOMapper.selectByPhone(phone);

        log.info("==> 用户是否注册, phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO1));

        // 若已注册，则直接返回用户 ID
        if (Objects.nonNull(userDO1)) {
            return Response.success(userDO1.getId());
        }
        // 否则注册新用户
        // 获取全局自增的小哈书 ID
        String xiaohongshuId = distributedIdGeneratorRpcService.getXiaohongshuId();

        String userIdStr = distributedIdGeneratorRpcService.getUserId();
        Long userId = Long.valueOf(userIdStr);

        UserDO userDO = UserDO.builder()
                .id(userId)
                .phone(phone)
                .xiaohongshuId(String.valueOf(xiaohongshuId)) // 自动生成小红书号 ID
                .nickname("小红薯" + xiaohongshuId) // 自动生成昵称, 如：小红薯10000
                .status(StatusEnum.ENABLE.getValue()) // 状态为启用
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDeleted(DeletedEnum.NO.getValue()) // 逻辑删除
                .build();

        // 添加入库
        userDOMapper.insert(userDO);


        // 给该用户分配一个默认角色
        UserRoleDO userRoleDO = UserRoleDO.builder()
                .userId(userId)
                .roleId(RoleConstants.COMMON_USER_ROLE_ID)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .isDeleted(DeletedEnum.NO.getValue())
                .build();
        userRoleDOMapper.insert(userRoleDO);

        RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

        // 将该用户的角色 ID 存入 Redis 中
        List<String> roles = new ArrayList<>(1);
        roles.add(roleDO.getRoleKey());

        String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
        redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

        return Response.success(userId);
    }


    /**
     * 根据用户 Id 查询
     * @Auther: gchong3068
     * @Date: 2024/8/19 22:17
     * @param findUserByIdReqDTO 入参
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO>
     **/
    @Override
    public Response<FindUserByIdRspDTO> findById(FindUserByIdReqDTO findUserByIdReqDTO) {
        Long id = findUserByIdReqDTO.getId();

        FindUserByIdRspDTO findUserByIdRspDTOLocalCache = LOCAL_CACHE.getIfPresent(id);
        if (Objects.nonNull(findUserByIdRspDTOLocalCache)) {
            log.info("==> 命中了本地缓存；{}", findUserByIdRspDTOLocalCache);
            return Response.success(findUserByIdRspDTOLocalCache);
        }
        //用户缓存Redis
        String userInfoRedisKey = RedisKeyConstants.buildUserInfoKey(id);
        //从Redis查询
        String userInfoRedisValue = (String) redisTemplate.opsForValue().get(userInfoRedisKey);
        //若Redis存在
        if (StringUtils.isNotBlank(userInfoRedisValue)){
            FindUserByIdRspDTO findUserByIdRspDTO = JsonUtils.parseObject(userInfoRedisValue, FindUserByIdRspDTO.class);
            threadPoolTaskExecutor.submit(()-> LOCAL_CACHE.put(id,findUserByIdRspDTO));
            return Response.success(findUserByIdRspDTO);
        }
        //否则，从数据库拆查询
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);

        if (Objects.isNull(userDO)){
            threadPoolTaskExecutor.execute(()->{
                //防止缓存穿透，将空数据存入Redis (设置短时间过期)
                long expireSeconds = 60 + RandomUtil.randomInt(60);
                redisTemplate.opsForValue().set(userInfoRedisKey,"null", expireSeconds, TimeUnit.SECONDS);
            });
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }

        FindUserByIdRspDTO findUserByIdRspDTO = FindUserByIdRspDTO.builder()
                .id(userDO.getId())
                .nickName(userDO.getNickname())
                .avatar(userDO.getAvatar())
                .introduction(userDO.getIntroduction())
                .build();

        //异步将用户信息存入Redis
        threadPoolTaskExecutor.execute(()->{
            //过期时间(一天+随机数)
            long expireSeconds = 60*60*24 + RandomUtil.randomInt(60);
            redisTemplate.opsForValue().set(userInfoRedisKey,JsonUtils.toJsonString(findUserByIdRspDTO), expireSeconds, TimeUnit.SECONDS);
        });
        return Response.success(findUserByIdRspDTO);
    }

    /**
     * 根据手机号查询
     * @Auther: gchong3068
     * @Date: 2024/7/9 23:05
     * @param findUserByPhoneReqDTO 查询用户请求DTO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.dto.resp.FindUserByPhoneRspDTO>
     **/
    @Override
    public Response<FindUserByPhoneRspDTO> findByPhone(FindUserByPhoneReqDTO findUserByPhoneReqDTO) {
        String  phone = findUserByPhoneReqDTO.getPhone();

        UserDO userDO = userDOMapper.selectByPhone(phone);

        if (Objects.isNull(userDO)){
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }

        //构建返参
        FindUserByPhoneRspDTO findUserByPhoneRspDTO = FindUserByPhoneRspDTO.builder()
                .id(userDO.getId())
                .password(userDO.getPassword())
                .build();

        return Response.success(findUserByPhoneRspDTO);
    }

    /**
     * 更新密码
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:06
 	 * @param updateUserPasswordReqDTO 更新密码请求DTO
 	 * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> updatePassword(UpdateUserPasswordReqDTO updateUserPasswordReqDTO) {

        Long userId = LoginUserContextHolder.getUserId();

        UserDO userDO = UserDO.builder()
                .id(userId)
                .password(updateUserPasswordReqDTO.getEncodePassword())
                .updateTime(LocalDateTime.now())
                .build();

        userDOMapper.updateByPrimaryKeySelective(userDO);
        return Response.success();
    }

    /**
     * 批量查询
     * @Auther: gchong3068
     * @Date: 2024/9/25 19:42
     * @param findUserByIdsReqDTO 批量查询请求DTO
     * @return com.gchong3068.framework.common.response.Response<java.util.List < com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO>>
     **/
    @Override
    public Response<List<FindUserByIdRspDTO>> findByIds(FindUserByIdsReqDTO findUserByIdsReqDTO) {
        List<Long> userIds = findUserByIdsReqDTO.getIds();

        List<String> redisKeys = userIds.stream()
                .map(RedisKeyConstants::buildUserInfoKey)
                .toList();

        List<Object> redisValues = redisTemplate.opsForValue().multiGet(redisKeys);

        if (CollUtil.isNotEmpty(redisValues)){
            redisValues = redisValues.stream().filter(Objects::nonNull).toList();
        }

        List<FindUserByIdRspDTO> findUserByIdRspDTOS  = null;

        //DTO返参实体类
        if (CollUtil.isNotEmpty(redisValues)){
            findUserByIdRspDTOS  = redisValues.stream()
                    .map(value -> JsonUtils.parseObject(String.valueOf(value), FindUserByIdRspDTO.class)).toList();
        }

        //信息都存在 Redis中 直接返回
        if (CollUtil.size(userIds) == CollUtil.size(findUserByIdRspDTOS)){
            return Response.success(findUserByIdRspDTOS);
        }

        //筛选缓存中不存在用户数据，查询
        List<Long> userIdsNeedQuery = null;

        if (CollUtil.isNotEmpty(findUserByIdRspDTOS)){
            Map<Long, FindUserByIdRspDTO> map = findUserByIdRspDTOS.stream()
                    .collect(Collectors.toMap(FindUserByIdRspDTO::getId,p -> p));
            //筛选
            userIdsNeedQuery = userIds.stream()
                    .filter(id -> Objects.isNull(map.get(id)))
                    .toList();
        }else {
            userIdsNeedQuery = userIds;
        }

        //数据库查询
        List<UserDO> userDOS = userDOMapper.selectByIds(userIdsNeedQuery);

        List<FindUserByIdRspDTO> findUserByIdRspDTOS2 = null;

        if (CollUtil.isNotEmpty(userDOS)){
            findUserByIdRspDTOS2 = userDOS.stream()
                    .map(userDO -> FindUserByIdRspDTO.builder()
                            .id(userDO.getId())
                            .nickName(userDO.getNickname())
                            .avatar(userDO.getAvatar())
                            .introduction(userDO.getIntroduction())
                            .build())
                    .toList();

            //异步将查询用户数据同步到redis
            List<FindUserByIdRspDTO> finalFindUserByRspDTOS = findUserByIdRspDTOS2;
            threadPoolTaskExecutor.submit(()->{
                Map<Long, FindUserByIdRspDTO> map = finalFindUserByRspDTOS.stream().collect(Collectors.toMap(FindUserByIdRspDTO::getId, p -> p));

                //执行 pipeline
                redisTemplate.executePipelined(new SessionCallback<>() {
                    @Override
                    public Object execute(RedisOperations operations)  {

                        for (UserDO userDO :userDOS) {
                            Long userId = userDO.getId();

                            //用户信息缓存
                            String userInfoRedisKey = RedisKeyConstants.buildUserInfoKey(userId);

                            FindUserByIdRspDTO findUserByIdRspDTO = map.get(userId);
                            String value = JsonUtils.toJsonString(findUserByIdRspDTO);

                            //过期时间
                            long expireSeconds = 60* 60 * 24 + RandomUtil.randomInt(60 * 60 * 24);
                            operations.opsForValue().set(userInfoRedisKey,value,expireSeconds,TimeUnit.SECONDS);
                        }
                        return null;
                    }
                });
            });
        }

        if (CollUtil.isNotEmpty(findUserByIdRspDTOS)){
            findUserByIdRspDTOS2.addAll(findUserByIdRspDTOS);
        }
        return Response.success(findUserByIdRspDTOS2);
    }

    /**
     * 获取用户主页信息
     * @Auther: gchong3068
     * @Date: 2025/6/5 17:04 
     * @param findUserProfileReqVO
     * @return com.gchong3068.framework.common.response.Response<com.gchong3068.xiaohongshu.user.biz.model.vo.FindUserProfileRspVO>
     **/
    @Override
    public Response<FindUserProfileRspVO> findUSerProfile(FindUserProfileReqVO findUserProfileReqVO) {
        // 要查询的用户 ID
        Long userId = findUserProfileReqVO.getUserId();

        // 若入参中用户 ID 为空，则查询当前登录用户
        if (Objects.isNull(userId)) {
            userId = LoginUserContextHolder.getUserId();
        }

        //查询本地缓存
        if (!Objects.equals(userId, LoginUserContextHolder.getUserId())) { // 如果是用户本人查看自己的主页，则不走本地缓存（对本人保证实时性）
            FindUserProfileRspVO userProfileLocalCache = PROFILE_LOCAL_CACHE.getIfPresent(userId);
            if (Objects.nonNull(userProfileLocalCache)) {
                log.info("## 用户主页信息命中本地缓存: {}", JsonUtils.toJsonString(userProfileLocalCache));
                return Response.success(userProfileLocalCache);
            }
        }

        // 1. 优先查询缓存
        String userProfileRedisKey  = RedisKeyConstants.buildUserProfileKey(userId);

        String userProfileJson = (String)redisTemplate.opsForValue().get(userProfileRedisKey);

        if (StringUtils.isNotBlank(userProfileJson)){
            FindUserProfileRspVO findUserProfileRspVO = JsonUtils.parseObject(userProfileJson, FindUserProfileRspVO.class);
            // 异步同步到本地缓存
            syncUserProfile2LocalCache(userId, findUserProfileRspVO);
            // 如果是博主本人查看，保证计数的实时性
            authorGetActualCountData(userId, findUserProfileRspVO);

            return Response.success(findUserProfileRspVO);
        }


        // 2. 再查询数据库
        UserDO userDO = userMapper.selectByPrimaryKey(userId);

        if (Objects.isNull(userDO)) {
            throw new BizException(ResponseCodeEnum.USER_NOT_FOUND);
        }

        // 构建返参 VO
        FindUserProfileRspVO findUserProfileRspVO = FindUserProfileRspVO.builder()
                .userId(userDO.getId())
                .avatar(userDO.getAvatar())
                .nickname(userDO.getNickname())
                .xiaohongshuId(userDO.getXiaohongshuId())
                .sex(userDO.getSex())
                .introduction(userDO.getIntroduction())
                .build();

        //计算年龄
        LocalDate birthday = userDO.getBirthday();
        findUserProfileRspVO.setAge(Objects.isNull(birthday) ? 0 : DateUtils.calculateAge(birthday));

        // RPC: Feign 调用计数服务
        // 关注数、粉丝数、收藏与点赞总数；发布的笔记数，获得的点赞数、收藏数
        rpcCountServiceAndSetData(userId, findUserProfileRspVO);

        //异步同步到Redis中
        syncUserProfile2Redis(userProfileRedisKey,findUserProfileRspVO);
        // 异步同步到本地缓存
        syncUserProfile2LocalCache(userId, findUserProfileRspVO);

        return Response.success(findUserProfileRspVO);
    }


    /**
     * 异步同步到 Redis 中
     * @Auther: gchong3068
     * @Date: 2025/6/10 17:19
     * @param userProfileRedisKey
     * @param findUserProfileRspVO
     **/
    private void syncUserProfile2Redis(String userProfileRedisKey,FindUserProfileRspVO findUserProfileRspVO){
        threadPoolTaskExecutor.submit(()->{
            long expireSeconds = 60* 60 * 24 + RandomUtil.randomInt(60 * 60);

            redisTemplate.opsForValue().set(userProfileRedisKey,JsonUtils.toJsonString(findUserProfileRspVO),expireSeconds,TimeUnit.SECONDS);
        });
    }

    /**
     * 异步同步到本地缓存
     * @Auther: gchong3068
     * @Date: 2025/6/10 17:30
     * @param userId
     * @param findUserProfileRspVO
     **/
    private void syncUserProfile2LocalCache(Long userId, FindUserProfileRspVO findUserProfileRspVO) {
        threadPoolTaskExecutor.submit(()-> PROFILE_LOCAL_CACHE.put(userId,findUserProfileRspVO));
    }

    /**
     * Feign 调用计数服务, 并设置计数数据
     * @param userId
     * @param findUserProfileRspVO
     */
    private void rpcCountServiceAndSetData(Long userId, FindUserProfileRspVO findUserProfileRspVO) {
        FindUserCountByIdRspDTO findUserCountsByIdRspDTO = countRpcService.findUserCountById(userId);

        if (Objects.nonNull(findUserCountsByIdRspDTO)) {
            Long fansTotal = findUserCountsByIdRspDTO.getFansTotal();
            Long followingTotal = findUserCountsByIdRspDTO.getFollowingTotal();
            Long likeTotal = findUserCountsByIdRspDTO.getLikeTotal();
            Long collectTotal = findUserCountsByIdRspDTO.getCollectTotal();
            Long noteTotal = findUserCountsByIdRspDTO.getNoteTotal();

            findUserProfileRspVO.setFansTotal(NumberUtils.formatNumberString(fansTotal));
            findUserProfileRspVO.setFollowingTotal(NumberUtils.formatNumberString(followingTotal));
            findUserProfileRspVO.setLikeAndCollectTotal(NumberUtils.formatNumberString(likeTotal + collectTotal));
            findUserProfileRspVO.setNoteTotal(NumberUtils.formatNumberString(noteTotal));
            findUserProfileRspVO.setLikeTotal(NumberUtils.formatNumberString(likeTotal));
            findUserProfileRspVO.setCollectTotal(NumberUtils.formatNumberString(collectTotal));
        }
    }


    /**
     * 作者本人获取真实的计数数据（保证实时性）
     * @param userId
     * @param findUserProfileRspVO
     */
    private void authorGetActualCountData(Long userId, FindUserProfileRspVO findUserProfileRspVO) {
        if (Objects.equals(userId, LoginUserContextHolder.getUserId())) { // 如果是博主本人
            rpcCountServiceAndSetData(userId, findUserProfileRspVO);
        }
    }



}
