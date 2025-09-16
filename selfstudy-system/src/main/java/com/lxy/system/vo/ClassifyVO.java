package com.lxy.system.vo;

import com.lxy.system.po.Classify;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/19 17:59
 */
public class ClassifyVO extends Classify {

    private static final long serialVersionUID = 807766301683388704L;

    //学习人数
    private Integer studyCount;

    public Integer getStudyCount() {
        return studyCount;
    }

    public void setStudyCount(Integer studyCount) {
        this.studyCount = studyCount;
    }
}
