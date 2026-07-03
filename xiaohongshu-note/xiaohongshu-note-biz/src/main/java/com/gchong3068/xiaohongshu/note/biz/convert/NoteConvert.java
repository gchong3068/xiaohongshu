package com.gchong3068.xiaohongshu.note.biz.convert;

import com.gchong3068.xiaohongshu.note.biz.domain.dataobject.NoteDO;
import com.gchong3068.xiaohongshu.note.biz.model.dto.PublishNoteDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description: 实体类转换
 * @Author: gchong3068
 * @Date: 2025/6/11 23:48
 * @Version: v1.0.0
 **/
@Mapper
public interface NoteConvert {

    /**
     * 初始化 convert 实例
     * @Auther: gchong3068
     * @Date: 2025/6/11 23:49
     * @param null
     * @return null
     **/
    NoteConvert INSTANCE = Mappers.getMapper(NoteConvert.class);

    /**
     * 将 DO 转化为 DTO
     * @param bean
     * @return
     */
    PublishNoteDTO convertDO2DTO(NoteDO bean);

    /**
     * 将 DTO 转化为 DO
     * @param bean
     * @return
     */
    NoteDO convertDTO2DO(PublishNoteDTO bean);

}
