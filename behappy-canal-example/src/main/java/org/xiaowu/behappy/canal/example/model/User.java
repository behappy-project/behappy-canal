package org.xiaowu.behappy.canal.example.model;


import jakarta.persistence.*;
import lombok.Data;
import org.xiaowu.behappy.canal.example.enums.Gender;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 测试实体类
 */
@Data
@Entity
@Table(name = "t_user")
public class User implements Serializable {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /**
     * 用户名
     */
    @Column(name = "user_name")
    private String userName;


    /**
     * 用户性别
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * 国家id
     */
    @Column(name = "country_id")
    private Integer countryId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name="logo", columnDefinition="longblob", nullable=true)
    private byte[] logo;

    /**
     * 用户出生日期
     */
    private Date birthday;

    private Boolean flag;

    /**
     * 用户创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}
