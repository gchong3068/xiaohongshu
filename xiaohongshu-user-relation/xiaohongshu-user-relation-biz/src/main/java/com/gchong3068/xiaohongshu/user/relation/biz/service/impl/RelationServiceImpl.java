package com.gchong3068.xiaohongshu.user.relation.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import com.gchong3068.framework.biz.context.holder.LoginUserContextHolder;
import com.gchong3068.framework.common.exception.BizException;
import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.framework.common.util.DateUtils;
import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.user.dto.resp.FindUserByIdRspDTO;
import com.gchong3068.xiaohongshu.user.relation.biz.constant.MQConstants;
import com.gchong3068.xiaohongshu.user.relation.biz.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FansDO;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.dataobject.FollowingDO;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.mapper.FansDOMapper;
import com.gchong3068.xiaohongshu.user.relation.biz.domain.mapper.FollowingDOMapper;
import com.gchong3068.xiaohongshu.user.relation.biz.enums.LuaResultEnum;
import com.gchong3068.xiaohongshu.user.relation.biz.enums.ResponseCodeEnum;
import com.gchong3068.xiaohongshu.user.relation.biz.model.dto.FollowUserMqDTO;
import com.gchong3068.xiaohongshu.user.relation.biz.model.dto.UnfollowUserMqDTO;
import com.gchong3068.xiaohongshu.user.relation.biz.model.vo.*;
import com.gchong3068.xiaohongshu.user.relation.biz.rpc.UserRpcService;
import com.gchong3068.xiaohongshu.user.relation.biz.service.RelationService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @Description: 关注用户服务实现类
 * @Author: gchong3068
 * @Date: 2024/9/9 20:56
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class RelationServiceImpl implements RelationService {

    @Resource
    private UserRpcService userRpcService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private FollowingDOMapper followingDOMapper;
    @Resource
    private FansDOMapper fansDOMapper;
    @Resource
    private RocketMQTemplate rocketMQTemplate;
    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 关注用户
     * @Auther: gchong3068
     * @Date: 2024/9/13 10:07
     * @param followUserReqVO 关注用户请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> follow(FollowUserReqVO followUserReqVO) {

        Long followUserId = followUserReqVO.getFollowUserId();

        Long userId = LoginUserContextHolder.getUserId();

        if (Objects.equals(userId,followUserId)){
            throw new BizException(ResponseCodeEnum.CANT_FOLLOW_YOUR_SELF);
        }

        FindUserByIdRspDTO findUserByIdRspDTO = userRpcService.findById(followUserId);

        if (Objects.isNull(findUserByIdRspDTO)) {
            throw new BizException(ResponseCodeEnum.FOLLOW_USER_NOT_EXISTED);
        }
        //构建关注列表Redis Key
        String followingRedisKey = RedisKeyConstants.buildUserFollowingKey(userId);

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_check_and_add.lua")));
        //返回值
        script.setResultType(Long.class);

        //时间
        LocalDateTime now = LocalDateTime.now();
        //时间戳
        long timestamp = DateUtils.localDateTime2Timestamp(now);
        //执行lua 脚本
        Long result = redisTemplate.execute(script, Collections.singletonList(followingRedisKey), followUserId, timestamp);

        //校验返回
        checkLuaScriptResult(result);

        if (Objects.equals(result,LuaResultEnum.ZSET_NOT_EXIST.getCode())){
            List<FollowingDO> followingDOS = followingDOMapper.selectByUserId(userId);

            //随机过期时间
            long expireSeconds = 60*60*24 + RandomUtil.randomInt(60*60*24);

            if (CollUtil.isEmpty(followingDOS)){
                DefaultRedisScript<Long> script2 = new DefaultRedisScript<>();
                script2.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_add_and_expire.lua")));
                script2.setResultType(Long.class);

                redisTemplate.execute(script2, Collections.singletonList(followingRedisKey), followUserId, timestamp, expireSeconds);
            }else {
                //构建参数
                Object[] luaArgs = buildLuaArgs(followingDOS,expireSeconds);

                DefaultRedisScript<Long> script3 = new DefaultRedisScript<>();
                script3.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/follow_batch_add_and_expire.lua")));
                script3.setResultType(Long.class);
                redisTemplate.execute(script3, Collections.singletonList(followingRedisKey),luaArgs);

                result = redisTemplate.execute(script,Collections.singletonList(followingRedisKey), followUserId, timestamp);
                checkLuaScriptResult(result);
            }
        }

        //发送MQ
        FollowUserMqDTO followUserMqDTO = FollowUserMqDTO.builder()
                .userid(userId)
                .followUserId(followUserId)
                .createTime(now)
                .build();

        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(followUserMqDTO)).build();

        String destination = MQConstants.TOPIC_FOLLOW_OR_UNFOLLOW + ":" + MQConstants.TAG_FOLLOW;

        log.info("==> 开始发送关注操作 MQ, 消息体: {}", followUserMqDTO);

        String hashKey = String.valueOf(userId);

        rocketMQTemplate.asyncSendOrderly(destination, message, hashKey, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> MQ 发送异常: ", throwable);
            }
        });
        return Response.success();
    }

    /**
     * 取关
     * @Auther: gchong3068
     * @Date: 2024/9/21 19:55
     * @param unfollowUserReqVO 取关请求参数
     * @return com.gchong3068.framework.common.response.Response<?>
     **/
    @Override
    public Response<?> unfollow(UnfollowUserReqVO unfollowUserReqVO) {
        Long unfollowUserId = unfollowUserReqVO.getUnfollowUserId();
        Long userId = LoginUserContextHolder.getUserId();
        //无法取关自己
        if (Objects.equals(userId,unfollowUserId)){
            throw new BizException(ResponseCodeEnum.CANT_UNFOLLOW_YOUR_SELF);
        }
        //校验用户是否存在
        FindUserByIdRspDTO findUserByIdRspDTO = userRpcService.findById(unfollowUserId);

        if(Objects.isNull(findUserByIdRspDTO)){
            throw new BizException(ResponseCodeEnum.FOLLOW_USER_NOT_EXISTED);
        }
        //当前用户关注列表 Redis Key
        String followingRedisKey = RedisKeyConstants.buildUserFollowingKey(userId);

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        //lua 脚本
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/unfollow_check_and_delete.lua")));

        script.setResultType(Long.class);

        Long result = redisTemplate.execute(script, Collections.singletonList(followingRedisKey), unfollowUserId);

        //校验 Lua 脚本
        if (Objects.equals(result,LuaResultEnum.NOT_FOLLOWED.getCode())){
            throw new BizException(ResponseCodeEnum.NOT_FOLLOWED);
        }

        if (Objects.equals(result,LuaResultEnum.ZSET_NOT_EXIST.getCode())){
            //从数据库查询
            List<FollowingDO> followingDOS = followingDOMapper.selectByUserId(userId);

            //随机过期时间
            long expireSeconds = 60*60*24 + RandomUtil.randomInt(60*60*24);

            if (CollUtil.isEmpty(followingDOS)){
                throw new BizException(ResponseCodeEnum.NOT_FOLLOWED);
            }else { // 若记录不为空，则将关注关系数据全量同步到 Redis 中，并设置过期时间；

                Object[] luaArgs = buildLuaArgs(followingDOS,expireSeconds);

                DefaultRedisScript<Long> script3 = new DefaultRedisScript<>();
                script3.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_batch_add_and_expire.lua")));
                script3.setResultType(Long.class);
                redisTemplate.execute(script3,Collections.singletonList(followingRedisKey),luaArgs);

                result = redisTemplate.execute(script,Collections.singletonList(followingRedisKey),unfollowUserId);

                if (Objects.equals(result,LuaResultEnum.NOT_FOLLOWED.getCode())){
                    throw new BizException(ResponseCodeEnum.NOT_FOLLOWED);
                }
            }

        }

        // 发送 MQ
        // 构建消息体 DTO
        UnfollowUserMqDTO unfollowUserMqDTO = UnfollowUserMqDTO.builder()
                .userId(userId)
                .unfollowUserId(unfollowUserId)
                .createTime(LocalDateTime.now())
                .build();

        // 构建消息对象，并将 DTO 转成 Json 字符串设置到消息体中
        Message<String> message = MessageBuilder.withPayload(JsonUtils.toJsonString(unfollowUserMqDTO))
                .build();

        // 通过冒号连接, 可让 MQ 发送给主题 Topic 时，携带上标签 Tag
        String destination = MQConstants.TOPIC_FOLLOW_OR_UNFOLLOW + ":" + MQConstants.TAG_UNFOLLOW;

        log.info("==> 开始发送取关操作 MQ, 消息体: {}", unfollowUserMqDTO);

        String hashKey = String.valueOf(userId);

        //异步发送MQ信息
        rocketMQTemplate.asyncSendOrderly(destination, message, hashKey, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("==> MQ 发送成功，SendResult: {}", sendResult);
            }

            @Override
            public void onException(Throwable throwable) {
                log.error("==> MQ 发送异常: ", throwable);
            }
        });
        return null;
    }

    /**
     * 查询关注列表
     * @Auther: gchong3068
     * @Date: 2024/9/25 21:46
     * @param findFollowingListReqVO 查询关注列表请求参数
     * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.user.relation.biz.model.vo.FindFollowingUserRspVO>
     **/
    @Override
    public PageResponse<FindFollowingUserRspVO> findFollowingList(FindFollowingListReqVO findFollowingListReqVO) {
        //用户ID
        Long userId = findFollowingListReqVO.getUserId();
        //页码
        Integer pageNo = findFollowingListReqVO.getPageNo();

        //从Redis中查询
        String followingListRedisKey = RedisKeyConstants.buildUserFollowingKey(userId);
        //查询总大小
        long total = redisTemplate.opsForZSet().zCard(followingListRedisKey);
        //返参
        List<FindFollowingUserRspVO> findFollowingUserRspVOS = null;

        //每页显示条数
        long limit = 10;

        if (total > 0 ){
            //总页数
            long totalPage = PageResponse.getTotalPage(total, limit);
            //请求超出页码总数
            if(pageNo > totalPage) return PageResponse.success(null,pageNo,total);

            //从Redis 查询ZSet分页数据
            //偏移量
            long offset = PageResponse.getOffset(pageNo,limit);

            // 使用 ZREVRANGEBYSCORE 命令按 score 降序获取元素，同时使用 LIMIT 子句实现分页
            // 注意：这里使用了 Double.POSITIVE_INFINITY 和 Double.NEGATIVE_INFINITY 作为分数范围
            // 因为关注列表最多有 1000 个元素，这样可以确保获取到所有的元素
            Set<Object> followingUserIdsSet = redisTemplate.opsForZSet()
                    .reverseRangeByScore(followingListRedisKey, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, offset, limit);

            if (CollUtil.isNotEmpty(followingUserIdsSet)) {
                //提取所有用户ID到集合
                List<Long> userIds = followingUserIdsSet.stream()
                        .map(item -> Long.valueOf(item.toString())).toList();

                //RPC: 批量查询用户信息
                findFollowingUserRspVOS = rpcUserServiceAndDTO2VO(userIds,findFollowingUserRspVOS);

            }
        }else {
            //若Redis中没有数据，从数据库查询
            long count = followingDOMapper.selectCountByUserId(userId);
            //总页数
            long totalPage = PageResponse.getTotalPage(count,limit);

            if (pageNo>totalPage) return PageResponse.success(null,pageNo,count);

            //偏移量
            long offset = PageResponse.getOffset(pageNo,limit);

            //分页查询
            List<FollowingDO> followingDOS = followingDOMapper.selectPageListByUserId(userId,offset,limit);

            if (CollUtil.isNotEmpty(followingDOS)){

                List<Long> userIds = followingDOS.stream().map(FollowingDO::getFollowingUserId).toList();

                // RPC: 调用用户服务，并将 DTO 转换为 VO
                findFollowingUserRspVOS = rpcUserServiceAndDTO2VO(userIds, findFollowingUserRspVOS);

                // 异步将关注列表全量同步到 Redis
                threadPoolTaskExecutor.submit(() -> syncFollowingList2Redis(userId));

            }

        }
        return PageResponse.success(findFollowingUserRspVOS,pageNo,total);
    }


    /**
     * 查询粉丝列表
     * @Auther: gchong3068
     * @Date: 2024/9/28 12:27
 	 * @param findFansListReqVO 查询粉丝列表请求参数
 	 * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.user.relation.biz.model.vo.FindFansUserRspVO>
     **/
    @Override
    public PageResponse<FindFansUserRspVO> findFansList(FindFansListReqVO findFansListReqVO) {
        Long userId = findFansListReqVO.getUserId();
        //页码
        Integer pageNo = findFansListReqVO.getPageNo();

        String fansListRedisKey = RedisKeyConstants.buildUserFansKey(userId);

        Long total = redisTemplate.opsForZSet().zCard(fansListRedisKey);

        List<FindFansUserRspVO> findFansUserRspVOS = null;

        long limit = 10;
        if (total>0){
            long totalPage = PageResponse.getTotalPage(total,limit);

            if (pageNo > totalPage) return PageResponse.success(null,pageNo,total);

            //从Redis中查询ZSet分页数据
            long offset = PageResponse.getOffset(pageNo, limit);

            Set<Object> followingUserIdsSet = redisTemplate.opsForZSet()
                    .reverseRangeByScore(fansListRedisKey,Double.NEGATIVE_INFINITY,Double.POSITIVE_INFINITY,offset,limit);
            if (CollUtil.isNotEmpty(followingUserIdsSet)){
                List<Long> userIds = followingUserIdsSet.stream().map(item -> Long.valueOf(item.toString())).toList();

                findFansUserRspVOS = rpcUserServiceAndCountServiceAndDTO2VO(userIds,findFansUserRspVOS);

            }
        } else {
            //查询记录总数
            total = fansDOMapper.selectCountByUserId(userId);

            long totalPage = PageResponse.getTotalPage(total,limit);

            if (pageNo > 500 || pageNo > totalPage) return PageResponse.success(null,pageNo,total);

            long offset = PageResponse.getOffset(pageNo, limit);
            //分页查询
            List<FansDO> fansDOS = fansDOMapper.selectPageListByUserId(userId, offset, limit);

            if (CollUtil.isNotEmpty(fansDOS)){
                List<Long> userIds = fansDOS.stream().map(FansDO::getFansUserId).toList();

                findFansUserRspVOS = rpcUserServiceAndCountServiceAndDTO2VO(userIds,findFansUserRspVOS);

                threadPoolTaskExecutor.submit(()->syncFansList2Redis(userId));
            }
        }
        return PageResponse.success(findFansUserRspVOS , pageNo,total);
    }

    /**
     * 校验结果
     * @Auther: gchong3068
     * @Date: 2024/9/13 9:42
     * @param result 返回值
     **/
    private static void checkLuaScriptResult(Long result) {
        LuaResultEnum luaResultEnum = LuaResultEnum.valueOf(result);
        if (Objects.isNull(luaResultEnum)) throw new RuntimeException("Lua 返回结果错误");
        // 校验 Lua 脚本执行结果
        switch (luaResultEnum) {
            // 关注数已达到上限
            case FOLLOW_LIMIT -> throw new BizException(ResponseCodeEnum.FOLLOWING_COUNT_LIMIT);
            // 已经关注了该用户
            case ALREADY_FOLLOWED -> throw new BizException(ResponseCodeEnum.ALREADY_FOLLOWED);
        }
    }

    /**
     * 构建Lua 脚本参数
     * @Auther: gchong3068
     * @Date: 2024/9/13 9:38
 	 * @param followingDOS List
 	 * @param expireSeconds long
 	 * @return java.lang.Object[]
     **/
    private static Object[] buildLuaArgs(List<FollowingDO> followingDOS, long expireSeconds) {
        int argsLength = followingDOS.size();
        Object[] luaArgs = new Object[argsLength];

        int i = 0;
        for (FollowingDO followingDO : followingDOS){
            luaArgs[i] = DateUtils.localDateTime2Timestamp(followingDO.getCreateTime());
            luaArgs[i+1] = followingDO.getFollowingUserId();
            i += 2;
        }
        luaArgs[argsLength -1] = expireSeconds;
        return luaArgs;
    }
    
    /**
     * 全量同步关注列表到Redis
     * @Auther: gchong3068
     * @Date: 2024/9/26 23:11 
 	 * @param userId 用户ID
     **/
    private void syncFollowingList2Redis(Long userId){

        List<FollowingDO> followingDOS = followingDOMapper.selectAllByUserId(userId);

        if (CollUtil.isNotEmpty(followingDOS)){
            //用户关注列表Redis Key
            String followingListRedisKey = RedisKeyConstants.buildUserFollowingKey(userId);

            long expireSeconds = 60 * 60 * 24 + RandomUtil.randomInt(60*24*60);

            Object[] luaArgs = buildLuaArgs(followingDOS,expireSeconds);

            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_batch_add_and_expire.lua")));
            script.setResultType(Long.class);
            redisTemplate.execute(script,Collections.singletonList(followingListRedisKey),luaArgs);
        }
    }

    /**
     * 调用用户服务，并将DTO转换为VO
     * @Auther: gchong3068
     * @Date: 2024/9/28 12:02 
     * @param userIds 用户ID集合
     * @param findFollowingUserRspVOS List
     * @return java.util.List<com.gchong3068.xiaohongshu.user.relation.biz.model.vo.FindFollowingUserRspVO>
     **/
    private List<FindFollowingUserRspVO> rpcUserServiceAndDTO2VO(List<Long> userIds, List<FindFollowingUserRspVO> findFollowingUserRspVOS){
        //Rpc 批量查询
        List<FindUserByIdRspDTO> findUserByIdRspDTOS =userRpcService.findByIds(userIds);

        // 若不为空，DTO 转 VO
        if (CollUtil.isNotEmpty(findUserByIdRspDTOS)) {
            findFollowingUserRspVOS = findUserByIdRspDTOS.stream()
                    .map(dto -> FindFollowingUserRspVO.builder()
                            .userId(dto.getId())
                            .avatar(dto.getAvatar())
                            .nickname(dto.getNickName())
                            .introduction(dto.getIntroduction())
                            .build())
                    .toList();
        }
        return findFollowingUserRspVOS;
    }
    
    /**
     * 粉丝列表同步到Redis (5000条)
     * @Auther: gchong3068
     * @Date: 2024/9/28 12:04
 	 * @param userId 用户ID
     **/
    private void syncFansList2Redis(Long userId){
        List<FansDO> fansDOS = fansDOMapper.select5000FansByUserId(userId);
        if (CollUtil.isNotEmpty(fansDOS)){
            String fansListRedisKey = RedisKeyConstants.buildUserFansKey(userId);

            long expireSeconds = 60 * 60 * 24 + RandomUtil.randomInt(60*24*60);

            Object[] luaArgs = buildFansZSetLuaArgs(fansDOS,expireSeconds);

            //执行Lua 脚本
            DefaultRedisScript<Long> script = new DefaultRedisScript<>();
            script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/follow_batch_add_and_expire.lua")));
            script.setResultType(Long.class);
            redisTemplate.execute(script,Collections.singletonList(fansListRedisKey),luaArgs);
        }

    }

    /**
     * 构建 Lua 脚本参数：粉丝列表
     * @Auther: gchong3068
     * @Date: 2024/9/28 12:19
 	 * @param fansDOS 粉丝DOS
 	 * @param expireSeconds 过期时间
 	 * @return java.lang.Object[]
     **/
    private static Object[] buildFansZSetLuaArgs(List<FansDO> fansDOS, long expireSeconds) {
        // 每个粉丝关系有 2 个参数（score 和 value），再加一个过期时间
        int argsLength = fansDOS.size() * 2 + 1;
        Object[] luaArgs = new Object[argsLength];

        int i = 0;
        for (FansDO fansDO : fansDOS){
            // 粉丝的关注时间作为 score
            luaArgs[i] = DateUtils.localDateTime2Timestamp(fansDO.getCreateTime());
            // 粉丝的用户 ID 作为 ZSet value
            luaArgs[i + 1] = fansDO.getFansUserId();
            i += 2;
        }
        // 最后一个参数是 ZSet 的过期时间
        luaArgs[argsLength - 1] = expireSeconds;
        return luaArgs;

    }

    /**
     * 调用用户，计数服务，并将DTO -> VO
     * @Auther: gchong3068
     * @Date: 2024/9/28 12:09 
 	 * @param userIds 用户ID集合
 	 * @param findFansUserRspVOS List
 	 * @return java.util.List<com.gchong3068.xiaohongshu.user.relation.biz.model.vo.FindFansUserRspVO>
     **/
    private List<FindFansUserRspVO> rpcUserServiceAndCountServiceAndDTO2VO(List<Long> userIds,List<FindFansUserRspVO> findFansUserRspVOS){
        //RPC 批量查询
        List<FindUserByIdRspDTO> findUserByIdRspDTOS = userRpcService.findByIds(userIds);
        
        if (CollUtil.isNotEmpty(findUserByIdRspDTOS)){
            findFansUserRspVOS = findFansUserRspVOS.stream()
                    .map(item ->FindFansUserRspVO.builder()
                            .userId(item.getUserId())
                            .avatar(item.getAvatar())
                            .nickname(item.getNickname())
                            .noteTotal(0L)  //TODO: 暂置为0
                            .fansTotal(0L)
                            .build())
                    .toList();
        }
        return findFansUserRspVOS;
    }
    
}
