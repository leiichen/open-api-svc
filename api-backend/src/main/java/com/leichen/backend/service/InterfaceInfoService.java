package com.leichen.backend.service;

import com.leichen.backend.common.BaseResponse;
import com.leichen.backend.common.PageResp;
import com.leichen.backend.model.BO.InterfaceInfoBO;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.model.DTO.InterfaceInfoReq;
import com.leichen.backend.model.DTO.InvokeRequest;

/**
* @author lei
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-09-28 14:16:37
*/
public interface InterfaceInfoService {
    PageResp<InterfaceInfoBO> queryPage(InterfaceInfoBO interfaceInfoBO);

    void save(InterfaceInfoBO interfaceInfoBO);

    void deleteInterfaceInfo(Long id, UserBO userBO);

    void updateInterfaceInfo(InterfaceInfoReq interfaceInfoReq, UserBO userBO);

    void offlineInterfaceInfo(Long id);

    void publishInterfaceInfo(Long id);

    BaseResponse<Object> invokeInterfaceInfo(InvokeRequest invokeRequest);
}
