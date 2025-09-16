package com.lxy.system.vo.user;

import java.io.Serializable;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/24 9:19
 */
public class UserRankVO implements Serializable {

    private static final long serialVersionUID = -1366781559630442509L;

    private Integer id;

    private String name;

    private String profilePath;

    private Integer totalDuration;

    private Integer todayDuration;

    private Integer ranking;

    public UserRankVO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public Integer getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Integer totalDuration) {
        this.totalDuration = totalDuration;
    }

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getTodayDuration() {
        return todayDuration;
    }

    public void setTodayDuration(Integer todayDuration) {
        this.todayDuration = todayDuration;
    }

    @Override
    public String toString() {
        return "UserRankVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", profilePath='" + profilePath + '\'' +
                ", totalDuration=" + totalDuration +
                ", todayDuration=" + todayDuration +
                ", ranking=" + ranking +
                '}';
    }
}
