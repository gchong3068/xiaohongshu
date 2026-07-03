package com.gchong3068.xiaohongshu.kv.biz;

import com.gchong3068.framework.common.util.JsonUtils;
import com.gchong3068.xiaohongshu.kv.biz.domain.dataobject.NoteContentDO;
import com.gchong3068.xiaohongshu.kv.biz.domain.repository.NoteContentRepository;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

/**
 * @Description: Cassandra 测试
 * @Author: gchong3068
 * @Date: 2024/7/26 23:17
 * @Version: v1.0.0
 **/
@SpringBootTest
@Slf4j
class CassandraTests {

    @Resource
    private NoteContentRepository noteContentRepository;


    /**
     * 测试插入
     * @Auther: gchong3068
     * @Date: 2024/7/26 23:20
     **/
    @Test
    void testInsert(){
            NoteContentDO noteContentDO = NoteContentDO.builder()
                    .id(UUID.randomUUID())
                    .content("测试")
                    .build();
            noteContentRepository.save(noteContentDO);

    }

    /**
     * 测试修改数据
     */
    @Test
    void testUpdate() {
        NoteContentDO nodeContent = NoteContentDO.builder()
                .id(UUID.fromString("bb773af3-1cf1-4935-851d-ce089f96525d"))
                .content("代码测试笔记内容更新")
                .build();

        noteContentRepository.save(nodeContent);
    }

    /**
     * 测试查询数据
     */
    @Test
    void testSelect() {
        Optional<NoteContentDO> optional = noteContentRepository.findById(UUID.fromString("bb773af3-1cf1-4935-851d-ce089f96525d"));
        optional.ifPresent(noteContentDO -> log.info("查询结果：{}", JsonUtils.toJsonString(noteContentDO)));
    }

    /**
     * 测试删除数据
     */
    @Test
    void testDelete() {
        noteContentRepository.deleteById(UUID.fromString("bb773af3-1cf1-4935-851d-ce089f96525d"));
    }




}
