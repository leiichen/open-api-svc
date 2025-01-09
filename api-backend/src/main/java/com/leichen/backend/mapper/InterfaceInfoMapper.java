package com.leichen.backend.mapper;

import com.leichen.backend.model.DO.InterfaceInfoDO;
import com.leichen.backend.model.PO.InterfaceInfoPO;

import java.util.List;

/**
* @author lei
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
* @createDate 2024-09-28 14:16:37
* @Entity com.yupi.project.model.entity.InterfaceInfo
*/
public interface InterfaceInfoMapper {

    Integer queryCount(InterfaceInfoPO interfaceInfoPO);

    List<InterfaceInfoDO> queryPage(InterfaceInfoPO interfaceInfoPO);

    void save(InterfaceInfoPO interfaceInfoPO);

    void delete(Long id);

    InterfaceInfoDO getInterfaceById(Long id);

    void update(InterfaceInfoPO interfaceInfoPO);
}




