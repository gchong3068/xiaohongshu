package com.gchong3068.xiaohongshu.kv.biz.domain.repository;

import com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.CommentContentDO;
import com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.CommentContentPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;
import java.util.UUID;

/**
 * @Description: Cassandra
 * @Author: gchong3068
 * @Date: 2025/2/15 10:14
 * @Version: v1.0.0
 **/
public interface CommentContentRepository extends CassandraRepository<CommentContentDO, CommentContentPrimaryKey> {

    /**
     * 批量查询评论内容
     * @Auther: gchong3068
     * @Date: 2025/2/15 10:24
 	 * @param noteId 笔记Id
 	 * @param yearMonthIn 日期
 	 * @param contentIds 评论ID集合
 	 * @return java.util.List<com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.CommentContentDO>
     **/
    List<CommentContentDO> findByPrimaryKeyNoteIdAndPrimaryKeyYearMonthInAndPrimaryKeyContentIdIn(
            Long noteId,List<String> yearMonthIn,List<UUID> contentIds
    );

	/**
	 * 删除评论正文
	 * @Auther: gchong3068
	 * @Date: 2025/6/2 21:05
	 * @param noteId
	 * @param yearMonth
	 * @param contentId
	 **/
	void deleteByPrimaryKeyNoteIdAndPrimaryKeyYearMonthAndPrimaryKeyContentId(Long noteId , String yearMonth,UUID contentId);
}
