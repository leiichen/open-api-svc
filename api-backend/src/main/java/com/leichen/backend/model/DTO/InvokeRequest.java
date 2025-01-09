package com.leichen.backend.model.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InvokeRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 接口id
     */
    private Long interfaceId;
    /**
     * 请求参数
     */
    private List<Field> userRequestParams;

    @Data
    public static class Field {
        private String field;
        private String value;
    }
}
