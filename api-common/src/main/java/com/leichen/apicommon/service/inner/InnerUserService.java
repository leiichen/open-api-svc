package com.leichen.apicommon.service.inner;

import com.leichen.apicommon.model.VO.UserVO;

/**
 * 用户服务对外接口
 */
public interface InnerUserService {
    UserVO getInvokeUser(String accessKey);
}
