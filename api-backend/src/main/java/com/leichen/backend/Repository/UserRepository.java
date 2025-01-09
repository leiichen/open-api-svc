package com.leichen.backend.Repository;

import com.leichen.backend.mapper.UserMapper;
import com.leichen.backend.model.BO.UserBO;
import com.leichen.backend.model.DO.UserDO;
import com.leichen.backend.model.PO.UserPO;
import com.leichen.backend.model.converter.UserConverter;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserRepository {
    @Resource
    private UserMapper userMapper;
    public Integer queryCount(UserBO userBO) {
        UserPO userPO = UserConverter.INSTANCE.toUserPO(userBO);
        return userMapper.queryCount(userPO);
    }

    public List<UserDO> queryPage(UserBO userBO) {
        UserPO userPO = UserConverter.INSTANCE.toUserPO(userBO);
        return userMapper.queryPage(userPO);
    }

    public void save(UserBO userBO) {
        UserPO userPO = UserConverter.INSTANCE.toUserPO(userBO);
        userMapper.save(userPO);
        userBO.setId(userPO.getId());
    }

    public void delete(Long id) {
        userMapper.delete(id);
    }

    public UserDO getUserById(Long id) {
        return userMapper.getUserById(id);
    }

    public void update(UserBO userBO) {
        UserPO UserPO = UserConverter.INSTANCE.toUserPO(userBO);
        userMapper.update(UserPO);
    }

    public UserDO selectOne(UserBO userBO) {
        UserPO userPO = UserConverter.INSTANCE.toUserPO(userBO);
        return userMapper.selectOne(userPO);
    }
}
