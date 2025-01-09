package com.leichen.backend.model.PO;

import com.leichen.backend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
public class UserPO extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称（账号）
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色：user / admin
     */
    private String userRole;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
