package com.github.mjaroslav.mcingametester.loader;

import com.github.mjaroslav.mcingametester.api.Client;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Server;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import lombok.Getter;
import lombok.val;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.LOG;

public final class TestLoader {
    @Getter
    private final @NotNull Side side;
    private final Map<LoaderState, Set<ASMData>> testCandidates = new HashMap<>();
    @Getter
    private @Nullable LoaderState gameStopState;
    @Getter
    private LoaderState currentState;

    public TestLoader() {
        LOG.info("Setting up MCInGameTester");
        side = FMLCommonHandler.instance().getSide();
        if (side.isClient())
            LOG.info("Client side environment!");
        else
            LOG.info("Server side environment!");
    }

    // I hope this table contains information from all modifications :)
    public void parseASMTable(@NotNull ASMDataTable table) {
        try {
            table.getAll(Common.class.getName()).forEach(this::registerTestClass);
            LOG.info("Found " + testCandidates.size() + " classes with common tests");
            val commonCount = testCandidates.size();
            if (side.isClient()) {
                table.getAll(Client.class.getName()).forEach(this::registerTestClass);
                LOG.info("Found " + (testCandidates.size() - commonCount) + " classes with client tests");
            } else {
                table.getAll(Server.class.getName()).forEach(this::registerTestClass);
                LOG.info("Found " + (testCandidates.size() - commonCount) + " classes with server tests");
            }
            gameStopState = testCandidates.keySet().stream().max(Comparator.comparingInt(Enum::ordinal)).orElse(null);
            if (gameStopState != null)
                LOG.info("All tests ends on " + gameStopState + " state, game will stopped after this state!");
            else LOG.info("No tests found, game not will be stopped after loading. It's strange...");
        } catch (Exception any) {
            throw new ReportedException(CrashReport.makeCrashReport(any, "Error while MCInGameTesting loading"));
        }
    }

    public void onFMLStateEvent(@NotNull FMLStateEvent event) {
        currentState = getLoaderStateFromEventClass(event.getClass());
        val tests = testCandidates.get(currentState);
        if (tests != null) {
            LOG.info("Running " + currentState + " tests...");
            tests.forEach(test -> {
                TestContainer container;
                try {
                    container = new TestContainer(test.getClassName());
                } catch (Exception any) {
                    throw new ReportedException(CrashReport.makeCrashReport(any, "Error while test construction"));
                }
                container.runTests();
            });
        }
        if (gameStopState != null)
            if (gameStopState.equals(currentState)) {
                LOG.info("Congratulations, all tests ended success, stopping the game...");
                FMLCommonHandler.instance().exitJava(0, true);
            }
    }

    private void registerTestClass(@NotNull ASMData data) {
        val state = parseLoaderStateFromAnnotationInfo(data);
        if (!testCandidates.containsKey(state))
            testCandidates.put(state, new HashSet<>());
        testCandidates.get(state).add(data);
    }

    // WTF? Why EnumHolder exists and have private fields?
    private @NotNull LoaderState parseLoaderStateFromAnnotationInfo(@NotNull ASMData data) {
        if (data.getAnnotationInfo().containsKey("value")) {
            val enumHolder = data.getAnnotationInfo().get("value");
            return LoaderState.valueOf(ReflectionHelper.getPrivateValue(EnumHolder.class,
                    (EnumHolder) enumHolder, "value"));
        } else return LoaderState.AVAILABLE; // Default from annotations
    }

    private @Nullable LoaderState getLoaderStateFromEventClass(@NotNull Class<? extends FMLStateEvent> clazz) {
        return Arrays.stream(LoaderState.values()).filter(state -> clazz.equals(getEventClassFromState(state)))
                .findFirst().orElse(null);
    }

    private @Nullable Class<? extends FMLStateEvent> getEventClassFromState(@NotNull LoaderState state) {
        return ReflectionHelper.getPrivateValue(LoaderState.class, state, "eventClass");
    }
}
