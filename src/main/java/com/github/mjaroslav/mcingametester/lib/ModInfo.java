package com.github.mjaroslav.mcingametester.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModInfo {
    public static final String MOD_ID = "mcingametester";
    public static final String NAME = "MCInGameTester";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "after:*;"; // After all mods

    public static final String CLIENT_PROXY = "com.github.mjaroslav.mcingametester.client.ClientProxy";
    public static final String SERVER_PROXY = "com.github.mjaroslav.mcingametester.common.CommonProxy";

    public static final Logger LOG = LogManager.getLogger(NAME);

    public static final String PROP_STOP_AFTER_SUCCESS = NAME + ".stopAfterSuccess";
}
