package com.gchong3068.xiaohongshu.user.biz.domain.mapper;

import com.gchong3068.xiaohongshu.user.biz.domain.dataobject.RoleDO;

import java.util.List;

public interface RoleDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RoleDO record);

    int insertSelective(RoleDO record);

    RoleDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RoleDO record);

    int updateByPrimaryKey(RoleDO record);

    /**
     * 查询所有角色
     */
    List<RoleDO> selectEnabledList();
}