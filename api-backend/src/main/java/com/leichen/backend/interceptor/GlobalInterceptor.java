package com.leichen.backend.interceptor;

import com.leichen.backend.common.ErrorCode;
import com.leichen.backend.exception.BusinessException;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.service.UserService;
import com.leichen.backend.utils.UserHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.leichen.backend.constant.UserConstant.USER_LOGIN_STATE;

@Component
public class GlobalInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        UserBO currentUser = (UserBO) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        currentUser = userService.getUserById(currentUser.getId());
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        UserHolder.saveUser(currentUser);
        return true;
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
