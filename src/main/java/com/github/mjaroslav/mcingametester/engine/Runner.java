package com.github.mjaroslav.mcingametester.engine;

import com.github.mjaroslav.mcingametester.mod.MCInGameTester;
import com.github.mjaroslav.mcingametester.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

public final class Runner {
    public final Set<TestContainer> containers = new HashSet<>();

    public void runTestsFromContainer(@NotNull TestContainer container) throws InvocationTargetException,
            IllegalAccessException {
        MCInGameTester.proxy.startLogTest(container);
        containers.add(container);
        if (container.getBeforeClassMethod() != null)
            container.getBeforeClassMethod().invoke(null);
        for (var test : container.getTestTasks()) {
            try {
                MCInGameTester.proxy.stepLogTest(test);
                runTest(container, test);
                test.setResult(TestState.SUCCESS, null);
            } catch (Throwable e) {
                if (e instanceof AssertionError fail) {
                    test.setResult(TestState.FAIL, fail);
                    if (Config.isShouldStopOnFirstFail()) {
                        Utils.stopTheGame(test);
                        return;
                    }
                } else {
                    LOG.error("Error while running " + test.toString(), e);
                    Utils.stopTheGame(false);
                    return;
                }
            }
        }
        if (container.getAfterClassMethod() != null)
            container.getAfterClassMethod().invoke(null);
        MCInGameTester.proxy.endLogTest();
    }

    public void runTest(@NotNull TestContainer container, @NotNull TestTask testTask) throws Throwable {
        if (container.getBeforeEachMethod() != null)
            container.getBeforeEachMethod().invoke(container.getObject());
        try {
            testTask.getTestMethod().invoke(testTask.getTestObject());
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof AssertionError)
                throw e.getCause();
            else if (!e.getCause().getClass().equals(testTask.getExpectedException()))
                throw new AssertionError("Unexpected exception: " + e, e);
        }
        if (container.getAfterEachMethod() != null)
            container.getAfterEachMethod().invoke(container.getObject());
    }

    public @NotNull TestState getState() {
        return containers.stream().map(TestContainer::getState).max(Comparator.comparingInt(Enum::ordinal))
                .orElse(TestState.SUCCESS);
    }
}
