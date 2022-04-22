package com.bluetron.nb.common.upmsservice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bluetron.nb.common.upmsservice.jpa.JpaUser;
import com.bluetron.nb.common.upmsservice.jpa.UserRepository;
import com.bluetron.nb.common.util.tools.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @className: TestJpa
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/2/22 10:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestJpa {

    @Autowired
    private UserRepository userRepositoy;

    @Test
    public void findByIdTest() {

        Optional optional = userRepositoy.findById(1440969706411397120L);
        Assert.assertNotNull(optional.get());
    }

    @Test
    public void countByNameTest() {

        int count = userRepositoy.countByLoginName("admin");
        Assert.assertTrue(count>1);
    }

    @Test
    public void existByNameTest() {

        boolean exists = userRepositoy.existsByLoginName("admin");
        Assert.assertTrue(exists);
    }

    @Test
    public void findByLoginNameTest() {

        List<JpaUser> user = userRepositoy.findByLoginName("admin");
        log.info("user: {}--{}", user.toString(), JSONArray.toJSON(user));
        Assert.assertNotNull(user);

    }


    @Test
    public void findByLoginNameAndDeptIdTest() {

        JpaUser user = userRepositoy.findByLoginNameAndDeptId("admin", 1440911410581213416L);
        log.info("user: {}--{}", user.toString(), JSONObject.toJSON(user));
        Assert.assertNotNull(user);

    }


    @Test
    public void queryByLoginNameTest() {

        List<JpaUser> user = userRepositoy.queryUserByNameUseJPQL("admin");
        log.info("user: {}", JSONArray.toJSON(user));
        Assert.assertNotNull(user);

    }

    @Test
    public void queryByLoginNameUseSqlTest() {

        List<JpaUser> user = userRepositoy.queryUserByNameUseSQl("admin");
        log.info("user: {}", JSONArray.toJSON(user));
        Assert.assertNotNull(user);

    }

    @Test
    public void queryByLoginNameAndDeptIdTest() {

        List<JpaUser> user = userRepositoy.queryUserByNameAndDeptIdUseJPQL("admin", 1440911410581213416L);
        log.info("user: {}", JSONArray.toJSON(user));
        Assert.assertNotNull(user);

    }

    @Test
    public void saveJpaUserTest() {

        JpaUser user = new JpaUser();
        user.setUserId(IdUtil.nextLongId());
        user.setUserType(2);
        user.setDeptId(1440911410581213416L);
        user.setLoginName("testJpa");
        user.setPassword("123456");
        user.setShowName("测试Jpa用户");
        user.setDeletedFlag(1);
        user.setUserStatus(0);

        //MyModelUtil.fillCommonsForInsert(user);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setCreateUserId(11L);
        user.setUpdateUserId(11L);

        user = userRepositoy.save(user);
        log.info("create user: {}", JSONArray.toJSON(user));

        user.remove(userRepositoy,user.getUserId());

        Assert.assertNotNull(user);

    }

}


