package com.leichen.backend.model.converter;

import com.leichen.backend.common.PageResp;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.model.DO.UserDO;
import com.leichen.backend.model.DTO.UserRequest;
import com.leichen.backend.model.DTO.UserResp;
import com.leichen.backend.model.DTO.UserVO;
import com.leichen.backend.model.PO.UserPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    UserPO toUserPO(UserBO userBO);

    UserVO toUserVO(UserBO userBO);

    UserVO toUserVO(UserDO userDO);

    UserBO toUserBO(UserRequest userRequest);

    UserBO toUserBO(UserDO userDO);

    PageResp<UserResp> toUserResp(PageResp<UserBO> interfaceInfoDOPage);

    List<UserBO> toUserBOList(List<UserDO> userDOList);
}
