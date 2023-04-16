<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
	<class name="${entity.tableName}" table="${entity.tableName}" optimistic-lock="version">
		<#if entity.columns?exists>
			<#list entity.columns as attr>
				<#if attr.dbFieldName == "id">
					<#if entity.jformPkType?if_exists?html=='UUID'>
						<id name="id" type="java.lang.String" length="${attr.dbLength}" unsaved-value="null">
							<generator class="uuid" />
						</id>
					<#elseif entity.jformPkType?if_exists?html=='NATIVE'>
						<#if dataType=='MYSQL' || dataType=='MARIADB'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="identity" />
							</id>
						<#elseif dataType=='ORACLE' || dataType=='DM' || dataType=='DB2'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="native" />
							</id>
						<#elseif dataType=='SQLSERVER'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="identity" />
							</id>
						<#elseif dataType=='POSTGRESQL'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="native" />
							</id>
						<#else>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="identity" />
							</id>
						</#if>
					<#elseif entity.jformPkType?if_exists?html=='SEQUENCE'>
						<#if dataType=='MYSQL' || dataType=='MARIADB'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="identity" />
							</id>
						<#elseif dataType=='ORACLE' || dataType=='DM' || dataType=='DB2'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="sequence">
									<param name="sequence">${entity.jformPkSequence}
									</param>
								</generator>
							</id>
						<#elseif dataType=='SQLSERVER'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="identity" />
							</id>
						<#elseif dataType=='POSTGRESQL'>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="native" />
							</id>
						<#else>
							<id name="id" type="java.lang.Long" length="${attr.dbLength}" unsaved-value="null">
								<generator class="identity" />
							</id>
						</#if>
					<#else>
						<id name="id" type="java.lang.String" length="${attr.dbLength}" unsaved-value="null">
							<generator class="uuid" />
						</id>
					</#if>

				<#else>
					<property name="${attr.dbFieldName}"
						<#switch attr.dbType?lower_case>
							<#case "string">
								type="java.lang.String"
							<#break>
							<#case "text">
							<#-- update--begin--author:scott Date:20180227 for:针对oracle情况下text类型采用clob转换 -->
								<#if dataType=='ORACLE' || dataType=='DM' || dataType=='HSQL'>
									type="clob"
								<#else>
									type="text"
								</#if>
							<#-- update--end--author:scott Date:20180227 for:针对oracle情况下text类型采用clob转换 -->
							<#break>
							<#case "int">
								type="java.lang.Integer"
							<#break>
							<#case "double">
								<#if dataType=='MYSQL'>
									type="java.lang.Double"
								<#elseif dataType=='ORACLE'>
									type="java.math.BigDecimal"
								<#elseif dataType=='POSTGRESQL'>
									type="java.math.BigDecimal"
								<#elseif dataType=='SQLSERVER'>
									type="java.math.BigDecimal"
								<#elseif dataType=='DM'>
									type="java.math.BigDecimal"
								<#else>
									type="java.lang.Double"
								</#if>
							<#break>
							<#case "date">
								<#if dataType=='MYSQL'>
									type="java.util.Date"
								<#elseif dataType=='ORACLE'>
									type="java.sql.Timestamp"
								<#elseif dataType=='POSTGRESQL'>
									type="java.util.Date"
								<#elseif dataType=='SQLSERVER'>
									type="java.util.Date"
								<#elseif dataType=='DM'>
									type="java.util.Date"
								<#else>
									type="java.util.Date"
								</#if>
							<#break>
							<#case "bigdecimal">
							  	<#if dataType=='MYSQL'>
									type="java.math.BigDecimal"
								<#elseif dataType=='ORACLE'>
									type="java.math.BigDecimal"
								<#elseif dataType=='POSTGRESQL'>
									type="java.math.BigDecimal"
								<#elseif dataType=='SQLSERVER'>
									type="java.math.BigDecimal"
								<#elseif dataType=='DM'>
									type="java.math.BigDecimal"
								<#else>
									type="java.math.BigDecimal"
								</#if>
							<#break>
							<#case "blob">
								<#if dataType=='MYSQL'>
									type="blob"
								<#elseif dataType=='ORACLE'>
							 		type="blob"
								<#elseif dataType=='POSTGRESQL'>
									type="binary"
								<#elseif dataType=='SQLSERVER'>
									type="image"
								<#elseif dataType=='DM'>
									type="blob"
								<#else>
									type="blob"
								</#if>
							<#break>
						</#switch> access="property">
						<column name="${attr.dbFieldName}" <#if dataType=='SQLSERVER' && attr.dbType?lower_case="string"> sql-type="nvarchar(${attr.dbLength})" </#if>
							                               <#if dataType=='SQLSERVER' && attr.dbType?lower_case="text"> sql-type="ntext" </#if>
							<#if attr.dbType=='double'||attr.dbType=='BigDecimal'>
							precision="${attr.dbLength}" scale="${attr.dbPointLength}"
							<#else>
							<#-- update-begin-author:taoyan -->
							<#if db=='hsql' && (attr.dbType?lower_case=='blob'||attr.dbType?lower_case=='text')>
							<#-- 1G -->
							length="1073741824"
							<#elseif db=='db2' && attr.dbType?lower_case=='blob'>
							<#-- 1M -->
							length="1048576"
							<#else>
							length="${attr.dbLength}"
							</#if>
							</#if>
							<#if attr.dbDefaultVal?exists&&attr.dbDefaultVal!=''>default="${attr.dbDefaultVal}"</#if>
							not-null="<#if attr.dbIsNull == 1>false<#else>true</#if>" unique="false">
							<comment>${attr.dbFieldTxt}</comment>
						</column>
					</property>
				</#if>
			</#list>
		</#if>
	</class>






</hibernate-mapping>