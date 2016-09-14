package com.tianxun.framework.mq;

import java.util.List;

public class MqDataVo<T> {

    private String entityName;
    
    private List<T> entityData;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<T> getEntityData() {
        return entityData;
    }

    public void setEntityData(List<T> entityData) {
        this.entityData = entityData;
    }
    
    
}
