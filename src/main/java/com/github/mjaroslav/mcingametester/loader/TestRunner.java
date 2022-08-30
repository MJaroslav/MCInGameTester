package com.github.mjaroslav.mcingametester.loader;

import com.github.mjaroslav.mcingametester.MCInGameTester;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

public class TestRunner {
    protected Set<TestContainer> containers = new HashSet<>();

    public TestRunner() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Test results:");
            for (var container : containers)
                TestPrinter.printResultsFromContainer(container);
        }));
    }

    public void runTestsFromContainer(@NotNull TestContainer container) throws InvocationTargetException,
            IllegalAccessException {
        MCInGameTester.proxy.startLogTest(container);
        containers.add(container);
        if (container.getBeforeClassMethod() != null)
            container.getBeforeClassMethod().invoke(null);
        for (var test : container.getTests()) {
            try {
                MCInGameTester.proxy.stepLogTest(test);
                runTest(container, test);
                test.setResult(TestState.SUCCESS, null);
            } catch (Throwable e) {
                if (e instanceof TestException fail) {
                    test.setResult(TestState.FAIL, fail.getCause());
                    if (Config.isShouldStopOnFirstFail()) {
                        TestPrinter.printResultFromTest(test);
                        MCInGameTester.stopTheGame(false);
                    }
                } else {
                    LOG.error("Error while running " + test.toString(), e);
                    MCInGameTester.stopTheGame(false);
                }
            }
        }
        if (container.getAfterClassMethod() != null)
            container.getAfterClassMethod().invoke(null);
        MCInGameTester.proxy.endLogTest();
    }

    public void runTest(@NotNull TestContainer container, @NotNull Test test) throws Throwable {
        if (container.getBeforeEachMethod() != null)
            container.getBeforeEachMethod().invoke(container.getObject());
        try {
            test.getTestMethod().invoke(test.getTestObject());
        } catch (InvocationTargetException e) {
            if (!e.getCause().getClass().equals(test.getExpectedException())) // Ignore expected
                if (e.getCause() instanceof AssertionError) throw new TestException(e.getCause()); // Fail test
                else throw e.getCause(); // Error while testing
        }
        if (container.getAfterEachMethod() != null)
            container.getAfterEachMethod().invoke(container.getObject());
    }
}
