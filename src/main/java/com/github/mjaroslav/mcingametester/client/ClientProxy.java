package com.github.mjaroslav.mcingametester.client;

import com.github.mjaroslav.mcingametester.common.CommonProxy;
import com.github.mjaroslav.mcingametester.loader.Test;
import com.github.mjaroslav.mcingametester.loader.TestContainer;
import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public final class ClientProxy extends CommonProxy {
    private ProgressBar currentBar;

    @Override
    public void startLogTest(@NotNull TestContainer container) {
        super.startLogTest(container);
        currentBar = ProgressManager.push("Tests@" + current.getContainerName(), current.getTestCount());
    }

    @Override
    public void stepLogTest(@NotNull Test test) {
        super.stepLogTest(test);
        currentBar.step(test.getName());
    }

    @Override
    public void endLogTest() {
        super.endLogTest();
        ProgressManager.pop(currentBar);
        currentBar = null;
    }
}
