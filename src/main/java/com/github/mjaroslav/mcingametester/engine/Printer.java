package com.github.mjaroslav.mcingametester.engine;

import com.github.mjaroslav.mcingametester.util.ComparisonError;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

// TODO: Make formatted output for tests
@UtilityClass
public class Printer {
    public void printResultsFromContainer(@NotNull TestContainer container) {
        for (var test : container.getTestTasks()) printResultFromTest(test);
    }

    public void printResultFromTest(@NotNull TestTask testTask) {
        if (testTask.getState() == TestState.FAIL) {
            val message = testTask + " FAILED";
            if (testTask.getResult() != null) {
                var throwable = testTask.getResult();
                if (throwable instanceof ComparisonError comparison) {
                    LOG.error(testTask + " FAILED");
                    LOG.error(comparison.getMessage());
                    LOG.error("Expected: " + comparison.getExpected());
                    LOG.error("Actual: " + comparison.getActual());
                    return;
                }
                // Unwrap AssertionError
                if (throwable instanceof AssertionError && throwable.getCause() != null)
                    throwable = throwable.getCause();
                // Unwrap InvocationTargetException
                if (throwable instanceof InvocationTargetException) throwable = throwable.getCause();
                // Simple AssertionError usually has message
                if (throwable instanceof AssertionError) LOG.error(throwable.getMessage());
                else LOG.error(message, throwable);
            } else LOG.error(message);
        } else LOG.info(testTask + " SUCCESS");
    }
}
