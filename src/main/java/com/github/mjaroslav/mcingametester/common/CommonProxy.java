package com.github.mjaroslav.mcingametester.common;

import cpw.mods.fml.common.event.*;
import org.jetbrains.annotations.NotNull;

public class CommonProxy {
    public void onFMLConstructionEvent(@NotNull FMLConstructionEvent event) {}

    public void onFMLPreInitializationEvent(@NotNull FMLPreInitializationEvent event) {}

    public void onFMLInitializationEvent(@NotNull FMLInitializationEvent event) {}

    public void onFMLPostInitializationEvent(@NotNull FMLPostInitializationEvent event) {}

    public void onFMLServerAboutToStartEvent(@NotNull FMLServerAboutToStartEvent event) {}

    public void onFMLServerStartedEvent(@NotNull FMLServerStartedEvent event) {}

    public void onFMLServerStartingEvent(@NotNull FMLServerStartingEvent event) {}

    public void onFMLServerStoppedEvent(@NotNull FMLServerStoppedEvent event) {}

    public void onFMLServerStoppingEvent(@NotNull FMLServerStoppingEvent event) {}

    public void onFMLLoadCompleteEvent(@NotNull FMLLoadCompleteEvent event) {}
}
