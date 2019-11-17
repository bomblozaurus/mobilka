package com.teamE.events.converters;


import com.teamE.events.data.entity.Scope;
import com.teamE.users.StudentHouse;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;

public class StudentHouseConverter extends PropertyEditorSupport {

    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(fromValue(text));
    }

    private static StudentHouse fromValue(String value) {
        for (StudentHouse category : StudentHouse.values()) {
            if (category.getId() == Integer.parseInt(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(Scope.values()));
    }

    }
