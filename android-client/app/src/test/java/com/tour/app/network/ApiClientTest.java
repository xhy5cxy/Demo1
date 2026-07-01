package com.tour.app.network;

import org.junit.Test;

import static org.junit.Assert.*;

public class ApiClientTest {

    @Test
    public void testBaseUrlNotEmpty() {
        assertNotNull(ApiClient.BASE_URL);
        assertFalse(ApiClient.BASE_URL.isEmpty());
    }

    @Test
    public void testBaseUrlContainsApi() {
        assertTrue(ApiClient.BASE_URL.contains("api"));
    }

    @Test
    public void testEmulatorUrlExists() {
        assertNotNull(ApiClient.BASE_URL_EMULATOR);
    }

    @Test
    public void testLocalUrlExists() {
        assertNotNull(ApiClient.BASE_URL_LOCAL);
    }
}