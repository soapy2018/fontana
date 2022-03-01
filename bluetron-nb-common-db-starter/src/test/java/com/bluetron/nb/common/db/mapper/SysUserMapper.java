package com.bluetron.nb.common.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bluetron.nb.common.db.model.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);


//    /**
//     * 查询全部用户
//     * @return
//     */
//    List<SysUser> selectAll();
}