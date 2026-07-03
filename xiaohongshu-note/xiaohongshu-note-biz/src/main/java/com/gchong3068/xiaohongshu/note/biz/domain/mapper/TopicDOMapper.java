package com.gchong3068.xiaohongshu.note.biz.domain.mapper;

import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.TopicDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TopicDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TopicDO record);

    int insertSelective(TopicDO record);

    TopicDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TopicDO record);

    int updateByPrimaryKey(TopicDO record);

    String selectNameByPrimaryKey(Long id);


    List<TopicDO> selectByLikeName(String keyword);

    List<TopicDO> selectByTopicIdIn(List<Long> topicIds);

    TopicDO selectByTopicName(String name);

    int batchInsert(@Param("newTopics") List<TopicDO> newTopics);
}