package com.github.mjaroslav.mcingametester.loader;

import com.github.mjaroslav.mcingametester.lib.ModInfo;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Config {
    public boolean isGameShouldBeStoppedAfterSuccessTests() {
        return Boolean.parseBoolean(System.getProperty(ModInfo.PROP_STOP_AFTER_SUCCESS, "true"));
    }
}
