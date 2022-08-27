package com.github.mjaroslav.mcingametester.client;

import com.github.mjaroslav.mcingametester.common.CommonProxy;
import net.minecraft.client.Minecraft;

public class ClientProxy extends CommonProxy {
    @Override
    public void stopGame() {
        Minecraft.getMinecraft().shutdown();
    }
}
