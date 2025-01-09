package com.leichen.backend.model.DTO;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author yupi
 */
@Data
public class UserRegisterReq implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userName;

    private String userPassword;

    private String checkPassword;
}
