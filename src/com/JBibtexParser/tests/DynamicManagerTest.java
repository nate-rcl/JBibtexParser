package com.JBibtexParser.tests;

import com.JBibtexParser.typemanager.DynamicTypesManager;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class DynamicManagerTest {
    @Test
    public void ShouldCorrectlyCreateFields() {
        DynamicTypesManager dynamicTypesManager = new DynamicTypesManager();
        assertEquals(0,dynamicTypesManager.getAllFields().size());
        dynamicTypesManager.getField("mockField");
        assertEquals(1,dynamicTypesManager.getAllFields().size());
        dynamicTypesManager.getField("mockField2");
        assertEquals(2,dynamicTypesManager.getAllFields().size());
        dynamicTypesManager.getField("mockField");
        assertEquals(2,dynamicTypesManager.getAllFields().size());
    }
}
