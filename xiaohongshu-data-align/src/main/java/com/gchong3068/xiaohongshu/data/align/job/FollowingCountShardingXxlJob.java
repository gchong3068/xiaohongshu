package com.gchong3068.xiaohongshu.data.align.job;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.xiaohongshu.data.align.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.data.align.constant.TableConstants;
import com.gchong3068.xiaohongshu.data.align.domain.mapper.DeleteMapper;
import com.gchong3068.xiaohongshu.data.align.domain.mapper.SelectMapper;
import com.gchong3068.xiaohongshu.data.align.domain.mapper.UpdateMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @Description: 定时分片广播任务：对当日发生变更的用户关注数进行对齐
 * @Author: gchong3068
 * @Date: 2024/11/15 18:06
 * @Version: v1.0.0
 **/
@Component
@Slf4j
public class FollowingCountShardingXxlJob {


    @Resource
    private SelectMapper selectMapper;
    @Resource
    private UpdateMapper updateMapper;
    @Resource
    private DeleteMapper deleteMapper;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 分片广播任务
     * @Auther: gchong3068
     * @Date: 2024/11/15 18:35
     **/
    @XxlJob("followingCountShardingJobHandler")
    public void followingCountShardingJobHandler() throws Exception {
        // 获取分片参数
        // 分片序号
        int shardIndex = XxlJobHelper.getShardIndex();
        // 分片总数
        int shardTotal = XxlJobHelper.getShardTotal();

        XxlJobHelper.log("=================> 开始定时分片广播任务：对当日发生变更的用户关注数进行对齐");
        XxlJobHelper.log("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

        log.info("分片参数：当前分片序号 = {}, 总分片数 = {}", shardIndex, shardTotal);

        //表后缀
        String data = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //表名后缀
        String tableNameSuffix = TableConstants.buildTableNameSuffix(data,shardIndex);

        int batchSize = 1000;

        int processedTotal = 0;

        for (;;){
            List<Long> userIds  = selectMapper.selectBatchFromDataAlignFollowingCountTempTable(tableNameSuffix,batchSize);

            if (CollUtil.isEmpty(userIds )) break;

            userIds .forEach(userId  ->{
                // 2: 对 t_following 关注表执行 count(*) 操作，获取关注总数
                int followingTotal = selectMapper.selectCountFromFollowingTableByUserId(userId);
                // 3. 更新t_user_count 表
                int count = updateMapper.updateUserFollowingTotalByUserId(userId, followingTotal);

                //更新Redis
                if (count>0){
                    String redisKey = RedisKeyConstants.buildCountUserKey(userId);

                    Boolean hasKey = redisTemplate.hasKey(redisKey);
                    if (hasKey){
                        redisTemplate.opsForHash().put(redisKey,RedisKeyConstants.FIELD_FOLLOWING_TOTAL,followingTotal);
                    }
                }
            });

            // 4. 批量物理删除这一批次记录
            deleteMapper.batchDeleteDataAlignFollowingCountTempTable(tableNameSuffix,userIds);

            // 当前已处理的记录数
            processedTotal += userIds.size();
        }
        XxlJobHelper.log("=================> 结束定时分片广播任务：对当日发生变更的用户关注数进行对齐，共对齐记录数：{}", processedTotal);
    }


}
