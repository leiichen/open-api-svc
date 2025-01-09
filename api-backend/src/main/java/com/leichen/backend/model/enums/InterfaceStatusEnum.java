package com.leichen.backend.model.enums;

public enum InterfaceStatusEnum {
    OFFLINE("下线", 0),
    ONLINE("上线", 1);

    private final String text;
    private final int value;

    InterfaceStatusEnum(String text, int value)
    {
        this.text = text;
        this.value = value;
    }
    public int getValue() {
        return value;
    }

}
