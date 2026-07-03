package com.gchong3068.xiaohongshu.kv.biz.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

/**
 * @Description: 评论内容主题
 * @Author: gchong3068
 * @Date: 2025/1/10 15:20
 * @Version: v1.0.0
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PrimaryKeyClass
public class CommentContentPrimaryKey {

    @PrimaryKeyColumn(name = "note_id",type = PrimaryKeyType.PARTITIONED)
    private Long noteId;  // 分区键1

    @PrimaryKeyColumn(name = "year_month",type = PrimaryKeyType.PARTITIONED)
    private String yearMonth;  // 分区键2

    @PrimaryKeyColumn(name = "content_id",type = PrimaryKeyType.CLUSTERED)
    private UUID contentId;  // 聚簇键

}
