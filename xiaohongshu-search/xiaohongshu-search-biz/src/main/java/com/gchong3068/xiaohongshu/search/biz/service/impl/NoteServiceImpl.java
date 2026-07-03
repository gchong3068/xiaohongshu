package com.gchong3068.xiaohongshu.search.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.gchong3068.framework.common.constant.DateConstants;
import com.gchong3068.framework.common.response.PageResponse;
import com.gchong3068.framework.common.response.Response;
import com.gchong3068.framework.common.util.DateUtils;
import com.gchong3068.framework.common.util.NumberUtils;
import com.gchong3068.xiaohongshu.search.biz.domain.mapper.SelectMapper;
import com.gchong3068.xiaohongshu.search.biz.enums.NotePublishTimeRangeEnum;
import com.gchong3068.xiaohongshu.search.biz.enums.NoteSortTypeEnum;
import com.gchong3068.xiaohongshu.search.biz.index.NoteIndex;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchNoteReqVO;
import com.gchong3068.xiaohongshu.search.biz.model.vo.SearchNoteRspVO;
import com.gchong3068.xiaohongshu.search.biz.service.NoteService;
import com.gchong3068.xiaohongshu.search.dto.RebuildNoteDocumentReqDTO;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 笔记服务实现类
 * @Author: gchong3068
 * @Date: 2024/12/6 13:30
 * @Version: v1.0.0
 **/
@Service
@Slf4j
public class NoteServiceImpl implements NoteService {
    
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private SelectMapper selectMapper;


    /**
     * 搜索笔记
     * @Auther: gchong3068
     * @Date: 2024/12/6 14:07
     * @param searchNoteReqVO 搜索笔记入参
     * @return com.gchong3068.framework.common.response.PageResponse<com.gchong3068.xiaohongshu.search.model.vo.SearchNoteRspVO>
     **/
    @Override
    public PageResponse<SearchNoteRspVO> searchNote(SearchNoteReqVO searchNoteReqVO) {
        String keyword = searchNoteReqVO.getKeyword();
        Integer pageNo = searchNoteReqVO.getPageNo();
        Integer type = searchNoteReqVO.getType();
        Integer sort = searchNoteReqVO.getSort();
        Integer publishTimeRange = searchNoteReqVO.getPublishTimeRange();

        SearchRequest searchRequest = new SearchRequest(NoteIndex.NAME);

        //构建查询器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder boolQueryBuilder  = QueryBuilders.boolQuery().must(
                QueryBuilders.multiMatchQuery(keyword)
                        .field(NoteIndex.FIELD_NOTE_TITLE,2.0f) //标题权重2.0
                        .field(NoteIndex.FIELD_NOTE_TOPIC));

        // 按笔记类型过滤
        if (Objects.nonNull(type)){
            boolQueryBuilder.filter(QueryBuilders.termQuery(NoteIndex.FIELD_NOTE_TYPE,type));
        }

        //按发布时间过滤
        NotePublishTimeRangeEnum notePublishTimeRangeEnum  = NotePublishTimeRangeEnum.valueOf(publishTimeRange);

        if (Objects.nonNull(notePublishTimeRangeEnum)) {
            // 结束时间
            String endTime = LocalDateTime.now().format(DateConstants.DATE_FORMAT_Y_M_D_H_M_S);
            // 开始时间
            String startTime = null;

            switch (notePublishTimeRangeEnum) {
                case DAY -> startTime = DateUtils.localDateTime2String(LocalDateTime.now().minusDays(1)); // 一天之前的时间
                case WEEK -> startTime = DateUtils.localDateTime2String(LocalDateTime.now().minusWeeks(1)); // 一周之前的时间
                case HALF_YEAR ->
                        startTime = DateUtils.localDateTime2String(LocalDateTime.now().minusMonths(6)); // 半年之前的时间
            }
            // 设置时间范围
            if (StringUtils.isNoneBlank(startTime)) {
                boolQueryBuilder.filter(QueryBuilders.rangeQuery(NoteIndex.FIELD_NOTE_CREATE_TIME)
                        .gte(startTime) // 大于等于
                        .lte(endTime) // 小于等于
                );
            }
        }

        //排序
        NoteSortTypeEnum noteSortTypeEnum = NoteSortTypeEnum.valueOf(sort);


        if (Objects.nonNull(noteSortTypeEnum)){


            switch(noteSortTypeEnum){
                // 按笔记发布时间降序
                case LATEST -> sourceBuilder.sort(new FieldSortBuilder(NoteIndex.FIELD_NOTE_CREATE_TIME).order(SortOrder.DESC));
                // 按笔记点赞量降序
                case MOST_LIKE -> sourceBuilder.sort(new FieldSortBuilder(NoteIndex.FIELD_NOTE_LIKE_TOTAL).order(SortOrder.DESC));
                // 按评论量降序
                case MOST_COMMENT -> sourceBuilder.sort(new FieldSortBuilder(NoteIndex.FIELD_NOTE_COMMENT_TOTAL).order(SortOrder.DESC));
                // 按收藏量降序
                case MOST_COLLECT -> sourceBuilder.sort(new FieldSortBuilder(NoteIndex.FIELD_NOTE_COLLECT_TOTAL).order(SortOrder.DESC));
            }

            sourceBuilder.query(boolQueryBuilder);
        }else {
            // 综合排序，自定义评分，并按 _score 评分降序
            sourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));

            FunctionScoreQueryBuilder.FilterFunctionBuilder[] filterFunctionBuilders = new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{
                    //function 1
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            new FieldValueFactorFunctionBuilder(NoteIndex.FIELD_NOTE_LIKE_TOTAL)
                                    .factor(0.5f)
                                    .modifier(FieldValueFactorFunction.Modifier.SQRT)
                                    .missing(0)
                    ),
                    //function 2
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            new FieldValueFactorFunctionBuilder(NoteIndex.FIELD_NOTE_COLLECT_TOTAL)
                                    .factor(0.3f)
                                    .modifier(FieldValueFactorFunction.Modifier.SQRT)
                                    .missing(0)
                    ),
                    //function 3
                    new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                            new FieldValueFactorFunctionBuilder(NoteIndex.FIELD_NOTE_COMMENT_TOTAL)
                                    .factor(0.2f)
                                    .modifier(FieldValueFactorFunction.Modifier.SQRT)
                                    .missing(0)
                    )
            };
            // 构建 function_score 查询
            // "score_mode": "sum",
            // "boost_mode": "sum"
            FunctionScoreQueryBuilder  functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder, filterFunctionBuilders)
                    .scoreMode(FunctionScoreQuery.ScoreMode.SUM)
                    .boostMode(CombineFunction.SUM);

            //设置查询
            sourceBuilder.query(functionScoreQueryBuilder);
        }

        //设置分页
        int pageSize = 10;
        int from = (pageNo-1)*pageSize;
        sourceBuilder.from(from);
        sourceBuilder.size(pageSize);

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field(NoteIndex.FIELD_NOTE_TITLE)
                .preTags("<strong>")
                .postTags("</strong>");
        sourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(sourceBuilder);

        //返参
        List<SearchNoteRspVO> searchNoteRspVOS = null;
        long total = 0;
        try{
            log.info("==> SearchRequest: {}", searchRequest);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            
            //处理搜索结果
            total = searchResponse.getHits().getTotalHits().value;
            log.info("==> 命中文档总数， hits：{}",total);
            
            searchNoteRspVOS = Lists.newArrayList();

            SearchHits hits = searchResponse.getHits();

            for (SearchHit hit : hits){
                log.info("==> 文档数据：{}",hit.getSourceAsString());

                //map返回文档字段
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                //提取特定字段
                Long noteId = (Long) sourceAsMap.get(NoteIndex.FIELD_NOTE_ID);
                String cover = (String) sourceAsMap.get(NoteIndex.FIELD_NOTE_COVER);
                String title = (String) sourceAsMap.get(NoteIndex.FIELD_NOTE_TITLE);
                String avatar = (String) sourceAsMap.get(NoteIndex.FIELD_NOTE_AVATAR);
                String nickname = (String) sourceAsMap.get(NoteIndex.FIELD_NOTE_NICKNAME);
                //更新时间
                String updateTimeStr = (String) sourceAsMap.get(NoteIndex.FIELD_NOTE_UPDATE_TIME);
                LocalDateTime updateTime = LocalDateTime.parse(updateTimeStr, DateConstants.DATE_FORMAT_Y_M_D_H_M_S);
                Integer likeTotal = (Integer) sourceAsMap.get(NoteIndex.FIELD_NOTE_LIKE_TOTAL);
                Integer commentTotal = (Integer) sourceAsMap.get(NoteIndex.FIELD_NOTE_COMMENT_TOTAL);
                Integer collectTotal = (Integer) sourceAsMap.get(NoteIndex.FIELD_NOTE_COLLECT_TOTAL);

                //高亮字段
                String highlightedTitle = null;
                if (CollUtil.isNotEmpty(hit.getHighlightFields()) && hit.getHighlightFields().containsKey(NoteIndex.FIELD_NOTE_TITLE)){
                    highlightedTitle = hit.getHighlightFields().get(NoteIndex.FIELD_NOTE_TITLE).fragments()[0].string();
                }

                //构建VO
                SearchNoteRspVO searchNoteRspVO = SearchNoteRspVO.builder()
                        .noteId(noteId)
                        .cover(cover)
                        .title(title)
                        .highlightTitle(highlightedTitle)
                        .avatar(avatar)
                        .nickname(nickname)
                        .updateTime(DateUtils.formatRelativeTime(updateTime))
                        .likeTotal(NumberUtils.formatNumberString(likeTotal))
                        .commentTotal(NumberUtils.formatNumberString(commentTotal))
                        .collectTotal(NumberUtils.formatNumberString(collectTotal))
                        .build();
                searchNoteRspVOS.add(searchNoteRspVO);
            }
        }catch (Exception e){
            log.error("==>查询ElasticSearch 异常：",e);
        }
        return PageResponse.success(searchNoteRspVOS,pageNo,total);
    }


    /**
     * 重建用户文档
     * @Auther: gchong3068
     * @Date: 2024/12/19 17:06
     * @param rebuildNoteDocumentReqDTO 重建用户文档入参
     * @return com.gchong3068.framework.common.response.Response<java.lang.Long>
     **/
    @Override
    public Response<Long> rebuildDocument(RebuildNoteDocumentReqDTO rebuildNoteDocumentReqDTO) {

        Long noteId = rebuildNoteDocumentReqDTO.getId();

        //从数据库查询Elasticsearch 索引数据
        List<Map<String, Object>> result = selectMapper.selectEsNoteIndexData(noteId, null);

        //遍历
        for (Map<String,Object> recordMap :  result){

            IndexRequest indexRequest = new IndexRequest(NoteIndex.NAME);

            indexRequest.id(String.valueOf(recordMap.get(NoteIndex.FIELD_NOTE_ID)));
            indexRequest.source(recordMap);

            try{
                restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            }catch (IOException e){
                log.error("==> 重建笔记文档失败：",e);
            }
        }
        return Response.success();
    }
}
