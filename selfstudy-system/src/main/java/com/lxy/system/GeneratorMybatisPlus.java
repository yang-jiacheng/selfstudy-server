package com.lxy.system;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.lxy.system.engine.VelocityTemplateEngine;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

/**
 * 代码生成器
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2022/10/08 20:57
 */

@Slf4j
public class GeneratorMybatisPlus {

    /**
     * 生成路径
     */
    public static final String DIR = "D:\\java\\jcode\\selfstudy-server\\selfstudy-system";
    /**
     * 包名配置
     */
    public static final String PARENT_PACKAGE = "com.lxy.system";
    /**
     * 作者
     */
    public static final String AUTHOR = "jiacheng yang.";
    /**
     * 表名
     */
    public static final String[] TABS = {"sys_dict"};

    public static String DRIVER_NAME = "";
    public static String URL = "";
    public static String USERNAME = "";
    public static String PASSWORD = "";

    // 使用静态代码块读取
    static {
        Properties properties = new Properties();
        InputStream inputStream = GeneratorMybatisPlus.class.getResourceAsStream("/dataSource.properties");
        try {
            properties.load(inputStream);
            DRIVER_NAME = properties.getProperty("jdbc.driverClassName");
            URL = properties.getProperty("jdbc.url");
            USERNAME = properties.getProperty("jdbc.username");
            PASSWORD = properties.getProperty("jdbc.password");
        } catch (IOException e) {
            log.error("读取配置文件失败", e);
        }
    }

    // @TableField(updateStrategy = FieldStrategy.ALWAYS) 忽略空值判断，实体对象的字段是什么值就用什么值更新，支持null值更新操作

    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
            // 全局配置
            .globalConfig(builder -> {
                // 作者、输出路径、是否覆盖、日期类型、注释日期格式
                builder.author(AUTHOR).outputDir(DIR + "/src/main/java").disableOpenDir().dateType(DateType.ONLY_DATE)
                    .commentDate("yyyy-MM-dd");
            }).packageConfig(builder -> {
                builder.parent(PARENT_PACKAGE).entity("po").service("service").serviceImpl("service.impl")
                    .mapper("mapper")
                    .pathInfo(Collections.singletonMap(OutputFile.xml, DIR + "/src/main/resources/mybatis"));
            }).strategyConfig(builder -> {
                builder.addInclude(TABS)

                    .serviceBuilder().enableFileOverride().formatServiceFileName("%sService")
                    // .disable() // 禁用 Service 接口生成

                    .mapperBuilder().enableFileOverride().enableBaseResultMap().enableBaseColumnList()
                    .formatMapperFileName("%sMapper").formatXmlFileName("%sMapper")
                    .mapperTemplate("/templates/mapper.java.vm") // 设置 Mapper 模板
                    .mapperXmlTemplate("/templates/mapper.xml.vm")
                    // .disable() // 禁用 Mapper 接口生成

                    .entityBuilder().enableFileOverride().naming(NamingStrategy.underline_to_camel)
                    .columnNaming(NamingStrategy.underline_to_camel).javaTemplate("/templates/entity.java.vm")
                    .enableLombok()
                    // .disable() // 禁用实体类生成

                    .controllerBuilder().disable() // 禁用 Controller 层生成
                ;
            })

            .injectionConfig(builder -> {
                builder.beforeOutputFile((tableInfo, objectMap) -> {
                    System.out.println("生成表: " + tableInfo.getEntityName());
                });
            }).templateEngine(new VelocityTemplateEngine()).execute();
    }

}
