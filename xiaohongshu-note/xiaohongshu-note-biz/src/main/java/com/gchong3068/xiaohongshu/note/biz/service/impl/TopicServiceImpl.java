package com.gchong3068.xiaohongshu.note.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.TopicDO;
import com.gchong3068.xiaohongshu.note.biz.domain.mapper.TopicDOMapper;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindTopicListReqVO;
import com.gchong3068.xiaohongshu.note.biz.model.vo.FindTopicRspVO;
import com.gchong3068.xiaohongshu.note.biz.service.TopicService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: 频道业务
 * @Author: gchong3068
 * @Date: 2025/6/12 18:01
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class TopicServiceImpl implements TopicService {

    @Resource
    private TopicDOMapper  topicDOMapper;


    @Override
    public Response<List<FindTopicRspVO>> findTopicList(FindTopicListReqVO findTopicListReqVO) {
        String keyword = findTopicListReqVO.getKeyword();

        List<TopicDO> topicDOS = topicDOMapper.selectByLikeName(keyword);

        List<FindTopicRspVO> findTopicRspVOS = null;
        if (CollUtil.isNotEmpty(topicDOS)) {
            findTopicRspVOS = topicDOS.stream()
                    .map(topicDO -> FindTopicRspVO.builder()
                            .id(topicDO.getId())
                            .name(topicDO.getName())
                            .build())
                    .toList();
        }

        return Response.success(findTopicRspVOS);
    }
}
