package com.gchong3068.xiaohongshu.data.align.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.data.align.constant.MQConstants;
import com.gchong3068.xiaohongshu.data.align.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.data.align.constant.TableConstants;
import com.gchong3068.xiaohongshu.data.align.domain.mapper.InsertMapper;
import com.gchong3068.xiaohongshu.data.align.model.dto.LikeUnlikeNoteMqDTO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Objects;

/**
 * @Description: 日增量数据落库：笔记点赞、取消点赞
 * @Author: gchong3068
 * @Date: 2024/11/9 18:26
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_data_align_" + MQConstants.TOPIC_COUNT_NOTE_LIKE,
        topic = MQConstants.TOPIC_COUNT_NOTE_LIKE)
@Slf4j
public class TodayNoteLikeIncrementData2DBConsumer implements RocketMQListener<String> {


    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private InsertMapper insertRecordMapper;

    @Value("${table.shards}")
    private int tableShards;

    @Override
    public void onMessage(String body) {
        log.info("## TodayNoteLikeIncrementData2DBConsumer 消费到了 MQ: {}", body);
        //信息体JSON 字符串 DTO
        LikeUnlikeNoteMqDTO unlikeNoteMqDTO  = JsonUtils.parseObject(body, LikeUnlikeNoteMqDTO.class);

        if (Objects.isNull(unlikeNoteMqDTO )) return;

        Long noteId = unlikeNoteMqDTO.getNoteId();

        Long noteCreatorId = unlikeNoteMqDTO.getNoteCreatorId();

        //今日日期
        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd")); //转字符窜


        // ------------------------- 笔记的点赞数变更记录 -------------------------
        String noteBloomKey  = RedisKeyConstants.buildBloomUserNoteLikeNoteIdListKey(date);

        // 1. 布隆过滤器判断该日增量数据是否已经记录
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();

        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/bloom_today_note_like_check.lua")));

        script.setResultType(Long.class);

        //返回结果
        Long result = redisTemplate.execute(script, Collections.singletonList(noteBloomKey ), noteId);

        // Lua 脚本：添加到布隆过滤器
        RedisScript<Long> bloomAddScript = RedisScript.of("return redis.call('BF.ADD', KEYS[1], ARGV[1])", Long.class);
        // 若布隆过滤器判断不存在（绝对正确）
        if (Objects.equals(result, 0L)) {
            // 2. 若无，才会落库，减轻数据库压力

            // 根据分片总数，取模，获取对应的分片序号
            long noteIdHashKey = noteId % tableShards;

            try {
                // 将日增量变更数据落库
                // - t_data_align_note_like_count_temp_日期_分片序号
                insertRecordMapper.insert2DataAlignNoteLikeCountTempTable(TableConstants.buildTableNameSuffix(date, noteIdHashKey), noteId);
            } catch (Exception e) {
                log.error("", e);
            }

            // 4. 数据库写入成功后，再添加布隆过滤器中
            redisTemplate.execute(bloomAddScript, Collections.singletonList(noteBloomKey), noteId);
        }
        // ------------------------- 笔记发布者获得的点赞数变更记录 -------------------------
        // 笔记发布者对应的 Bloom Key
        String userBloomKey = RedisKeyConstants.buildBloomUserNoteLikeUserIdListKey(date);
        // 执行 Lua 脚本，拿到返回结果
        result = redisTemplate.execute(script, Collections.singletonList(userBloomKey), noteCreatorId);
        // 若布隆过滤器判断不存在（绝对正确）
        if (Objects.equals(result, 0L)) {
            // 2. 若无，才会落库，减轻数据库压力

            // 根据分片总数，取模，获取对应的分片序号
            long userIdHashKey = noteCreatorId % tableShards;

            try {
                // 将日增量变更数据落库
                // - t_data_align_user_like_count_temp_日期_分片序号
                insertRecordMapper.insert2DataAlignUserLikeCountTempTable(TableConstants.buildTableNameSuffix(date, userIdHashKey), noteCreatorId);
            } catch (Exception e) {
                log.error("", e);
            }

            // 4. 数据库写入成功后，再添加布隆过滤器中
            redisTemplate.execute(bloomAddScript, Collections.singletonList(userBloomKey), noteCreatorId);
        }
    }
}
