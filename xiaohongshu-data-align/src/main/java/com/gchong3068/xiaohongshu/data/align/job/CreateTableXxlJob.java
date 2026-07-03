package com.gchong3068.xiaohongshu.data.align.job;

import com.gchong3068.xiaohongshu.data.align.constant.TableConstants;
import com.gchong3068.xiaohongshu.data.align.domain.mapper.CreateTableMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description: 定时任务：自动创建日增量计数变更表
 * @Author: gchong3068
 * @Date: 2024/11/7 14:09
 * @Version: v1.0.0
 **/
@Component
@RefreshScope
public class CreateTableXxlJob {

    /*
    表分片总数
     */
    @Value("${table.shards}")
    private int tableShards;

    @Resource
    private CreateTableMapper createTableMapper;

    @XxlJob("createTableJobHandler")
    public void createTableJobHandler() throws Exception {
        String date = LocalDateTime.now().plusDays(1)
                        .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        XxlJobHelper.log("## 开始创建日增量数据表，日期: {}...", date);

        if (tableShards > 0) {
            for (int hashKey = 0; hashKey < tableShards; hashKey++) {
                // 表名后缀
                String tableNameSuffix = TableConstants.buildTableNameSuffix(date, hashKey);

                // 创建表
                createTableMapper.createDataAlignFollowingCountTempTable(tableNameSuffix);
                createTableMapper.createDataAlignFansCountTempTable(tableNameSuffix);
                createTableMapper.createDataAlignNoteCollectCountTempTable(tableNameSuffix);
                createTableMapper.createDataAlignUserCollectCountTempTable(tableNameSuffix);
                createTableMapper.createDataAlignUserLikeCountTempTable(tableNameSuffix);
                createTableMapper.createDataAlignNoteLikeCountTempTable(tableNameSuffix);
                createTableMapper.createDataAlignNotePublishCountTempTable(tableNameSuffix);
            }
        }

        XxlJobHelper.log("## 结束创建日增量数据表，日期: {}...", date);


    }

}
