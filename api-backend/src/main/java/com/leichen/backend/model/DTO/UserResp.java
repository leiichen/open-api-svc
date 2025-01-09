package com.leichen.backend.model.DTO;

import lombok.Data;

import java.util.Date;

@Data
public class UserResp {
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
