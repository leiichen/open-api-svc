package com.leichen.backend.service.impl;

import cn.hutool.core.util.StrUtil;

import com.leichen.apicommon.model.VO.UserVO;
import com.leichen.apicommon.service.inner.InnerUserService;
import com.leichen.backend.Repository.UserRepository;
import com.leichen.backend.common.ErrorCode;
import com.leichen.backend.exception.BusinessException;
import com.leichen.backend.model.DO.UserDO;
import com.leichen.backend.model.converter.UserConverter;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {
    @Resource
    private UserRepository userRepository;
    @Override
    public UserVO getInvokeUser(String accessKey) {
        if (StrUtil.isEmpty(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserDO userDO = userRepository.getUserByAccessKey(accessKey);
        UserVO userVO = UserConverter.INSTANCE.toCommonUserVO(userDO);
        return userVO;
    }
}
