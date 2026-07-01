package com.tour.app.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class JWTUtilsTest {

    @Test
    public void testJWTUtilsExists() {
        assertNotNull(JWTUtils.class);
    }

    @Test
    public void testIsTokenExpiredMethodExists() {
        try {
            JWTUtils.class.getMethod("isTokenExpired", String.class);
        } catch (NoSuchMethodException e) {
            fail("isTokenExpired method not found");
        }
    }

    @Test
    public void testExtractUserIdMethodExists() {
        try {
            JWTUtils.class.getMethod("extractUserId", String.class);
        } catch (NoSuchMethodException e) {
            fail("extractUserId method not found");
        }
    }
}