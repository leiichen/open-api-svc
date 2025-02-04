package com.leichen.backend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONNull;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.apiclient.client.ApiClient;
import com.apiclient.config.ApiClientConfig;
import com.apiclient.model.request.BaseRequest;
import com.apiclient.model.response.ResultResponse;
import com.apiclient.service.BaseService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.leichen.backend.Repository.InterfaceInfoRepository;
import com.leichen.backend.common.BaseResponse;
import com.leichen.backend.common.ErrorCode;
import com.leichen.backend.common.PageResp;
import com.leichen.backend.common.ResultUtils;
import com.leichen.backend.exception.BusinessException;
import com.leichen.backend.model.BO.InterfaceInfoBO;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.model.DO.InterfaceInfoDO;
import com.leichen.backend.model.DTO.InterfaceInfoReq;
import com.leichen.backend.model.DTO.InvokeRequest;
import com.leichen.backend.model.converter.InterfaceInfoConverter;
import com.leichen.backend.model.enums.InterfaceStatusEnum;
import com.leichen.backend.service.InterfaceInfoService;
import com.leichen.backend.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

import static com.leichen.backend.constant.UserConstant.ADMIN_ROLE;


/**
 * @author lei
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-09-28 14:16:37
 */
@Service
@Slf4j
public class InterfaceInfoServiceImpl implements InterfaceInfoService {

    @Resource
    private InterfaceInfoRepository interfaceInfoRepository;

    @Resource
    private BaseService baseService;

    @Override
    public PageResp<InterfaceInfoBO> queryPage(InterfaceInfoBO interfaceInfoBO) {
        Integer total = interfaceInfoRepository.queryCount(interfaceInfoBO);
        PageResp<InterfaceInfoBO> interfaceInfoBOPageResp = new PageResp<>();
        if (total == 0) {
            interfaceInfoBOPageResp.setTotal(0);
            return interfaceInfoBOPageResp;
        }
        List<InterfaceInfoDO> interfaceInfoDOS = interfaceInfoRepository.queryPage(interfaceInfoBO);
        List<InterfaceInfoBO> interfaceInfoBOS = InterfaceInfoConverter.INSTANCE.toInterfaceInfoBOList(interfaceInfoDOS);
        interfaceInfoBOPageResp.setTotal(total);
        interfaceInfoBOPageResp.setList(interfaceInfoBOS);
        return interfaceInfoBOPageResp;
    }

    @Override
    public void save(InterfaceInfoBO interfaceInfoBO) {
        interfaceInfoRepository.save(interfaceInfoBO);
    }

    @Override
    public void deleteInterfaceInfo(Long id, UserBO userBO) {
        InterfaceInfoDO interfaceInfoDO = interfaceInfoRepository.getInterfaceById(id);
        if (interfaceInfoDO == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 仅本人或管理员可删除
        if (!interfaceInfoDO.getCreatedBy().equals(userBO.getUserName()) && !ADMIN_ROLE.equals(userBO.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        interfaceInfoRepository.delete(id);
    }

    @Override
    public void updateInterfaceInfo(InterfaceInfoReq interfaceInfoReq, UserBO userBO) {
        InterfaceInfoBO interfaceInfoBO = InterfaceInfoConverter.INSTANCE.toInterfaceInfoBO(interfaceInfoReq);
        // 仅本人或管理员可修改
        if (!interfaceInfoBO.getCreatedBy().equals(userBO.getUserName()) && !ADMIN_ROLE.equals(userBO.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        interfaceInfoReq.setUpdatedBy(userBO.getUserName());
        interfaceInfoRepository.update(interfaceInfoBO);
    }

    @Override
    public void offlineInterfaceInfo(Long id) {
        InterfaceInfoDO interfaceInfoDO = interfaceInfoRepository.getInterfaceById(id);
        interfaceInfoDO.setStatus(InterfaceStatusEnum.OFFLINE.getValue());
        interfaceInfoRepository.update(InterfaceInfoConverter.INSTANCE.toInterfaceInfoBO(interfaceInfoDO));
    }

    @Override
    public void publishInterfaceInfo(Long id) {
        InterfaceInfoDO interfaceInfoDO = interfaceInfoRepository.getInterfaceById(id);
        interfaceInfoDO.setStatus(InterfaceStatusEnum.ONLINE.getValue());
        interfaceInfoRepository.update(InterfaceInfoConverter.INSTANCE.toInterfaceInfoBO(interfaceInfoDO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse<Object> invokeInterfaceInfo(InvokeRequest invokeRequest) {
        Long interfaceId = invokeRequest.getInterfaceId();
        InterfaceInfoDO interfaceInfoDO = interfaceInfoRepository.getInterfaceById(interfaceId);
        if (interfaceInfoDO.getStatus() != InterfaceStatusEnum.ONLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口未开启");
        }
        List<InvokeRequest.Field> userRequestParams = invokeRequest.getUserRequestParams();
        HashMap<String, Object> params = new HashMap<>();
        if (CollUtil.isNotEmpty(userRequestParams)) {
            JSONObject jsonObject = new JSONObject();
            for (InvokeRequest.Field userRequestParam : userRequestParams) {
                String field = userRequestParam.getField();
                String value = userRequestParam.getValue();
                jsonObject.putOnce(field, value);
            }
            params = JSONUtil.toBean(jsonObject, HashMap.class);
        }
        UserBO userBO = UserHolder.getUser();
        String accessKey = userBO.getAccessKey();
        String secretKey = userBO.getSecretKey();
        try {
            ApiClient apiClient = new ApiClient(accessKey, secretKey);
            BaseRequest baseRequest = BaseRequest.builder()
                    .path(interfaceInfoDO.getUrl())
                    .method(interfaceInfoDO.getMethod())
                    .requestParams(params)
                    .build();
            ResultResponse response = baseService.request(apiClient, baseRequest);
            return ResultUtils.success(response);
        } catch (Exception e) {
            log.error("调用接口失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, e.getMessage());
        }
    }
}




