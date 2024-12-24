package com.lxy.common.enums;

/**
 * @Description: TODO
 * @author: jiacheng yang.
 * @Date: 2022/11/30 17:58
 * @Version: 1.0
 */
public enum QuestionType {

    SINGLE_CHOICE(1,"单选题",1),
    MULTIPLE_CHOICE(2,"多选题",2),
    TRUE_OR_FALSE(3,"判断题",1),
    SUBJECTIVE_ESSAY(4,"主观问答题",3),
    HEARING_SINGLE_CHOICE(6,"听力题",1),
    READING_COMPREHENSION(7,"材料分析题",2),
    CLOZE(8,"完形填空",1),
    FILL_BLANK(9,"填空题",4);

    public final Integer type;
    public final String name;
    public final Integer judge;

    QuestionType(Integer type,String name,Integer judge){
        this.type = type;
        this.name = name;
        this.judge = judge;
    }

}
