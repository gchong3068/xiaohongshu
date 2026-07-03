package com.gchong3068.xiaohongshu.kv.biz.domain.repository;

import com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.NoteContentDO;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

/**
 * @Description: Cassandra 笔记内容存储
 * @Author: gchong3068
 * @Date: 2024/7/26 23:08
 * @Version: v1.0.0
 **/
public interface NoteContentRepository extends CassandraRepository<NoteContentDO, UUID> {

}
