package com.gchong3068.xiaohongshu.note.biz.domain.mapper;

import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.ChannelDO;

import java.util.List;

public interface ChannelDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChannelDO record);

    int insertSelective(ChannelDO record);

    ChannelDO selectByPrimaryKey(Long id);

    List<ChannelDO> selectAll();

    int updateByPrimaryKeySelective(ChannelDO record);

    int updateByPrimaryKey(ChannelDO record);
}