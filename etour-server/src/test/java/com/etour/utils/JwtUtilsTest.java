package com.etour.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilsTest {

    @Test
    public void testJwtUtilsClassExists() {
        assertNotNull(JwtUtils.class);
    }

    @Test
    public void testGenerateTokenMethodExists() {
        try {
            JwtUtils.class.getMethod("generateToken", Long.class, String.class);
        } catch (NoSuchMethodException e) {
            fail("generateToken method not found");
        }
    }

    @Test
    public void testValidateTokenMethodExists() {
        try {
            JwtUtils.class.getMethod("validateToken", String.class);
        } catch (NoSuchMethodException e) {
            fail("validateToken method not found");
        }
    }

    @Test
    public void testExtractUserIdMethodExists() {
        try {
            JwtUtils.class.getMethod("extractUserId", String.class);
        } catch (NoSuchMethodException e) {
            fail("extractUserId method not found");
        }
    }
}