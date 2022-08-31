package com.github.mjaroslav.mcingametester.mod;

import com.github.mjaroslav.mcingametester.mod.common.CommonProxy;
import com.github.mjaroslav.mcingametester.engine.Engine;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.jetbrains.annotations.NotNull;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.*;

@Mod(modid = MOD_ID, name = NAME, version = VERSION, dependencies = DEPENDENCIES, acceptableRemoteVersions = "*")
public final class MCInGameTester {
    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void onFMLConstructionEvent(@NotNull FMLConstructionEvent event) {
        Engine.INSTANCE.findCandidates(event.getASMHarvestedData());
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLPreInitializationEvent(@NotNull FMLPreInitializationEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLInitializationEvent(@NotNull FMLInitializationEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLPostInitializationEvent(@NotNull FMLPostInitializationEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerAboutToStartEvent(@NotNull FMLServerAboutToStartEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStartedEvent(@NotNull FMLServerStartedEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStartingEvent(@NotNull FMLServerStartingEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStoppedEvent(@NotNull FMLServerStoppedEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStoppingEvent(@NotNull FMLServerStoppingEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLLoadCompleteEvent(@NotNull FMLLoadCompleteEvent event) {
        Engine.INSTANCE.onFMLStateEvent(event);
    }
}
