package com.github.mjaroslav.mcingametester.test;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.Server;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.FMLCommonHandler;

@Server
public class TestServerSide {
    @Test(expected = ClassNotFoundException.class)
    void test$clientClass() throws ClassNotFoundException {
        Class.forName("net.minecraft.client.Minecraft");
    }

    @Test
    void test$serverClass() throws ClassNotFoundException {
        Class.forName("net.minecraft.server.dedicated.DedicatedServer");
    }

    @Test
    void test$fmlSide() {
        Assert.isTrue(FMLCommonHandler.instance().getSide().isServer(), "Non server side");
    }
}
