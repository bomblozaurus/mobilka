package com.teamE.commonAddsEvents.converters;

import com.teamE.commonAddsEvents.Scope;
import java.beans.PropertyEditorSupport;
import java.util.Arrays;

public class ScopeConverter extends PropertyEditorSupport{

    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(fromValue(text));
    }

    private static Scope fromValue(String value) {
        for (Scope category : Scope.values()) {
            if (category.getValue().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + value + ", Allowed values are " + Arrays.toString(Scope.values()));
    }
}