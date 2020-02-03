package com.teamE.events.data.entity;

import org.hibernate.search.bridge.builtin.EnumBridge;

public class StudentHouseBridge extends EnumBridge {

    @Override
    public Enum<? extends Enum> stringToObject(String stringValue) {
        if (stringValue.equals("_null_")) {
            return null;
        }
        return super.stringToObject(stringValue);
    }

    @Override
    public String objectToString(Object object) {
        if (object == null) {
            return "_null_";
        }
        return super.objectToString(object);
    }
}
