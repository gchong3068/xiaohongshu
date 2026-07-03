package com.gchong3068.xiaohongshu.kv.biz.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

/**
 * @Description: 评论内容
 * @Author: gchong3068
 * @Date: 2025/1/10 15:19
 * @Version: v1.0.0
 **/
@Table("comment_content")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentContentDO {

    @PrimaryKey
    private CommentContentPrimaryKey primaryKey;

    private String content;

}
