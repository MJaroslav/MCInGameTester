package com.github.mjaroslav.mcingametester.test;

import com.github.mjaroslav.mcingametester.api.*;
import com.github.mjaroslav.mcingametester.lib.ModInfo;

@Common
public class TestForTest {
    @BeforeClass
    public static void beforeClass() {
        ModInfo.LOG.info("Running before class");
    }

    @AfterClass
    public static void afterClass() {
        ModInfo.LOG.info("Running after class");
    }

    @BeforeEach
    public void beforeEach() {
        ModInfo.LOG.info("Running before each");
    }

    @AfterEach
    public void afterEach() {
        ModInfo.LOG.info("Running after each");
    }

    @Test
    public void test$1() {
        ModInfo.LOG.info("Running test$1");
    }

    @Test
    public void test$2() {
        ModInfo.LOG.info("Running test$1");
    }

    @Test
    public void test$3() {
        ModInfo.LOG.info("Running test$1");
    }

    @Test(expected = IllegalStateException.class)
    public void test$4() {
        ModInfo.LOG.info("Running test$4 with expected exception");
        throw new IllegalStateException("Expected");
    }
}
