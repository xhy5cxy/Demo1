package com.tour.app.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class StrategyTest {

    @Test
    public void testStrategyCreation() {
        Strategy strategy = new Strategy();
        strategy.setId(1L);
        strategy.setTitle("北京三日游");
        strategy.setContent("第一天：天安门、故宫...");
        strategy.setCoverImage("https://example.com/cover.jpg");
        strategy.setUserId(1001L);

        assertEquals(1L, strategy.getId().longValue());
        assertEquals("北京三日游", strategy.getTitle());
        assertEquals("第一天：天安门、故宫...", strategy.getContent());
        assertEquals("https://example.com/cover.jpg", strategy.getCoverImage());
        assertEquals(1001L, strategy.getUserId().longValue());
    }

    @Test
    public void testStrategyDefaultValues() {
        Strategy strategy = new Strategy();
        
        assertNull(strategy.getId());
        assertNull(strategy.getTitle());
        assertNull(strategy.getContent());
        assertNull(strategy.getCoverImage());
        assertNull(strategy.getUserId());
    }

    @Test
    public void testStrategyTitleNotEmpty() {
        Strategy strategy = new Strategy();
        strategy.setTitle("");
        
        assertFalse(strategy.getTitle().isEmpty());
    }
}