package com.leichen.backend.service.impl;

import com.leichen.backend.Repository.UserRepository;
import com.leichen.backend.common.ErrorCode;
import com.leichen.backend.common.PageResp;
import com.leichen.backend.exception.BusinessException;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.model.DO.UserDO;
import com.leichen.backend.model.DTO.UserVO;
import com.leichen.backend.model.converter.UserConverter;
import com.leichen.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.leichen.backend.constant.UserConstant.USER_LOGIN_STATE;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "lei.chen";

    @Override
    public long userRegister(String userName, String userPassword, String checkPassword) {
        if (userName.length() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名称过短");
        }
        if (userPassword.length() < 4 || checkPassword.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userName.intern()) {
            long count = userRepository.queryCount(UserBO.builder().userName(userName).build());
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            UserBO userBO = UserBO.builder()
                    .userName(userName)
                    .userPassword(encryptPassword)
                    .build();
            userRepository.save(userBO);
            return userBO.getId();
        }
    }

    @Override
    public UserVO userLogin(String userName, String userPassword,
                            HttpServletRequest request) {
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        UserDO userDO = userRepository.selectOne(UserBO.builder().userName(userName).userPassword(encryptPassword).build());
        UserBO userBO = UserConverter.INSTANCE.toUserBO(userDO);
        if (userBO == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        UserVO userVO = UserConverter.INSTANCE.toUserVO(userBO);
        request.getSession().setAttribute(USER_LOGIN_STATE, userBO);
        return userVO;
    }


    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserBO getUserById(long userId) {
        UserDO userDO = userRepository.getUserById(userId);
        return UserConverter.INSTANCE.toUserBO(userDO);
    }

    @Override
    public boolean save(UserBO userBO) {
        try {
            userRepository.save(userBO);
        } catch (DuplicateKeyException e) {
            log.error("save user error", e);
            return false;
        }
        return true;
    }

    @Override
    public void update(UserBO userBO) {
        userRepository.update(userBO);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.delete(id);
    }

    @Override
    public PageResp<UserBO> queryPage(UserBO userBO) {
        Integer total =  userRepository.queryCount(userBO);
        PageResp<UserBO> userBOPageResp = new PageResp<>();
        if (total == 0) {
            userBOPageResp.setTotal(0);
            return userBOPageResp;
        }
        List<UserDO> userDOList =  userRepository.queryPage(userBO);
        List<UserBO> userBOList = UserConverter.INSTANCE.toUserBOList(userDOList);
        userBOPageResp.setTotal(total);
        userBOPageResp.setList(userBOList);
        return userBOPageResp;
    }

}




