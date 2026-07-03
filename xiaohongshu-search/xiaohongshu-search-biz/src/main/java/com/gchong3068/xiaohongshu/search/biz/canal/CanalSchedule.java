package com.gchong3068.xiaohongshu.search.biz.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.gchong3068.framework.common.enums.StatusEnum;
import com.gchong3068.xiaohongshu.search.biz.domain.mapper.SelectMapper;
import com.gchong3068.xiaohongshu.search.biz.enums.NoteStatusEnum;
import com.gchong3068.xiaohongshu.search.biz.enums.NoteVisibleEnum;
import com.gchong3068.xiaohongshu.search.biz.index.NoteIndex;
import com.gchong3068.xiaohongshu.search.biz.index.UserIndex;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Canal 定时任务
 * @Author: gchong3068
 * @Date: 2024/12/16 11:57
 * @Version: v1.0.0
 **/
@Component
@Slf4j
public class CanalSchedule implements Runnable{

    @Resource
    private CanalProperties canalProperties;
    @Resource
    private CanalConnector canalConnector;
    @Resource
    private SelectMapper selectMapper;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Override
    @Scheduled(fixedDelay = 100)
    public void run() {
        long batchId = -1L;

        try{
            //从CanalConner 获取信息 返回的数据量由 batchSize 控制，若不足，则拉取已有
            Message message = canalConnector.getWithoutAck(canalProperties.getBatchSize());

            //获取当前信息批次 ID
            batchId = message.getId();

            //获取条数
            int size = message.getEntries().size();
            if (batchId == -1 || size == 0){
                try{
                    //拉取为空 ，休眠1s
                    TimeUnit.SECONDS.sleep(1);
                }catch (InterruptedException ignored){
                }
            }else {
                //如果当前批次有数据，打印
                processEntry(message.getEntries());
            }

            canalConnector.ack(batchId);
        }catch (Exception e){
            log.error("消费Canal 数据异常",e);
            //数据回溯
            canalConnector.rollback(batchId);
        }

    }

    /**
     * 处理批次数据
     * @Auther: gchong3068
     * @Date: 2024/12/16 21:28
 	 * @param entrys 批次数据集合
     **/
    private void processEntry(List<CanalEntry.Entry> entrys) throws Exception{
        for (CanalEntry.Entry entry : entrys){
            // 只处理 ROWDATA 行数据类型的 Entry，忽略事务等其他类型
            if(entry.getEntryType() == CanalEntry.EntryType.ROWDATA){
                CanalEntry.EventType eventType = entry.getHeader().getEventType();
                //数据库名称
                String database  = entry.getHeader().getSchemaName();
                //表名
                String table = entry.getHeader().getTableName();

                //解析RowChange
                CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());

                //遍历
                for (CanalEntry.RowData rowData : rowChange.getRowDatasList()){

                    List<CanalEntry.Column> columns = rowData.getAfterColumnsList();

                    Map<String,Object> columMap = parseColumns2Map(columns);

                    log.info("EventType: {}, Database: {}, Table: {}, Columns: {}", eventType, database, table, columMap);
                
                    processEvent(columMap,table,eventType);
                
                }
            }
        }
    }
    /**
     * 打印字段信息
     * @Auther: gchong3068
     * @Date: 2024/12/16 12:14
     * @param columns 字段集合
     **/
    private static void printColumn(List<CanalEntry.Column> columns) {
        for (CanalEntry.Column column : columns){
            System.out.println(column.getName() + " : " + column.getValue() + "    update=" + column.getUpdated());
        }
    }
    
    /**
     * 将列数据解析为Map
     * @Auther: gchong3068
     * @Date: 2024/12/16 21:36 
 	 * @param columns 列数据集合
 	 * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    private Map<String,Object> parseColumns2Map(List<CanalEntry.Column > columns){
        Map<String,Object> columnMap = Maps.newHashMap();
        columns.forEach(column -> {
            if (Objects.isNull(column)) return;
            columnMap.put(column.getName(),column.getValue());
        });
        return columnMap;
    }
    
    /**
     * 处理事件
     * @Auther: gchong3068
     * @Date: 2024/12/17 18:18 
 	 * @param columnMap 事件参数
 	 * @param table 表名
 	 * @param eventType 类型
     **/
    private void processEvent(Map<String,Object> columnMap,String table ,CanalEntry.EventType eventType) throws IOException {
        switch (table){ 
            case "t_note" -> handleNoteEvent(columnMap,eventType);
            case "t_user" -> handleUserEvent(columnMap,eventType);
            default -> log.warn("Table: {} not support", table);
        }
    }
    
    /**
     * 笔记表事件
     * @Auther: gchong3068
     * @Date: 2024/12/17 18:19 
 	 * @param columnMap 事件参数
 	 * @param eventType 类型
     **/
    private void handleNoteEvent(Map<String,Object> columnMap, CanalEntry.EventType eventType) throws IOException {
        long noteId = Long.parseLong(columnMap.get("note_id").toString());


        switch(eventType){
            case INSERT -> syncNoteIndex(noteId); //记录新增事件
            case UPDATE -> {
                //笔记变更可见后状态
                Integer  status = Integer.parseInt(columnMap.get("status").toString());
                // 笔记可见范围
                Integer visible = Integer.parseInt(columnMap.get("visible").toString());

                if (Objects.equals(status, NoteStatusEnum.NORMAL.getCode())
                        &&Objects.equals(visible, NoteVisibleEnum.PUBLIC.getCode())){ //正常
                    syncNoteIndex(noteId);
                } else if (Objects.equals(visible, NoteVisibleEnum.PRIVATE.getCode()) // 仅对自己可见
                        || Objects.equals(status, NoteStatusEnum.DELETED.getCode())
                        || Objects.equals(status, NoteStatusEnum.DOWNED.getCode()))  {

                    deleteNoteDocument(String.valueOf(noteId));  //删除
                }
            }
            default -> log.warn("Unhandled event type for t_note: {}", eventType);
        }
    }
    
    /**
     * 用户表事件
     * @Auther: gchong3068
     * @Date: 2024/12/17 18:20 
 	 * @param columnMap 事件参数
 	 * @param eventType 类型
     **/
    private void handleUserEvent(Map<String,Object> columnMap, CanalEntry.EventType eventType) throws IOException {
        long userId = Long.parseLong(columnMap.get("id").toString());

        switch(eventType){
            case INSERT -> syncUserIndex(userId);
            case UPDATE -> {
                // 用户变更后的状态
                Integer status = Integer.parseInt(columnMap.get("status").toString());
                // 逻辑删除
                Integer isDeleted = Integer.parseInt(columnMap.get("is_deleted").toString());

                if (Objects.equals(status, StatusEnum.ENABLE.getValue())
                        && Objects.equals(isDeleted, 0)) { // 用户状态为已启用，并且未被逻辑删除
                    // 更新用户索引、笔记索引
                    syncNotesIndexAndUserIndex(userId);
                } else if (Objects.equals(status, StatusEnum.DISABLED.getValue()) // 用户状态为禁用
                        || Objects.equals(isDeleted, 1)) { // 被逻辑删除
                    // 删除用户文档
                    deleteUserDocument(String.valueOf(userId));
                }
            }
            default -> log.warn("Unhandled event type for t_user: {}", eventType);
        }

    }

    /**
     * 同步笔记索引
     * @Auther: gchong3068
     * @Date: 2024/12/17 19:47
 	 * @param noteId 笔记id
     **/
    private void syncNoteIndex(Long noteId) throws IOException {
        //从数据库差查询 Elasticsearch 索引数据
        List<Map<String, Object>> result = selectMapper.selectEsNoteIndexData(noteId,null);

        //遍历
        for (Map<String , Object> recordMap : result ){
            //创建索引对象
            IndexRequest indexRequest = new IndexRequest(NoteIndex.NAME);
            //设置文档ID
            indexRequest.id(String.valueOf(recordMap.get(NoteIndex.FIELD_NOTE_ID)));
            //文档内容
            indexRequest.source(recordMap);
            //写入Elasticsearch 索引
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        }
    }

    /**
     * 同步用户索引
     * @Auther: gchong3068
     * @Date: 2024/12/18 13:54
     * @param userId 用户ID
     **/
    private void syncUserIndex(Long userId) throws IOException {

        List<Map<String, Object>> userResult = selectMapper.selectEsUserIndexDAta(userId);

        for (Map<String , Object> recordMap :userResult){
            // 创建索引请求对象，指定索引名称
            IndexRequest indexRequest = new IndexRequest(UserIndex.NAME);
            // 设置文档的 ID，使用记录中的主键 “id” 字段值
            indexRequest.id((String.valueOf(recordMap.get(UserIndex.FIELD_USER_ID))));
            // 设置文档的内容，使用查询结果的记录数据
            indexRequest.source(recordMap);
            // 将数据写入 Elasticsearch 索引
            restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);

        }

    }


    /**
     * 删除 笔记文档
     * @Auther: gchong3068
     * @Date: 2024/12/18 13:54
     * @param documentId 笔记ID
     **/
    private void deleteNoteDocument(String documentId) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(NoteIndex.NAME, documentId);
        restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
    }


    /**
     * 同步用户索引 、笔记索引
     * @Auther: gchong3068
     * @Date: 2024/12/18 13:56 
     * @param userId 用户ID
     **/
    private void syncNotesIndexAndUserIndex(Long userId) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();
        // 1. 用户索引
        List<Map<String, Object>> userResult = selectMapper.selectEsUserIndexDAta(userId);

        for (Map<String, Object> recordMap : userResult){
            IndexRequest indexRequest = new IndexRequest(UserIndex.NAME);
            indexRequest.id(String.valueOf(recordMap.get(UserIndex.FIELD_USER_ID)));
            indexRequest.source(recordMap);
            bulkRequest.add(indexRequest);
        }

        //2. 笔记索引
        List<Map<String, Object>> noteResult  = selectMapper.selectEsNoteIndexData(null,userId);

        for (Map<String, Object> recordMap : noteResult){
            IndexRequest indexRequest = new IndexRequest(NoteIndex.NAME);
            indexRequest.id(String.valueOf(recordMap.get(NoteIndex.FIELD_NOTE_ID)));
            indexRequest.source(recordMap);
            bulkRequest.add(indexRequest);
        }
        restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);
    }

    /**
     * 删除 用户文档
     * @Auther: gchong3068
     * @Date: 2024/12/18 14:05
     * @param documentId 用户ID
     **/
    private void deleteUserDocument(String documentId) throws IOException {
        // 创建删除请求对象，指定索引名称和文档 ID
        DeleteRequest deleteRequest = new DeleteRequest(UserIndex.NAME, documentId);
        // 执行删除操作，将指定文档从 Elasticsearch 索引中删除
        restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    }
    
}
