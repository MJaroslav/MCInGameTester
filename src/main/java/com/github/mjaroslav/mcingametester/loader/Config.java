package com.github.mjaroslav.mcingametester.loader;

import cpw.mods.fml.common.LoaderState;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.*;

@UtilityClass
public class Config {
    public boolean isShouldStopAfterSuccess() {
        return strongBoolean(ENV_STOP_AFTER_SUCCESS, PROP_STOP_AFTER_SUCCESS, true);
    }

    public @Nullable LoaderState getForcedGameStopState() {
        var value = System.getenv(ENV_FORCED_STOP_STATE);
        if (value == null) value = System.getProperty(PROP_FORCED_STOP_STATE);
        if (value == null) return null;
        return LoaderState.valueOf(value);
    }

    public boolean isShouldStopOnFirstFail() {
        return strongBoolean(ENV_STOP_FIRST_FAIL, PROP_STOP_FIRST_FAIL, false);
    }

    public boolean isShouldStopWhenNoTests() {
        // May be use CL env existing as default value?
        return strongBoolean(ENV_STOP_NO_TESTS, PROP_STOP_NO_TESTS, true);
    }

    public boolean isShouldHaltExit() {
        return strongBoolean(ENV_HALT_EXIT, PROP_HALT_EXIT, false);
    }

    private boolean strongBoolean(@NotNull String env, @NotNull String prop, boolean defaultValue) {
        var value = System.getenv(env);
        if (value == null) value = System.getProperty(prop);
        if (value == null) return defaultValue;
        if (!value.equalsIgnoreCase("false") && !value.equalsIgnoreCase("true"))
            throw new IllegalArgumentException(env + " environment variable (or " + prop + " system property) should " +
                    "be with strong boolean value");
        return Boolean.parseBoolean(value);
    }
}
