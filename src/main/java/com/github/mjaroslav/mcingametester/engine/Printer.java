package com.github.mjaroslav.mcingametester.engine;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

// TODO: Make formatted output for tests
@UtilityClass
public class Printer {
    public void printResultsFromContainer(@NotNull TestContainer container) {
        for (var test : container.getTestTasks()) printResultFromTest(test);
    }

    public void printResultFromTest(@NotNull TestTask testTask) {
        if (testTask.getState() == TestState.FAIL) LOG.error(testTask + " FAILED\n" + testTask.getResult());
        else LOG.info(testTask + " SUCCESS");
    }
}
