package com.gchong3068.xiaohongshu.search.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.framework.common.util.NumberUtils;
import com.gchong3068.xiaohongshu.search.biz.domain.mapper.SelectMapper;
import com.gchong3068.xiaohongshu.search.biz.index.UserIndex;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchUserReqVO;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchUserRspVO;
import com.gchong3068.xiaohongshu.search.biz.service.UserService;
import com.gchong3068.xiaohongshu.search.dto.RebuildUserDocumentReqDTO;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Description: 用户搜索业务实现
 * @Author: gchong3068
 * @Date: 2024/12/4 16:16
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private SelectMapper selectMapper;


    /**
     * 搜索用户
     * @Auther: gchong3068
     * @Date: 2024/12/4 16:18
 	 * @param searchUserReqVO 搜索用户请求参数
 	 * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.search.model.vo.SearchUserRspVO>
     **/
    @Override
    public PageResponse<SearchUserRspVO> searchUser(SearchUserReqVO searchUserReqVO) {
        String keyWork = searchUserReqVO.getKeyword();

        Integer pageNo = searchUserReqVO.getPageNo();

        //构建 SearchRequest,置顶索引
        SearchRequest searchRequest = new SearchRequest(UserIndex.NAME);

        //构建查询库
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //构建multi_match 查询nickname 和xiaohongshu_id 字段
        sourceBuilder.query(QueryBuilders.multiMatchQuery(
                keyWork,UserIndex.FIELD_USER_NICKNAME,UserIndex.FIELD_USER_XIAOHONGSHU_ID));

        //排序
        SortBuilder<?> sortBuilder = new FieldSortBuilder(UserIndex.FIELD_USER_FANS_TOTAL)
                .order(SortOrder.DESC);
        sourceBuilder.sort(sortBuilder);

        //设置分页
        int pageSize = 10;
        int from = (pageNo-1)*pageSize;

        sourceBuilder.from(from);
        sourceBuilder.size(pageSize);

        //设置高亮字段
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(UserIndex.FIELD_USER_NICKNAME)
                        .preTags("<strong>")
                        .postTags("</strong>");
        sourceBuilder.highlighter(highlightBuilder);

        //设置到SearchRequest中
        searchRequest.source(sourceBuilder);

        //返参
        List<SearchUserRspVO> searchUserRspVOS = null;

        long total = 0;
        try{
            log.info("==> SearchRequest: {}", searchRequest);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

            total = searchResponse.getHits().getTotalHits().value;
            log.info("==> 命中文档总数： hits：{}",total);

            searchUserRspVOS = Lists.newArrayList();

            //获取文章列表
            SearchHits hits = searchResponse.getHits();

            for(SearchHit hit : hits){
                log.info("==> 文档数据 ：{}", hit.getSourceAsString());

                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                Long userId = ((Number) sourceAsMap.get(UserIndex.FIELD_USER_ID)).longValue();
                String nickname = (String) sourceAsMap.get(UserIndex.FIELD_USER_NICKNAME);
                String avatar = (String) sourceAsMap.get(UserIndex.FIELD_USER_AVATAR);
                String xiaohongshuId = (String) sourceAsMap.get(UserIndex.FIELD_USER_XIAOHONGSHU_ID);
                Integer noteTotal = (Integer) sourceAsMap.get(UserIndex.FIELD_USER_NOTE_TOTAL);
                Integer fansTotal = (Integer) sourceAsMap.get(UserIndex.FIELD_USER_FANS_TOTAL);

                //获取高亮
                String highlightedNickname  = null;
                if (CollUtil.isNotEmpty(hit.getHighlightFields())
                        && hit.getHighlightFields().containsKey(UserIndex.FIELD_USER_NICKNAME)){
                    highlightedNickname  = hit.getHighlightFields().get(UserIndex.FIELD_USER_NICKNAME).fragments()[0].string();
                }

                //构建VO
                SearchUserRspVO searchUserRspVO = SearchUserRspVO.builder()
                        .userId(userId)
                        .nickname(nickname)
                        .avatar(avatar)
                        .xiaohongshuId(xiaohongshuId)
                        .noteTotal(noteTotal)
                        .fansTotal(NumberUtils.formatNumberString(fansTotal))
                        .highlightNickname(highlightedNickname)
                        .build();

                searchUserRspVOS.add(searchUserRspVO);
            }
        }catch (Exception e){
            log.error("==> 查询 ElasticSearch 异常：",e);
        }

        return PageResponse.success(searchUserRspVOS,pageNo,total);
    }
    /**
     * 重建用户文档
     * @Auther: gchong3068
     * @Date: 2024/12/19 17:10
 	 * @param rebuildUserDocumentReqDTO 重建用户文档入参
 	 * @return com.gchong3068.framework.common.response.Response<java.lang.Long>
     **/
    @Override
    public Response<Long> rebuildDocument(RebuildUserDocumentReqDTO rebuildUserDocumentReqDTO) {
        Long userId = rebuildUserDocumentReqDTO.getId();
        //数据库查询
        List<Map<String, Object>> result = selectMapper.selectEsUserIndexDAta(userId);

        for (Map<String, Object> map : result) {

            IndexRequest indexRequest = new IndexRequest(UserIndex.NAME);
            indexRequest.id((String) map.get(UserIndex.FIELD_USER_ID));
            indexRequest.source(map);
            try{
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            }catch (IOException e){
                log.error("==>重建用户文档错误");
            }
        }
        return Response.success();
    }
}
