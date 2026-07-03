package com.gchong3068.xiaohongshu.data.align.job;

import com.gchong3068.xiaohongshu.data.align.constant.TableConstants;
import com.gchong3068.xiaohongshu.data.align.domain.mapper.DeleteTableMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * @Description: 定时任务：自动删除最近一个月的日增量临时表
 * @Author: gchong3068
 * @Date: 2024/11/12 18:49
 * @Version: v1.0.0
 **/
@Component
public class DeleteTableXxlJob {

    @Value("${table.shards}")
    private int tableShards;
    @Resource
    private DeleteTableMapper deleteTableMapper;

    @XxlJob("deleteTableJobHandler")
    public void deleteTableJobHandler() throws Exception {
        XxlJobHelper.log("## 开始删除最近一个月的日增量临时表");

        // 今日
        LocalDate today = LocalDate.now();

        // 日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        LocalDate startDate = today;
        // 从昨天开始往前推一个月
        LocalDate endDate = today.minusMonths(1);

        // 循环最近一个月的日期，不包括今天
        while (startDate.isAfter(endDate)) {
            // 往前推一天
            startDate = startDate.minusDays(1);
            // 日期字符串
            String date = startDate.format(formatter);

            for (int hashKey = 0; hashKey < tableShards; hashKey++) {
                // 表名后缀
                String tableNameSuffix = TableConstants.buildTableNameSuffix(date, hashKey);
                XxlJobHelper.log("删除表后缀: {}", tableNameSuffix);

                // 删除表
                deleteTableMapper.deleteDataAlignFollowingCountTempTable(tableNameSuffix);
                deleteTableMapper.deleteDataAlignFansCountTempTable(tableNameSuffix);
                deleteTableMapper.deleteDataAlignNoteCollectCountTempTable(tableNameSuffix);
                deleteTableMapper.deleteDataAlignUserCollectCountTempTable(tableNameSuffix);
                deleteTableMapper.deleteDataAlignUserLikeCountTempTable(tableNameSuffix);
                deleteTableMapper.deleteDataAlignNoteLikeCountTempTable(tableNameSuffix);
                deleteTableMapper.deleteDataAlignNotePublishCountTempTable(tableNameSuffix);
            }
        }
        XxlJobHelper.log("## 结束删除最近一个月的日增量临时表");
    }

}
