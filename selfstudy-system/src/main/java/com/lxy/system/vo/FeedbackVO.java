package com.lxy.system.vo;

import com.lxy.system.po.Feedback;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/21 17:37
 */
public class FeedbackVO extends Feedback {
    private static final long serialVersionUID = -6416486320539350938L;

    private String name;

    private String phone;

    private String profilePath;

    private String adminName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
