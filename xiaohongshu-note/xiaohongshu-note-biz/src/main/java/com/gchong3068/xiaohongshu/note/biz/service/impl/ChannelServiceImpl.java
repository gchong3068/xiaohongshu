package com.gchong3068.xiaohongshu.note.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.ChannelDO;
import com.gchong3068.xiaohongshu.note.biz.domain.mapper.ChannelDOMapper;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindChannelRspVO;
import com.gchong3068.xiaohongshu.note.biz.service.ChannelService;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: TODO
 * @Author: gchong3068
 * @Date: 2025/6/12 17:29
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    @Resource
    private ChannelDOMapper channelDOMapper;


    @Override
    public Response<List<FindChannelRspVO>> findChannelList() {
        // TODO: 加二级缓存

        List<ChannelDO> channelDOS = channelDOMapper.selectAll();

        List<FindChannelRspVO> channelRspVOS = Lists.newArrayList();

        // 默认添加一个 “全部” 分类
        // FindChannelRspVO allChannel = FindChannelRspVO.builder()
        //         .id(0L)
        //         .name("全部")
        //         .build();
        // channelRspVOS.add(allChannel);

        if (CollUtil.isNotEmpty(channelDOS)) {
            CollUtil.addAll(channelRspVOS, channelDOS.stream()
                    .map(channelDO -> FindChannelRspVO.builder()
                            .id(channelDO.getId())
                            .name(channelDO.getName())
                            .build())
                    .toList());
        }

        return Response.success(channelRspVOS);
    }
}
