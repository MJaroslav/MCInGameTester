package com.github.mjaroslav.mcingametester.test;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.Client;
import com.github.mjaroslav.mcingametester.api.Test;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState;

@Client(when = LoaderState.INITIALIZATION)
public class TestClientSide {
    @Test
    void test$clientClass() throws ClassNotFoundException {
        Class.forName("net.minecraft.client.Minecraft");
    }

    @Test(expected = ClassNotFoundException.class)
    void test$serverClass() throws ClassNotFoundException {
        Class.forName("net.minecraft.server.dedicated.DedicatedServer");
    }

    @Test
    void test$fmlSide() {
        Assert.isTrue(FMLCommonHandler.instance().getSide().isClient(), "Non client side");
    }
}
