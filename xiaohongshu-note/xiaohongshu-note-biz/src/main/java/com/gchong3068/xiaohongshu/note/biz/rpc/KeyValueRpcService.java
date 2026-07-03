package com.gchong3068.xiaohongshu.note.biz.rpc;

import com.gchong3068.framework.common.response.Response;
import com.gchong3068.xiaohongshu.kv.api.KeyValueFeignApi;
import com.gchong3068.xiaohongshu.kv.dto.req.AddNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.DeleteNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.req.FindNoteContentReqDTO;
import com.gchong3068.xiaohongshu.kv.dto.rsp.FindNoteContentRspDTO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description:
 * @Author: gchong3068
 * @Date: 2024/8/17 11:01
 * @Version: v1.0.0
 **/
@Component
public class KeyValueRpcService {

    @Resource
    private KeyValueFeignApi keyValueFeignApi;

    /**
     * 保存笔记内容
     * @Auther: gchong3068
     * @Date: 2024/8/17 11:09
 	 * @param uuid uuid
 	 * @param content 笔记内容
 	 * @return boolean
     **/
    public boolean saveNoteContent(String uuid,String content){
        AddNoteContentReqDTO addNoteContentReqDTO = new AddNoteContentReqDTO();
        addNoteContentReqDTO.setUuid(uuid);
        addNoteContentReqDTO.setContent(content);
        Response<?> response = keyValueFeignApi.addNoteContent(addNoteContentReqDTO);
        return !Objects.isNull(response) && response.isSuccess();

    }

    /**
     * 删除笔记
     * @Auther: gchong3068
     * @Date: 2024/8/17 11:11
     * @param uuid uuid
     * @return boolean
     **/
    public boolean deleteNoteContent(String uuid){
        DeleteNoteContentReqDTO deleteNoteContentReqDTO = new DeleteNoteContentReqDTO();
        deleteNoteContentReqDTO.setUuid(uuid);
        Response<?> response = keyValueFeignApi.deleteNoteContent(deleteNoteContentReqDTO);
        return !Objects.isNull(response) && response.isSuccess();

    }

    /**
     * 查询笔记内容
     * @Auther: gchong3068
     * @Date: 2024/8/22 22:08
     * @param uuid uuid
     * @return java.lang.String
     **/

    public String findNoteContent(String uuid){
        FindNoteContentReqDTO findNoteContentReqDTO = new FindNoteContentReqDTO();
        findNoteContentReqDTO.setUuid(uuid);

        Response<FindNoteContentRspDTO> response = keyValueFeignApi.findNoteContent(findNoteContentReqDTO);

        if (Objects.isNull(response) || !response.isSuccess() || Objects.isNull(response.getData())){
            return null;
        }

        return response.getData().getContent();
    }
}
