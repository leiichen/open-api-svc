package com.leichen.backend.model.converter;

import com.leichen.backend.common.PageResp;
import com.leichen.backend.model.BO.InterfaceInfoBO;
import com.leichen.backend.model.DO.InterfaceInfoDO;
import com.leichen.backend.model.DTO.InterfaceInfoReq;
import com.leichen.backend.model.DTO.InterfaceInfoResp;
import com.leichen.backend.model.PO.InterfaceInfoPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InterfaceInfoConverter {
    InterfaceInfoConverter INSTANCE = Mappers.getMapper(InterfaceInfoConverter.class);
    InterfaceInfoBO toInterfaceInfoBO(InterfaceInfoReq interfaceInfoReq);
    InterfaceInfoBO toInterfaceInfoBO(InterfaceInfoDO interfaceInfoDO);

    InterfaceInfoPO toInterfaceInfoPO(InterfaceInfoBO interfaceInfoBO);

    List<InterfaceInfoBO> toInterfaceInfoBOList(List<InterfaceInfoDO> interfaceInfoDOS);

    PageResp<InterfaceInfoResp> toInterfaceInfoResp(PageResp<InterfaceInfoBO> interfaceInfoDOPage);
}
