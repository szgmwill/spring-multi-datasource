package com.tianxun.framework.entity.vo;

import com.tianxun.framework.common.ResponseCode;

public class BasePageResponse<T> extends BaseResponse{

    private Page<T> page;
    
    public BasePageResponse(ResponseCode responseCode) {
        super(responseCode);
        page = new Page<T>();
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
    
}
