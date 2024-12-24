package com.lxy.common.vo;

import com.lxy.common.po.ObjectStorage;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2024/12/14 15:47
 * @Version: 1.0
 */

public class ObjectStorageVO extends ObjectStorage {
    private static final long serialVersionUID = 4177033914831871822L;

    private String creatorName;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}
