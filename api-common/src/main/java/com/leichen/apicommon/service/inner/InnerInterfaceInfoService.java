package com.leichen.apicommon.service.inner;

import com.leichen.apicommon.model.entity.InterfaceInfo;

/**
 * 接口信息服务
 */
public interface InnerInterfaceInfoService {
    /**
     * 获取接口信息
     * @param path
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
