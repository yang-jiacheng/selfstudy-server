package com.lxy.system;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.lxy.system.engine.VelocityTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Description: 代码生成器
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:57
 * @Version: 1.0
 */

public class GeneratorMybatisPlus {

	private static final Logger logger = LoggerFactory.getLogger(GeneratorMybatisPlus.class);

	/**
	 * 模块名称
	 */
	public static final String MODULE_NAME="selfstudy-system";

	/**
	 * 包名配置
	 */
	public static final String PARENT_PACKAGE="com.lxy.system";

	public static final String AUTHOR="jiacheng yang.";

	public static final String[] TABS = {
			"version"
	};

	public static String DRIVER_NAME="";
	public static String URL="";
	public static String USERNAME="";
	public static String PASSWORD="";
	//使用静态代码块读取
	static {
		Properties properties = new Properties();
		InputStream inputStream = GeneratorMybatisPlus.class.getResourceAsStream("/dataSource.properties");
		try {
			properties.load(inputStream);
			DRIVER_NAME=properties.getProperty("jdbc.driverClassName");
			URL=properties.getProperty("jdbc.url");
			USERNAME=properties.getProperty("jdbc.username");
			PASSWORD=properties.getProperty("jdbc.password");
		} catch (IOException e) {
			logger.error("读取配置文件失败", e);
		}
	}

	//@TableField(updateStrategy = FieldStrategy.ALWAYS) 忽略空值判断，实体对象的字段是什么值就用什么值更新，支持null值更新操作

	public static void main(String[] args) {
		String dir = generatorPath();
		FastAutoGenerator.create(URL, USERNAME, PASSWORD)
				// 全局配置
				.globalConfig(builder -> {
					//作者、输出路径、是否覆盖、日期类型、注释日期格式
					builder.author(AUTHOR)
							.outputDir(dir + "/src/main/java")
							.disableOpenDir()
							.dateType(DateType.ONLY_DATE)
							.commentDate("yyyy-MM-dd");
				})
				.packageConfig(builder -> {
					builder.parent(PARENT_PACKAGE)
							.entity("po")
							.service("service")
							.serviceImpl("service.impl")
							.mapper("mapper")
							.pathInfo(Collections.singletonMap(OutputFile.xml, dir + "/src/main/resources/mybatis"));
				})
				.strategyConfig(builder -> {
					builder.addInclude(TABS)

							.serviceBuilder()
							.enableFileOverride()
							.formatServiceFileName("%sService")
//							.disable() // 禁用 Service 接口生成

							.mapperBuilder()
							.enableFileOverride()
							.enableBaseResultMap()
							.enableBaseColumnList()
							.formatMapperFileName("%sMapper")
							.formatXmlFileName("%sMapper")
							.mapperTemplate("/templates/mapper.java.vm") // 设置 Mapper 模板
							.mapperXmlTemplate("/templates/mapper.xml.vm")
//							.disable() // 禁用 Mapper 接口生成

							.entityBuilder()
							.enableFileOverride()
							.naming(NamingStrategy.underline_to_camel)
							.columnNaming(NamingStrategy.underline_to_camel)
							.javaTemplate("/templates/entity.java.vm")
							.enableLombok()
//							.disable() // 禁用实体类生成

							.controllerBuilder()
							.disable() // 禁用 Controller 层生成
					;


				})

				.injectionConfig(builder -> {
					builder.beforeOutputFile((tableInfo, objectMap) -> {
						System.out.println("生成表: " + tableInfo.getEntityName());
					});
				})
				.templateEngine(new VelocityTemplateEngine())
				.execute();
	}

	private static String generatorPath(){
		String dir = System.getProperty("user.dir");
		if (StrUtil.isNotEmpty(MODULE_NAME)){
			dir = Paths.get(dir, MODULE_NAME).toString();
		}
		return dir;
	}

}
