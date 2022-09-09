package com.github.mjaroslav.mcingametester.test;

import com.github.mjaroslav.mcingametester.api.Assert;
import com.github.mjaroslav.mcingametester.api.Server;
import com.github.mjaroslav.mcingametester.api.Test;
import com.github.mjaroslav.mcingametester.api.WorldShadow;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState;
import net.minecraft.world.World;

@Server(when = LoaderState.SERVER_STARTED) // World will fully load on this state.
public class TestServerSide {
    @WorldShadow
    World overworld;

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

    @Test
    void test$shadowWorld() {
        Assert.isTrue(overworld != null, "Null world");
        Assert.isEquals(overworld.provider.dimensionId, 0, "Not Overworld");
    }
}
