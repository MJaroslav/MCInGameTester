package com.github.mjaroslav.mcingametester.loader;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

// TODO: Make formatted output for tests
@UtilityClass
public class TestPrinter {
    public void printResultsFromContainer(@NotNull TestContainer container) {
        for (var test : container.getTests()) printResultFromTest(test);
    }

    public void printResultFromTest(@NotNull Test test) {
        LOG.info("\n\n\n");
        if (test.getState() == TestState.FAIL) LOG.error(test + " FAILED", test.getResult());
        else LOG.info(test + " SUCCESS");
        LOG.info("\n\n\n");
    }
}
