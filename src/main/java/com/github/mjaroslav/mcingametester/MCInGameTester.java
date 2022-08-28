package com.github.mjaroslav.mcingametester;

import com.github.mjaroslav.mcingametester.common.CommonProxy;
import com.github.mjaroslav.mcingametester.loader.TestLoader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.*;
import org.jetbrains.annotations.NotNull;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.*;

@Mod(modid = MOD_ID, name = NAME, version = VERSION, dependencies = DEPENDENCIES)
public final class MCInGameTester {
    public static final @NotNull TestLoader LOADER = new TestLoader();

    @SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
    public static CommonProxy proxy;

    @Instance
    public static MCInGameTester instance;

    @EventHandler
    public void onFMLConstructionEvent(@NotNull FMLConstructionEvent event) {
        LOADER.parseASMTable(event.getASMHarvestedData());
        LOG.info("MCInGameTester ready");
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLPreInitializationEvent(@NotNull FMLPreInitializationEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLInitializationEvent(@NotNull FMLInitializationEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLPostInitializationEvent(@NotNull FMLPostInitializationEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerAboutToStartEvent(@NotNull FMLServerAboutToStartEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStartedEvent(@NotNull FMLServerStartedEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStartingEvent(@NotNull FMLServerStartingEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStoppedEvent(@NotNull FMLServerStoppedEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLServerStoppingEvent(@NotNull FMLServerStoppingEvent event) {
        LOADER.onFMLStateEvent(event);
    }

    @EventHandler
    public void onFMLLoadCompleteEvent(@NotNull FMLLoadCompleteEvent event) {
        LOADER.onFMLStateEvent(event);
    }
}
