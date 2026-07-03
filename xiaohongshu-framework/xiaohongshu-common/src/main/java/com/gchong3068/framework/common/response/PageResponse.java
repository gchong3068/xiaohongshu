package com.gchong3068.framework.common.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 分页响应参数对象
 * @Author: gchong3068
 * @Date: 2024/9/25 21:20
 * @Version: v1.0.0
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class PageResponse<T> extends Response<List<T>> {

    private long pageNo;
    private long totalCount;
    private long pageSize;
    private long totalPage;

    public static <T> PageResponse<T> success(List<T> data,long pageNo,long totalCount){
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setSuccess(true);
        pageResponse.setData(data);
        pageResponse.setPageNo(pageNo);
        pageResponse.setTotalCount(totalCount);

        long pageSize = 10L;
        pageResponse.setPageSize(pageSize);

        long totalPage = (totalCount + pageSize - 1) / pageSize;
        pageResponse.setTotalPage(totalPage);
        return pageResponse;
    }

    public static <T> PageResponse<T> success(List<T> data, long pageNo, long totalCount, long pageSize) {
        PageResponse<T> pageResponse = new PageResponse<>();
        pageResponse.setSuccess(true);
        pageResponse.setData(data);
        pageResponse.setPageNo(pageNo);
        pageResponse.setTotalCount(totalCount);
        pageResponse.setPageSize(pageSize);

        // 计算总页数
        long totalPage = pageSize == 0 ? 0 : (totalCount + pageSize - 1) / pageSize;
        pageResponse.setTotalPage(totalPage);
        return pageResponse;
    }

    /**
     * 获取总页数
     * @return long
     */
    public static long getTotalPage(long totalCount, long pageSize) {
        return pageSize == 0 ? 0 : (totalCount + pageSize - 1) / pageSize;
    }

    /**
     * 计算分页查询 Offset
     * @Auther: gchong3068
     * @Date: 2024/9/26 22:50
 	 * @param pageNo 当前页码
 	 * @param pageSize 分页大小
 	 * @return long
     **/
    public static long getOffset(long pageNo, long pageSize){
        if (pageNo<1){
            pageNo =1;
        }

        return (pageNo -1) * pageSize;
    }
}
