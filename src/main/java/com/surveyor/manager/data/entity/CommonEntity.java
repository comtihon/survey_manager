package com.surveyor.manager.data.entity;

public interface CommonEntity {
    String getId();

    void pullLazy();

    /**
     * Fill many_to_one connections
     */
    void setConnection(CommonEntity entity);
}
