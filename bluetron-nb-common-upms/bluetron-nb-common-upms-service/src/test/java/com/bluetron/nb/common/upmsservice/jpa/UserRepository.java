package com.bluetron.nb.common.upmsservice.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @className: UserRepository
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/2/22 10:02
 */
public interface UserRepository extends JpaRepository<JpaUser, Long> {

    List<JpaUser> findByLoginName(String name);

    JpaUser findByLoginNameAndDeptId(String name, Long deptId);

    //使用@Query注解查询
    @Query(value="from JpaUser where loginName = ?1")
    List<JpaUser> queryUserByNameUseJPQL(String name);

    //使用@Query注解查询
    @Query(value="select * from bn_sys_user where login_name = ?1", nativeQuery = true)
    List<JpaUser> queryUserByNameUseSQl(String name);

    //使用@Query注解查询
    @Query(value="from #{#entityName} where loginName = :loginName and deptId = :deptId")
    List<JpaUser> queryUserByNameAndDeptIdUseJPQL(@Param("loginName") String name, @Param("deptId") Long deptId);

    int countByLoginName(String name);

    boolean existsByLoginName(String name);


}


