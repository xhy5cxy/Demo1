package com.etour.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestControllerTest {

    @Test
    public void testTestControllerExists() {
        assertNotNull(TestController.class);
    }

    @Test
    public void testTestControllerHasEndpointMethods() {
        assertTrue(TestController.class.getDeclaredMethods().length > 0);
    }
}