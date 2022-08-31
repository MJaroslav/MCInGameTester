package com.github.mjaroslav.mcingametester.mod.common;

import com.github.mjaroslav.mcingametester.engine.TestTask;
import com.github.mjaroslav.mcingametester.engine.TestContainer;
import org.jetbrains.annotations.NotNull;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

public class CommonProxy {
    protected TestContainer current;
    protected int tested;

    public void startLogTest(@NotNull TestContainer container) {
        current = container;
        tested = 1;
        LOG.info("Testing of " + current.getContainerName() + " that contains " + current.getTestCount() + " tests...");
    }

    public void stepLogTest(@NotNull TestTask testTask) {
        LOG.info("Run test " + testTask.getName() + " (" + tested++ + "/" + current.getTestCount() + ")...");
    }

    public void endLogTest() {
        LOG.info("Testing of " + current.getContainerName() + " ended successful!");
        current = null;
        tested = 0;
    }
}
