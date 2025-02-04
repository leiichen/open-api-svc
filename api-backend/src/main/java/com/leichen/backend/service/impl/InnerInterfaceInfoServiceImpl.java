package com.leichen.backend.service.impl;

import com.leichen.apicommon.model.entity.InterfaceInfo;
import com.leichen.apicommon.service.inner.InnerInterfaceInfoService;
import com.leichen.backend.Repository.InterfaceInfoRepository;
import com.leichen.backend.mapper.InterfaceInfoMapper;
import com.leichen.backend.model.converter.InterfaceInfoConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
@Slf4j
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {
    @Resource
    private InterfaceInfoRepository interfaceInfoRepository;
    @Override
    public InterfaceInfo getInterfaceInfo(String path, String method) {
        if (path.contains("?")) {
            path = path.substring(0, path.indexOf("?"));
        }
        if (path.startsWith("http://")) {
            path = path.substring(7);
        }
        if (path.startsWith("https://")) {
            path = path.substring(8);
        }
        log.info("【查询地址】：" + path);
        return InterfaceInfoConverter.INSTANCE.toInterfaceInfo(interfaceInfoRepository.getInterface(path, method));
    }
}
