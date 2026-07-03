package com.gchong3068.xiaohongshu.kv.biz.domain.dataobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

/**
 * @Description: 笔记内容
 * @Author: gchong3068
 * @Date: 2024/7/26 23:07
 * @Version: v1.0.0
 **/
@Table("note_content")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteContentDO {

    @PrimaryKey("id")
    private UUID id;

    private String content;
}
