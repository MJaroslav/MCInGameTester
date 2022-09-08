package com.github.mjaroslav.mcingametester.test;

import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Test;

@Common
public class TestCommonSide {
    @Test(expected = IllegalStateException.class)
    public void test$expectedOnBothSides() {
        throw new IllegalStateException("Expected");
    }
}
