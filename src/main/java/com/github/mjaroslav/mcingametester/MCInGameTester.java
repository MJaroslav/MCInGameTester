package com.github.mjaroslav.mcingametester;

import com.github.mjaroslav.mcingametester.common.CommonProxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.jetbrains.annotations.NotNull;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.*;

@Mod(modid = MOD_ID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public class MCInGameTester {
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    @Instance
    public static MCInGameTester instance;

    @EventHandler
    public void onFMLConstructionEvent(@NotNull FMLConstructionEvent event) {
        proxy.onFMLConstructionEvent(event);
    }

    @EventHandler
    public void onFMLPreInitializationEvent(@NotNull FMLPreInitializationEvent event) {
        proxy.onFMLPreInitializationEvent(event);
    }

    @EventHandler
    public void onFMLInitializationEvent(@NotNull FMLInitializationEvent event) {
        proxy.onFMLInitializationEvent(event);
    }

    @EventHandler
    public void onFMLPostInitializationEvent(@NotNull FMLPostInitializationEvent event) {
        proxy.onFMLPostInitializationEvent(event);
    }

    @EventHandler
    public void onFMLServerAboutToStartEvent(@NotNull FMLServerAboutToStartEvent event) {
        proxy.onFMLServerAboutToStartEvent(event);
    }

    @EventHandler
    public void onFMLServerStartedEvent(@NotNull FMLServerStartedEvent event) {
        proxy.onFMLServerStartedEvent(event);
    }

    @EventHandler
    public void onFMLServerStartingEvent(@NotNull FMLServerStartingEvent event) {
        proxy.onFMLServerStartingEvent(event);
    }

    @EventHandler
    public void onFMLServerStoppedEvent(@NotNull FMLServerStoppedEvent event) {
        proxy.onFMLServerStoppedEvent(event);
    }

    @EventHandler
    public void onFMLServerStoppingEvent(@NotNull FMLServerStoppingEvent event) {
        proxy.onFMLServerStoppingEvent(event);
    }

    @EventHandler
    public void onFMLLoadCompleteEvent(@NotNull FMLLoadCompleteEvent event) {
        proxy.onFMLLoadCompleteEvent(event);
    }
}
