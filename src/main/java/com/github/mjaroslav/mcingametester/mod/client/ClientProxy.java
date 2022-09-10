package com.github.mjaroslav.mcingametester.mod.client;

import com.github.mjaroslav.mcingametester.engine.TestContainer;
import com.github.mjaroslav.mcingametester.engine.TestTask;
import com.github.mjaroslav.mcingametester.mod.common.CommonProxy;
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
    public void stepLogTest(@NotNull TestTask testTask) {
        super.stepLogTest(testTask);
        currentBar.step(testTask.getName());
    }

    @Override
    public void endLogTest() {
        super.endLogTest();
        ProgressManager.pop(currentBar);
        currentBar = null;
    }
}
