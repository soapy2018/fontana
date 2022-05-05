package com.fontana.JeeOnline.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fontana.JeeOnline.entity.OnlCgformHead;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @interfaceName: OnlCgformHeadMapper
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/1/11 14:16
 */
public interface OnlCgformHeadMapper extends BaseMapper<OnlCgformHead> {
    @InterceptorIgnore(
            tenantLine = "true"
    )
    void executeDDL(@Param("sqlStr") String sqlStr);

    List<Map<String, Object>> queryList(@Param("sqlStr") String sqlStr);

    List<String> queryOnlinetables();

    Map<String, Object> queryOneByTableNameAndId(@Param("tbname") String var1, @Param("dataId") String var2);

    void deleteOne(@Param("sqlStr") String var1);

    @Select({"select max(copy_version) from onl_cgform_head where physic_id = #{physicId}"})
    Integer getMaxCopyVersion(@Param("physicId") String var1);

    @Select({"select table_name from onl_cgform_head where physic_id = #{physicId}"})
    List<String> queryAllCopyTableName(@Param("physicId") String var1);

    @Select({"select physic_id from onl_cgform_head GROUP BY physic_id"})
    List<String> queryCopyPhysicId();

    String queryCategoryIdByCode(@Param("code") String var1);

    @Select({"select count(*) from ${tableName} where ${pidField} = #{pidValue}"})
    Integer queryChildNode(@Param("tableName") String var1, @Param("pidField") String var2, @Param("pidValue") String var3);


}
