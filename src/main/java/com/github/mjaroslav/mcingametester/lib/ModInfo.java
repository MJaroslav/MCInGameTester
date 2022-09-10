package com.github.mjaroslav.mcingametester.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModInfo {
    public static final String MOD_ID = "mcingametester";
    public static final String NAME = "MCInGameTester";
    public static final String VERSION = "@VERSION@";
    public static final String DEPENDENCIES = "after:*;"; // After all mods

    public static final String CLIENT_PROXY = "com.github.mjaroslav.mcingametester.mod.client.ClientProxy";
    public static final String SERVER_PROXY = "com.github.mjaroslav.mcingametester.mod.common.CommonProxy";

    /**
     * Test logger, use it if you want log something.
     */
    public static final Logger LOG = LogManager.getLogger(NAME);

    public static final String PROP_STOP_AFTER_SUCCESS = NAME + ".stopAfterSuccess";
    public static final String PROP_FORCED_STOP_STATE = NAME + ".forcedGameStopState";
    public static final String PROP_STOP_NO_TESTS = NAME + ".stopNoTests";
    public static final String PROP_STOP_FIRST_FAIL = NAME + ".stopFirstFail";
    public static final String PROP_HALT_EXIT = NAME + ".haltExit";

    public static final String ENV_STOP_AFTER_SUCCESS = "MCIGT_STOP_AFTER_SUCCESS";
    public static final String ENV_FORCED_STOP_STATE = "MCIGT_FORCED_GAME_STOP_STATE";
    public static final String ENV_STOP_NO_TESTS = "MCIGT_STOP_NO_TESTS";
    public static final String ENV_STOP_FIRST_FAIL = "MCIGT_STOP_FIRST_FAIL";
    public static final String ENV_HALT_EXIT = "MCIGT_HALT_EXIT";
}
