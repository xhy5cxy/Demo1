package com.etour.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPhone("13800138000");

        assertEquals(1L, user.getId().longValue());
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("13800138000", user.getPhone());
    }

    @Test
    public void testUserDefaultValues() {
        User user = new User();
        
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertNull(user.getPhone());
    }

    @Test
    public void testUserUsernameValidation() {
        User user = new User();
        user.setUsername("valid_user");
        
        assertNotNull(user.getUsername());
        assertFalse(user.getUsername().isEmpty());
    }
}