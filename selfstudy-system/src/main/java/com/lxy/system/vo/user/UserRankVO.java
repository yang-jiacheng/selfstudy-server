package com.lxy.system.vo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户排行 VO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/24 9:19
 */

@NoArgsConstructor
@Data
public class UserRankVO implements Serializable {

    private static final long serialVersionUID = -1366781559630442509L;

    private Long id;

    private String name;

    private String profilePath;

    private Integer totalDuration;

    private Integer todayDuration;

    private Integer ranking;

}
