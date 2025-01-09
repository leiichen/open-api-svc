package com.leichen.backend.controller;

import com.leichen.backend.annotation.AuthCheck;
import com.leichen.backend.common.BaseResponse;
import com.leichen.backend.common.PageResp;
import com.leichen.backend.common.ResultUtils;
import com.leichen.backend.constant.CommonConstant;
import com.leichen.backend.model.BO.InterfaceInfoBO;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.model.DTO.InterfaceInfoReq;
import com.leichen.backend.model.DTO.InterfaceInfoResp;
import com.leichen.backend.model.DTO.InvokeRequest;
import com.leichen.backend.model.converter.InterfaceInfoConverter;
import com.leichen.backend.service.InterfaceInfoService;
import com.leichen.backend.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.leichen.backend.constant.UserConstant.ADMIN_ROLE;


@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {
    @Resource
    private InterfaceInfoService interfaceInfoService;

    @GetMapping("/page")
    public BaseResponse<PageResp<InterfaceInfoResp>> listInterfaceInfoByPage(InterfaceInfoReq interfaceInfoReq) {
        InterfaceInfoBO interfaceInfoBO = InterfaceInfoConverter.INSTANCE.toInterfaceInfoBO(interfaceInfoReq);
        interfaceInfoBO.setSortField("update_time");
        interfaceInfoBO.setSortOrder(CommonConstant.SORT_ORDER_ASC);
        PageResp<InterfaceInfoBO> interfaceInfoDOPage = interfaceInfoService.queryPage(interfaceInfoBO);
        PageResp<InterfaceInfoResp> interfaceInfoRespPage = InterfaceInfoConverter.INSTANCE.toInterfaceInfoResp(interfaceInfoDOPage);
        return ResultUtils.success(interfaceInfoRespPage);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoReq interfaceInfoReq) {
        UserBO userBO = UserHolder.getUser();
        interfaceInfoReq.setUpdatedBy(userBO.getUserName());
        interfaceInfoReq.setCreatedBy(userBO.getUserName());
        InterfaceInfoBO interfaceInfoBO = InterfaceInfoConverter.INSTANCE.toInterfaceInfoBO(interfaceInfoReq);
        interfaceInfoService.save(interfaceInfoBO);
        return ResultUtils.success(null);
    }

    @PostMapping("/delete")
    public BaseResponse<Void> deleteInterfaceInfo(Long id) {
        UserBO userBO = UserHolder.getUser();
        interfaceInfoService.deleteInterfaceInfo(id, userBO);
        return ResultUtils.success(null);
    }

    @PostMapping("/update")
    public BaseResponse<Void> updateInterfaceInfo(@RequestBody InterfaceInfoReq interfaceInfoReq) {
        UserBO userBO = UserHolder.getUser();
        interfaceInfoService.updateInterfaceInfo(interfaceInfoReq, userBO);
        return ResultUtils.success(null);
    }

    @PostMapping("/publish")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Void> publishInterfaceInfo(Long id) {
        interfaceInfoService.publishInterfaceInfo(id);
        return ResultUtils.success(null);
    }

    @PostMapping("/offline")
    @AuthCheck(mustRole = ADMIN_ROLE)
    public BaseResponse<Void> offlineInterfaceInfo(Long id) {
        interfaceInfoService.offlineInterfaceInfo(id);
        return ResultUtils.success(null);
    }

    @PostMapping("/invoke")
    public BaseResponse<Object> invokeInterfaceInfo(@RequestBody InvokeRequest invokeRequest) {
        return interfaceInfoService.invokeInterfaceInfo(invokeRequest);
    }
}
