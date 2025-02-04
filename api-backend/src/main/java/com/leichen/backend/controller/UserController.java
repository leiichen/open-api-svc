package com.leichen.backend.controller;


import com.leichen.backend.common.BaseResponse;
import com.leichen.backend.common.ErrorCode;
import com.leichen.backend.common.PageResp;
import com.leichen.backend.common.ResultUtils;
import com.leichen.backend.constant.CommonConstant;
import com.leichen.backend.exception.BusinessException;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.model.DTO.*;
import com.leichen.backend.model.converter.UserConverter;
import com.leichen.backend.service.UserService;
import com.leichen.backend.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户接口
 *
 * @author yupi
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/page")
    public BaseResponse<PageResp<UserResp>> listInterfaceInfoByPage(UserRequest userRequest) {
        UserBO userBO = UserConverter.INSTANCE.toUserBO(userRequest);
        userBO.setSortField("update_time");
        userBO.setSortOrder(CommonConstant.SORT_ORDER_ASC);
        PageResp<UserBO> interfaceInfoDOPage = userService.queryPage(userBO);
        PageResp<UserResp> userRespPageResp = UserConverter.INSTANCE.toUserResp(interfaceInfoDOPage);
        return ResultUtils.success(userRespPageResp);
    }

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterReq userRegisterReq) {
        String userAccount = userRegisterReq.getUserName();
        String userPassword = userRegisterReq.getUserPassword();
        String checkPassword = userRegisterReq.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginReq userLoginReq,
                                          HttpServletRequest request) {
        String userName = userLoginReq.getUserName();
        String userPassword = userLoginReq.getUserPassword();
        if (StringUtils.isAnyBlank(userName, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userName.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        UserVO userVO = userService.userLogin(userName, userPassword, request);
        return ResultUtils.success(userVO);
    }

    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    @GetMapping("/getLoginUser")
    public BaseResponse<UserVO> getLoginUser() {
        UserVO userVO = UserConverter.INSTANCE.toUserVO(UserHolder.getUser());
        return ResultUtils.success(userVO);
    }

    @PostMapping("/add")
    public BaseResponse<Void> addUser(@RequestBody UserRequest userRequest) {
        UserBO userBO = UserConverter.INSTANCE.toUserBO(userRequest);
        boolean result = userService.save(userBO);
        if (!result) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    public BaseResponse<Void> deleteUser(Long id) {
        userService.deleteById(id);
        return ResultUtils.success(null);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateUser(@RequestBody UserRequest userRequest) {
        UserBO userBO = UserConverter.INSTANCE.toUserBO(userRequest);
        userService.update(userBO);
        return ResultUtils.success(null);
    }

    @GetMapping("/getUser")
    public BaseResponse<UserVO> getUserById(Integer id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserBO userBO = userService.getUserById(id);
        UserVO userVO = UserConverter.INSTANCE.toUserVO(userBO);
        return ResultUtils.success(userVO);
    }
}
