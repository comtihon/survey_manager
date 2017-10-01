package com.surveyor.manager.data.dto;

import com.surveyor.manager.data.entity.CommonEntity;

public interface CommonDTO {
    String getId();

    void setId(String id);

    Class<CommonEntity> getEntityClass();
}
