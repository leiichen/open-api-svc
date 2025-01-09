package com.leichen.backend.Repository;

import com.leichen.backend.mapper.InterfaceInfoMapper;
import com.leichen.backend.model.BO.InterfaceInfoBO;
import com.leichen.backend.model.DO.InterfaceInfoDO;
import com.leichen.backend.model.PO.InterfaceInfoPO;
import com.leichen.backend.model.converter.InterfaceInfoConverter;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class InterfaceInfoRepository {
    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;


    public Integer queryCount(InterfaceInfoBO interfaceInfoBO) {
        InterfaceInfoPO interfaceInfoPO = InterfaceInfoConverter.INSTANCE.toInterfaceInfoPO(interfaceInfoBO);
        return interfaceInfoMapper.queryCount(interfaceInfoPO);
    }

    public List<InterfaceInfoDO> queryPage(InterfaceInfoBO interfaceInfoBO) {
        InterfaceInfoPO interfaceInfoPO = InterfaceInfoConverter.INSTANCE.toInterfaceInfoPO(interfaceInfoBO);
        return interfaceInfoMapper.queryPage(interfaceInfoPO);
    }

    public void save(InterfaceInfoBO interfaceInfoBO) {
        InterfaceInfoPO interfaceInfoPO = InterfaceInfoConverter.INSTANCE.toInterfaceInfoPO(interfaceInfoBO);
        interfaceInfoMapper.save(interfaceInfoPO);
    }

    public void delete(Long id) {
        interfaceInfoMapper.delete(id);
    }

    public InterfaceInfoDO getInterfaceById(Long id) {
        return interfaceInfoMapper.getInterfaceById(id);
    }

    public void update(InterfaceInfoBO interfaceInfoBO) {
        InterfaceInfoPO interfaceInfoPO = InterfaceInfoConverter.INSTANCE.toInterfaceInfoPO(interfaceInfoBO);
        interfaceInfoMapper.update(interfaceInfoPO);
    }
}
