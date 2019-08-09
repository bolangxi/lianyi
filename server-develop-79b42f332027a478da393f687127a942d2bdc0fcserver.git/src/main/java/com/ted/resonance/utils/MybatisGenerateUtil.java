package com.ted.resonance.utils;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

/**
 * 代码生成器!
 * 需要在maven中引入如下jar包即可用
 <dependency>
 <groupId>org.mybatis.generator</groupId>
 <artifactId>mybatis-generator-core</artifactId>
 <version>1.3.2</version>
 </dependency>
 *
 * 使用方法：
 *     1. 填写好表名
 *     2. 如果是模块划分再填写模块名
 *     3. 直接运行main搞定
 *
 * @author liujian
 * @version 2016/6/7 0007
 */
public class MybatisGenerateUtil {
    /**
     * 开发人员设置
     */
    // 需要生成的代码的表,可以多个，用英文逗号“,”分割，例如：incubator_industry,incubator_magazine_citys
    private static String tableName = "table_name";
    // 如果是模块划分，则需要加上模块名字
    private static String module = "";

    public static void main(String[] args) {
        MybatisGenerateUtil.doGenerateCode();
    }

    public static void doGenerateCode() {
        try {
            Configuration config = new Configuration();
            Context context = new Context(null);
            context.setId("MysqlTables");
            context.setTargetRuntime("MyBatis3");
            // 设置公共的配置
            doCommentGenerator(context);
            // 设置数据库
            doJdbcConnection(context);
            // 设置Java类型解析器
            doJavaTypeResolver(context);
            // 设置Java模型
            doJavaModelGenerator(context);
            // 设置Map文件
            doSqlMapGenerator(context);
            // 设置dao
            doJavaClientGenerator(context);
            // 设置table
            doTable(context);
            config.addContext(context);

            // overwrite
            DefaultShellCallback shellCallback = new DefaultShellCallback(true);

            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, shellCallback, new ArrayList<String>());

            myBatisGenerator.generate(null, new HashSet(), new HashSet());

            System.out.println("表："+tableName+",自动生成代码成功~!");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    private static void doJavaTypeResolver(Context context) {
        JavaTypeResolverConfiguration resolver = new JavaTypeResolverConfiguration();
        resolver.getProperties().setProperty("forceBigDecimals", "false");
        context.setJavaTypeResolverConfiguration(resolver);
    }

    private static void doCommentGenerator(Context context) {
        CommentGeneratorConfiguration common = new CommentGeneratorConfiguration();
        Properties properties = common.getProperties();
        properties.setProperty("suppressDate", "true");
        // 是否去除自动生成的注释 true：是 ： false:否
        properties.setProperty("suppressAllComments", "false");
        context.setCommentGeneratorConfiguration(common);
    }

    private static void doTable(Context context) {
        List<TableConfiguration> list = context.getTableConfigurations();
        list.clear();
        if (tableName != null && !"".equals(tableName)) {
            String[] tableArr = tableName.split(",");
            for (String tableName : tableArr) {
                TableConfiguration tableConfiguration = new TableConfiguration(context);
                tableConfiguration.setTableName(tableName);
                String bean = tableToBean(tableName);
                tableConfiguration.setDomainObjectName(bean);
                tableConfiguration.setCountByExampleStatementEnabled(false);
                tableConfiguration.setUpdateByExampleStatementEnabled(false);
                tableConfiguration.setDeleteByExampleStatementEnabled(false);
                tableConfiguration.setSelectByExampleStatementEnabled(false);
                tableConfiguration.setSelectByExampleQueryId("false");
                list.add(tableConfiguration);
            }
        }
    }

    private static String tableToBean(String tableName) {
        int flag = tableName.indexOf("_");
        if (flag > 0) {
            tableName = tableName.toLowerCase();
            String[] tableStr = tableName.split("_");
            String result = "";
            for (String tableTemp : tableStr) {
                result += tableTemp.substring(0, 1).toUpperCase() + tableTemp.substring(1);
            }
            return result + "Bean";
        } else {
            return tableName.substring(0, 1).toUpperCase() + tableName.substring(1) + "Bean";
        }
    }

    private static void doJavaClientGenerator(Context context) {
        JavaClientGeneratorConfiguration dao = new JavaClientGeneratorConfiguration();
        if(module != null && !"".equals(module)) {
            dao.setTargetPackage(iDaoTargetPackage+"."+module);
        }else{
            dao.setTargetPackage(iDaoTargetPackage);
        }
        dao.setConfigurationType("XMLMAPPER");
        if(childProjectName!=null && !"".equals(childProjectName)) {
            dao.setTargetProject(childProjectName+"/src/main/java");
        }else{
            dao.setTargetProject("src/main/java");
        }
        dao.getProperties().setProperty("enableSubPackages", "true");
        context.setJavaClientGeneratorConfiguration(dao);
    }

    private static void doSqlMapGenerator(Context context) {
        SqlMapGeneratorConfiguration sql = new SqlMapGeneratorConfiguration();
        sql.setTargetPackage(mapTargetPackage);
        if(childProjectName!=null && !"".equals(childProjectName)) {
            sql.setTargetProject(childProjectName+"/src/main/resources");
        }else{
            sql.setTargetProject("src/main/resources");
        }
        sql.getProperties().setProperty("enableSubPackages", "true");
        context.setSqlMapGeneratorConfiguration(sql);
    }

    private static void doJavaModelGenerator(Context context) {
        JavaModelGeneratorConfiguration java = new JavaModelGeneratorConfiguration();
        if(module!=null && !"".equals(module)) {
            java.setTargetPackage(modelTargetPackage+"."+module);
        }else{
            java.setTargetPackage(modelTargetPackage);
        }
        if(childProjectName!=null && !"".equals(childProjectName)) {
            java.setTargetProject(childProjectName+"/src/main/java");
        }else{
            java.setTargetProject("src/main/java");
        }
        java.getProperties().setProperty("enableSubPackages", "true");
        java.getProperties().setProperty("trimStrings", "true");
        context.setJavaModelGeneratorConfiguration(java);
    }

    private static void doJdbcConnection(Context context) {
        JDBCConnectionConfiguration jdbc = new JDBCConnectionConfiguration();
        if (connectionURL != null && !"".equals(connectionURL)) {
            jdbc.setConnectionURL(connectionURL);
        }
        if (driverClass != null && !"".equals(driverClass)) {
            jdbc.setDriverClass(driverClass);
        }
        if (password != null && !"".equals(password)) {
            jdbc.setPassword(password);
        }
        if (userId != null && !"".equals(userId)) {
            jdbc.setUserId(userId);
        }
        context.setJdbcConnectionConfiguration(jdbc);
    }

    /**
     * 配置管理员设置
     */
    // 默认mysql，如果是mysql可以不用设置这个变量
    private static String driverClass = "com.mysql.jdbc.Driver";
    // 数据库访问地址
    private static String connectionURL = "jdbc:mysql://172.17.3.225:3306/ted";
    // 数据库用户名
    private static String userId = "developer";
    // 数据库密码
    private static String password = "tNzDRFkPCAX0o";
    // Java模型生成的路径，例如：com.easyrong.domain
    private static String modelTargetPackage = "com.ted.resonance.entity";
    // map文件生成的路径。默认在resources，如果配置mybatis，标识生成到resources/mybatis下
    private static String mapTargetPackage = "mapper.ted";
    // dao文件生成的路径，例如：com.easyrong.dao
    private static String iDaoTargetPackage = "com.ted.resonance.mapper";
    // 如果是maven的父子关系项目，则需要添加你用的子项目名称
    private static String childProjectName = "";
}
