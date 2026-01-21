package com.lxy.system.vo.user;

import com.lxy.common.annotation.ExcelHeader;
import com.lxy.common.vo.ExcelImportVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户导入VO
 * 继承User实体，添加Excel导入相关字段和注解
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2023/02/20 17:50
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserImportVO extends ExcelImportVO {

    @Serial
    private static final long serialVersionUID = -6648347675413807036L;

    @ExcelHeader(title = "手机号（唯一性，必填）")
    private String phone;

    @ExcelHeader(title = "密码（不填默认是123456）")
    private String password;

    @ExcelHeader(title = "昵称（必填）")
    private String name;

    @ExcelHeader(title = "性别")
    private String gender;

    @ExcelHeader(title = "地址")
    private String address;

    //头像地址
    private String profilePath;

    //个人资料背景图
    private String coverPath;

    //注册类型 1。用户注册 2.后台添加
    private Integer registType;

    //余额
    private BigDecimal balance;

    //总学习时长
    private Integer totalDuration;

    //注册时间
    private Date createTime;

    //修改时间
    private Date updateTime;

}
