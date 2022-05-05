package com.fontana.upmsservice.jpa;


import com.fontana.db.entity.JpaBaseEntity;
import lombok.Data;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @className: JpaUser
 * @Description: TODO
 * @version: v1.0.0
 * @author: cqf
 * @date: 2022/2/22 9:55
 */
@Entity
@Table(name = "bn_sys_user")
@Data
public class JpaUser  extends JpaBaseEntity implements Serializable{

    /**
     * 用户Id。
     */
    @Id
    private Long userId;

    /**
     * 登录用户名。
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 用户密码。
     */
    private String password;

    /**
     * 用户显示名称。
     */
    @Column(name = "show_name")
    private String showName;

    /**
     * 用户部门Id。
     */
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 用户类型(0: 管理员 1: 系统管理用户 2: 系统业务用户)。
     */
    @Column(name = "user_type")
    private Integer userType;

    /**
     * 用户头像的Url。
     */
    @Column(name = "head_image_url")
    private String headImageUrl;

    /**
     * 用户状态(0: 正常 1: 锁定)。
     */
    @Column(name = "user_status")
    private Integer userStatus;

    /**
     * 逻辑删除标记字段(1: 正常 -1: 已删除)。
     */
    @Column(name = "deleted_flag")
    private Integer deletedFlag;

    // 返回类型定义
    @DomainEvents
    public List<Object> domainEvents() {
        System.err.println("DomainEvent Publication ok");
        return Stream.of(new JpaUserEvent(this)).collect(Collectors.toList());
    }

    // 事件发布后callback
    @AfterDomainEventPublication
    void callback() {
        System.err.println("After DomainEvent Publication");
    }

    public void remove(UserRepository userRepository, Long userId){
        userRepository.deleteById(userId);
    }

    // 事件定义
    @Data
    public class JpaUserEvent {

        private final JpaUser jpaUser;
        private final String state;

        public JpaUserEvent(JpaUser jpaUser) {
            this.jpaUser = jpaUser;
            state = "SUCCEED";
        }

    }
}


