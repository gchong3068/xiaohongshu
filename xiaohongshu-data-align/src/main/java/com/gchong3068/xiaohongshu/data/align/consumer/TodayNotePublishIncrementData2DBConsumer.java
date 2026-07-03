package com.gchong3068.xiaohongshu.data.align.consumer;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.data.align.constant.MQConstants;
import com.gchong3068.xiaohongshu.data.align.constant.RedisKeyConstants;
import com.gchong3068.xiaohongshu.data.align.constant.TableConstants;
import com.gchong3068.xiaohongshu.data.align.domain.mapper.InsertMapper;
import com.gchong3068.xiaohongshu.data.align.model.dto.NoteOperateMqDTO;
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
 * @Description: 日增量数据库落库：笔记发布、删除
 * @Author: gchong3068
 * @Date: 2024/11/13 17:15
 * @Version: v1.0.0
 **/
@Component
@RocketMQMessageListener(consumerGroup = "xiaohongshu_group_data_align_" + MQConstants.TOPIC_NOTE_OPERATE,
        topic = MQConstants.TOPIC_NOTE_OPERATE)
@Slf4j
public class TodayNotePublishIncrementData2DBConsumer implements RocketMQListener<String> {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;
    @Resource
    private InsertMapper insertRecordMapper;

    @Value("${table.shards}")
    private int tableShards;

    @Override
    public void onMessage(String body) {
        log.info("## TodayNotePublishIncrementData2DBConsumer 消费到了 MQ: {}", body);

        // 消息体 JSON 字符串转 DTO
        NoteOperateMqDTO noteOperateMqDTO = JsonUtils.parseObject(body, NoteOperateMqDTO.class);

        if (Objects.isNull(noteOperateMqDTO)) return;

        //发布，被删除着ID
        Long noteCreatorId = noteOperateMqDTO.getCreatorId();

        //日期
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String bloomKey = RedisKeyConstants.buildBloomUserNoteOperateListKey(date);

        DefaultRedisScript<Long> script = new DefaultRedisScript<>();

        script.setScriptSource(new ResourceScriptSource(new ClassPathResource("/lua/bloom_today_user_note_publish_check.lua")));

        script.setResultType(Long.class);

        Long result = redisTemplate.execute(script, Collections.singletonList(bloomKey), noteCreatorId);

        if (Objects.equals(result,0L)){
            // 根据分片总数，取模，分别获取对应的分片序号
            long userIdHashKey = noteCreatorId % tableShards;

            insertRecordMapper.insert2DataAlignUserNotePublishCountTempTable(TableConstants.buildTableNameSuffix(date, userIdHashKey), noteCreatorId);

            RedisScript<Long> bloomAddScript = RedisScript.of("return redis.call('BF.ADD', KEYS[1], ARGV[1])", Long.class);
            redisTemplate.execute(bloomAddScript, Collections.singletonList(bloomKey), noteCreatorId);
        }
    }
}
