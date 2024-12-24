package com.lxy.common;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Description: 代码生成器，必须先编译出target目录才能运行
 * @author: jiacheng yang.
 * @Date: 2022/10/08 20:57
 * @Version: 1.0
 */

public class GeneratorMybatisPlus {
	/**
	 * 模块名称
	 */
	public static final String MODULE_NAME="selfstudy-common";

	/**
	 * 包名配置
	 */
	public static final String PARENT_PACKAGE="com.lxy.common";

	public static final String AUTHOR="jiacheng yang.";

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
			e.printStackTrace();
			System.err.println("读取配置文件失败");
		}
	}



	public static void main(String[] args) {
		//实例化生成器对象
		AutoGenerator mpg = new AutoGenerator();

		//数据源 dataSourceConfig 配置：数据库类型...
		DataSourceConfig dataSourceConfig = new DataSourceConfig();
		dataSourceConfig.setDbType(DbType.MYSQL)
				.setUrl(URL).setUsername(USERNAME).setPassword(PASSWORD).setDriverName(DRIVER_NAME);
		mpg.setDataSource(dataSourceConfig);

		//数据库表配置：是否大写命名、【实体】是否为 lombok 模型、需要包含的表名、数据库表字段映射到实体的命名策略, 未指定按照 naming 执行、数据库表映射到实体的命名策略
		/* -------------------------- 表名写在这里 --------------------------- */
		String[] tabs = {"object_storage"};
		StrategyConfig strategyConfig = new StrategyConfig();
		strategyConfig.setCapitalMode(true)
				.setEntityLombokModel(false)
				.setInclude(tabs)
				.setColumnNaming(NamingStrategy.underline_to_camel)
				.setNaming(NamingStrategy.underline_to_camel);
		mpg.setStrategy(strategyConfig);

		//包名配置：
		PackageConfig packageConfig = new PackageConfig();
		packageConfig.setParent(PARENT_PACKAGE).setController("controller").setService("service").setServiceImpl("service.impl").setEntity("po").setMapper("mapper");
		mpg.setPackageInfo(packageConfig);

		// 模板设置
		TemplateConfig templateConfig = new TemplateConfig();
		//关闭controller层生成
		templateConfig.setController(null);
		// 关闭service层生成
		//templateConfig.setService(null);
		// 关闭ServiceImpl层生成
		//templateConfig.setServiceImpl(null);
		// 设置Mapper层模板
		templateConfig.setMapper("/templates/mapper.java.vm");
		//关闭默认Mybatis的Mapper.xml生成 会在Mapper层直接生成xml所以关闭
		templateConfig.setXml(null);
		// 设置po层模板
		templateConfig.setEntity("/templates/entity.vm");
		mpg.setTemplate(templateConfig);

		//全局策略配置：生成文件的输出目录、自定义service接口的生成名称、是否覆盖已有文件、是否打开输出目录、是否在 xml 中添加二级缓存配置、开发人员、是否开启 swagger2 模式、
		//是否开启 BaseResultMap、是否开启 baseColumnList、主键自动递增、指定时间类型
		GlobalConfig globalConfig = new GlobalConfig();
		/* -------------------------- 生成文件输出路径 --------------------------- */
		String dir = generatorPath();
		globalConfig.setOutputDir(dir+"/src/main/java/")
				.setServiceName("%sService")
				.setFileOverride(true)
				.setOpen(false)
				.setEnableCache(false)
				.setAuthor(AUTHOR)
				.setSwagger2(true)
				.setBaseResultMap(true)
				.setBaseColumnList(true)
				.setIdType(IdType.AUTO)
				.setDateType(DateType.ONLY_DATE);
		mpg.setGlobalConfig(globalConfig);

		// 注入自定义配置
		InjectionConfig injectionConfig = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<String, Object>();
				this.setMap(map);
			}
		};
		// 调整 mybatis Mapper.xml 生成目录
		List<FileOutConfig> fileOutConfigList = new ArrayList<FileOutConfig>();
		fileOutConfigList.add(new FileOutConfig("/templates/mapper.xml.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				//调整后的路径
				return dir + "/src/main/resources/mybatis/" + tableInfo.getEntityName() + "Mapper"+ StringPool.DOT_XML;
			}
		});
		injectionConfig.setFileOutConfigList(fileOutConfigList);
		mpg.setCfg(injectionConfig);
		//开始生成
		mpg.execute();

	}

	private static String generatorPath(){
		String dir = System.getProperty("user.dir");
		if (StrUtil.isNotEmpty(MODULE_NAME)){
			dir = Paths.get(dir, MODULE_NAME).toString();
		}
		return dir;
	}

}
