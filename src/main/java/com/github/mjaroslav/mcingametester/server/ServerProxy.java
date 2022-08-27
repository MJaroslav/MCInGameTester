package com.github.mjaroslav.mcingametester.server;

import com.github.mjaroslav.mcingametester.common.CommonProxy;
import net.minecraft.server.MinecraftServer;

public class ServerProxy extends CommonProxy {
    @Override
    public void stopGame() {
        MinecraftServer.getServer().initiateShutdown();
    }
}
