package com.fontana.codegenerate.support;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.PostgreSqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.SqlServerTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.fontana.codegenerate.constant.CodeGenConstant;
import com.fontana.util.lang.StringUtil;
import com.fontana.util.tools.SupUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * 代码生成器配置类
 *
 * @author cqf
 */
@Data
@Slf4j
public class MyCodeGenerator {
	/**
	 * 代码所在系统
	 */
	private String systemName;
	/**
	 * 代码模块名称
	 */
	private String codeName;
	/**
	 * 代码所在服务名
	 */
	private String serviceName;
	/**
	 * 代码生成的包名
	 */
	private String packageName;
	/**
	 * 输出目录
	 */
	private String outputDir;
	/**
	 * 作者
	 */
	private String author;
	/**
	 * 需要去掉的表前缀
	 */
	private String[] tablePrefix = {"auto_"};
	/**
	 * 需要生成的表名(两者只能取其一)
	 */
	private String[] includeTables;
	/**
	 * 需要排除的表名(两者只能取其一)
	 */
	private String[] excludeTables = {};
	/**
	 * 是否包含基础业务字段
	 */
	private Boolean hasSuperEntity = Boolean.TRUE;
	/**
	 * 基础业务字段
	 */
	private String[] superEntityColumns = {""};//{"create_user_id", "create_time", "update_user_id", "update_time", "deleted_flag"};
	/**
	 * 是否启用swagger
	 */
	private Boolean isSwagger2 = Boolean.TRUE;
	/**
	 * 数据库驱动名
	 */
	private String driverName;
	/**
	 * 数据库链接地址
	 */
	private String url;
	/**
	 * 数据库用户名
	 */
	private String username;
	/**
	 * 数据库密码
	 */
	private String password;

	public MyCodeGenerator(){
		Properties props = getProperties();
		packageName = props.getProperty("packageName");
		author = StringUtil.defaultIfEmpty(props.getProperty("author"), "AI");
		includeTables = StringUtil.split(props.getProperty("includeTables"),',');
		serviceName = props.getProperty("serviceName");
		outputDir = props.getProperty("outputDir");
		systemName = StringUtil.defaultIfEmpty(props.getProperty("systemName"), CodeGenConstant.REACT_NAME);
	}

	public void run() {
		Properties props = getProperties();
		AutoGenerator mpg = new AutoGenerator();
		GlobalConfig gc = new GlobalConfig();
		//生成目录
		String outputDir = getOutputDir();
		gc.setOutputDir(outputDir);
		gc.setAuthor(author);
		gc.setFileOverride(true);
		gc.setOpen(false);
		gc.setActiveRecord(false);
		gc.setEnableCache(false);
		gc.setBaseResultMap(true);
		gc.setBaseColumnList(true);
		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		gc.setServiceName("%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setControllerName("%sController");
		gc.setSwagger2(isSwagger2);
		mpg.setGlobalConfig(gc);
		DataSourceConfig dsc = new DataSourceConfig();
		String driverName = SupUtil.toStr(this.driverName, props.getProperty("spring.datasource.driver-class-name"));
		if (StringUtil.containsAny(driverName, DbType.MYSQL.getDb())) {
			dsc.setDbType(DbType.MYSQL);
			dsc.setTypeConvert(new MySqlTypeConvert());
		} else if (StringUtil.containsAny(driverName, DbType.POSTGRE_SQL.getDb())) {
			dsc.setDbType(DbType.POSTGRE_SQL);
			dsc.setTypeConvert(new PostgreSqlTypeConvert());
		} else if (StringUtil.containsAny(driverName, DbType.SQL_SERVER.getDb())) {
			dsc.setDbType(DbType.SQL_SERVER);
			dsc.setTypeConvert(new SqlServerTypeConvert());
		} else {
			dsc.setDbType(DbType.ORACLE);
			dsc.setTypeConvert(new OracleTypeConvert());
		}
		dsc.setDriverName(driverName);
		dsc.setUrl(SupUtil.toStr(this.url, props.getProperty("spring.datasource.url")));
		dsc.setUsername(SupUtil.toStr(this.username, props.getProperty("spring.datasource.username")));
		dsc.setPassword(SupUtil.toStr(this.password, props.getProperty("spring.datasource.password")));
		mpg.setDataSource(dsc);
		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		// strategy.setCapitalMode(true);// 全局大写命名
		// strategy.setDbColumnUnderline(true);//全局下划线命名
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setTablePrefix(tablePrefix);
		if (includeTables.length > 0) {
			strategy.setInclude(includeTables);
		}
		if (excludeTables.length > 0) {
			strategy.setExclude(excludeTables);
		}
		if (hasSuperEntity) {
			strategy.setSuperEntityClass("com.fontana.db.model.BaseLogicDelModel");
			//strategy.setSuperEntityColumns(superEntityColumns);
			strategy.setSuperMapperClass("com.fontana.db.mapper.BaseDaoMapper");
			strategy.setSuperServiceClass("com.fontana.db.service.IBaseService");
			strategy.setSuperServiceImplClass("com.fontana.db.service.impl.AbstractBaseService");
		} else {
			strategy.setSuperServiceClass("com.baomidou.mybatisplus.extension.service.IService");
			strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.extension.service.impl.ServiceImpl");
		}
		// 自定义 controller 父类
		strategy.setSuperControllerClass("com.fontana.db.controller.AbstractBaseController");
		//strategy.setEntityBuilderModel(false);
		strategy.setEntityLombokModel(true);
		strategy.setControllerMappingHyphenStyle(true);
		mpg.setStrategy(strategy);
		// 包配置
		PackageConfig pc = new PackageConfig();
		// 控制台扫描
		pc.setModuleName(null);
		pc.setParent(packageName);
		pc.setController("controller");
		pc.setEntity("entity");
		pc.setXml("mapper/xml");
		mpg.setPackageInfo(pc);
		mpg.setCfg(getInjectionConfig());
		mpg.execute();
	}

	private InjectionConfig getInjectionConfig() {
		String servicePackage = serviceName.split("-").length > 1 ? serviceName.split("-")[1] : serviceName;
		// 自定义配置
		Map<String, Object> map = new HashMap<>(16);
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				map.put("codeName", codeName);
				map.put("serviceName", serviceName);
				map.put("servicePackage", servicePackage);
				map.put("servicePackageLowerCase", servicePackage.toLowerCase());
				this.setMap(map);
			}
		};
		List<FileOutConfig> focList = new ArrayList<>();
		focList.add(new FileOutConfig("/templates/sql/menu.sql.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				map.put("entityKey", (tableInfo.getEntityName().toLowerCase()));
				map.put("menuId", IdWorker.getId());
				map.put("addMenuId", IdWorker.getId());
				map.put("editMenuId", IdWorker.getId());
				map.put("removeMenuId", IdWorker.getId());
				map.put("viewMenuId", IdWorker.getId());
				return getOutputDir() + StringPool.SLASH + "/sql/" + tableInfo.getEntityName().toLowerCase() + ".menu.mysql";
			}
		});
		focList.add(new FileOutConfig("/templates/entityVO.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDir() + StringPool.SLASH + packageName.replace("service", "api").replace(".", StringPool.SLASH) + StringPool.SLASH + "vo" + StringPool.SLASH + tableInfo.getEntityName() + "VO" + StringPool.DOT_JAVA;
			}
		});
		focList.add(new FileOutConfig("/templates/entityDTO.java.vm") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				return getOutputDir() + StringPool.SLASH + packageName.replace("service", "api").replace(".", StringPool.SLASH) + StringPool.SLASH + "dto" + StringPool.SLASH + tableInfo.getEntityName() + "DTO" + StringPool.DOT_JAVA;
			}
		});

		//***前端代码
		if (SupUtil.equals(systemName, CodeGenConstant.REACT_NAME)) {
			focList.add(new FileOutConfig("/templates/react/action.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/actions" + StringPool.SLASH + tableInfo.getEntityName().toLowerCase() + ".js";
				}
			});
			focList.add(new FileOutConfig("/templates/react/model.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/models" + StringPool.SLASH + tableInfo.getEntityName().toLowerCase() + ".js";
				}
			});
			focList.add(new FileOutConfig("/templates/react/service.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/services" + StringPool.SLASH + tableInfo.getEntityName().toLowerCase() + ".js";
				}
			});
			focList.add(new FileOutConfig("/templates/react/list.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/pages" + StringPool.SLASH + StringUtil.upperFirst(servicePackage) + StringPool.SLASH + tableInfo.getEntityName() + StringPool.SLASH + tableInfo.getEntityName() + ".js";
				}
			});
			focList.add(new FileOutConfig("/templates/react/add.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/pages" + StringPool.SLASH + StringUtil.upperFirst(servicePackage) + StringPool.SLASH + tableInfo.getEntityName() + StringPool.SLASH + tableInfo.getEntityName() + "Add.js";
				}
			});
			focList.add(new FileOutConfig("/templates/react/edit.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/pages" + StringPool.SLASH + StringUtil.upperFirst(servicePackage) + StringPool.SLASH + tableInfo.getEntityName() + StringPool.SLASH + tableInfo.getEntityName() + "Edit.js";
				}
			});
			focList.add(new FileOutConfig("/templates/react/view.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/pages" + StringPool.SLASH + StringUtil.upperFirst(servicePackage) + StringPool.SLASH + tableInfo.getEntityName() + StringPool.SLASH + tableInfo.getEntityName() + "View.js";
				}
			});
		} else if (SupUtil.equals(systemName, CodeGenConstant.VUE_NAME)) {
			focList.add(new FileOutConfig("/templates/vue/api.js.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/api" + StringPool.SLASH + servicePackage.toLowerCase() + StringPool.SLASH + tableInfo.getEntityName().toLowerCase() + ".js";
				}
			});
			focList.add(new FileOutConfig("/templates/vue/crud.vue.vm") {
				@Override
				public String outputFile(TableInfo tableInfo) {
					return getOutputWebDir() + "/views" + StringPool.SLASH + servicePackage.toLowerCase() + StringPool.SLASH + tableInfo.getEntityName().toLowerCase() + ".vue";
				}
			});
		}

		cfg.setFileOutConfigList(focList);
		return cfg;
	}

	/**
	 * 获取配置文件
	 *
	 * @return 配置Props
	 */
	private Properties getProperties() {
		// 读取配置文件
		Resource resource = new ClassPathResource("templates/code.properties");
		Properties props = new Properties();
		try {
			props = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}

	/**
	 * 生成到项目中
	 *
	 * @return outputDir
	 */
	public String getOutputDir() {
		String outputBackDir = (SupUtil.isBlank(outputDir) ? System.getProperty("user.dir") + "/output/back" : outputDir) + StringPool.SLASH + serviceName + "/back/src/main/java";
		System.out.println("outputBackDir: " + outputBackDir);
		return outputBackDir;
	}

	/**
	 * 生成到Web项目中
	 *
	 * @return outputDir
	 */
	public String getOutputWebDir() {
		String outputWebDir = (SupUtil.isBlank(outputDir) ? System.getProperty("user.dir") + "/output/front" : outputDir) + StringPool.SLASH + serviceName + "/front/src";
		System.out.println("outPutWebDir: " + outputWebDir);
		return outputWebDir;
	}

	/**
	 * 页面生成的文件名
	 */
	private String getGeneratorViewPath(String viewOutputDir, TableInfo tableInfo, String suffixPath) {
		String name = StringUtils.firstToLowerCase(tableInfo.getEntityName());
		String path = viewOutputDir + StringPool.SLASH + name + StringPool.SLASH + name + suffixPath;
		File viewDir = new File(path).getParentFile();
		if (!viewDir.exists()) {
			viewDir.mkdirs();
		}
		return path;
	}

}
