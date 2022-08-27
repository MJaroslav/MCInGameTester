package com.github.mjaroslav.mcingametester.common;

import com.github.mjaroslav.mcingametester.loader.TestLoader;
import cpw.mods.fml.common.event.*;
import org.jetbrains.annotations.NotNull;

public abstract class CommonProxy {
    protected TestLoader loader;

    public void onFMLConstructionEvent(@NotNull FMLConstructionEvent event) {
        loader = new TestLoader();
        loader.parseASMTable(event.getASMHarvestedData());
        loader.onFMLStateEvent(event);
    }

    public void onFMLPreInitializationEvent(@NotNull FMLPreInitializationEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLInitializationEvent(@NotNull FMLInitializationEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLPostInitializationEvent(@NotNull FMLPostInitializationEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLServerAboutToStartEvent(@NotNull FMLServerAboutToStartEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLServerStartedEvent(@NotNull FMLServerStartedEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLServerStartingEvent(@NotNull FMLServerStartingEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLServerStoppedEvent(@NotNull FMLServerStoppedEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLServerStoppingEvent(@NotNull FMLServerStoppingEvent event) {
        loader.onFMLStateEvent(event);
    }

    public void onFMLLoadCompleteEvent(@NotNull FMLLoadCompleteEvent event) {
        loader.onFMLStateEvent(event);
    }

    public abstract void softGameStop();
}
