package com.lxy.common.enums;

/**
 * TODO
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/12/20 10:49
 */

public enum LogUserType {

    ADMIN(0,"后台用户"),
    USER(1,"APP用户");

    public final Integer type;
    public final String name;

    LogUserType(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public static String getName(Integer type){
        for(LogUserType logUserType : LogUserType.values()){
            if(logUserType.type.equals(type)){
                return logUserType.name;
            }
        }
        return "-";
    }

}
