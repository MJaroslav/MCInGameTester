package com.github.mjaroslav.mcingametester.loader;

import com.github.mjaroslav.mcingametester.MCInGameTester;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ReportedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.*;

// TODO: Write abstractions for using customs test workers
@Getter
public class TestLoader {
    protected final @NotNull Side side;
    @Getter(AccessLevel.NONE)
    protected final Map<LoaderState, Set<ASMData>> containerCandidates = new HashMap<>();
    protected LoaderState gameStopState;
    protected LoaderState currentState;

    protected final @NotNull TestRunner runner;

    public TestLoader() {
        LOG.info("Setting up MCInGameTester");
        side = FMLCommonHandler.instance().getSide();
        if (side.isClient())
            LOG.info("Client side environment!");
        else
            LOG.info("Server side environment!");
        runner = createRunner();
    }

    protected @NotNull TestRunner createRunner() {
        return new TestRunner();
    }

    // I hope this table contains information from all modifications :)
    public void parseASMTable(@NotNull ASMDataTable table) {
        try {
            table.getAll(Common.class.getName()).forEach(this::registerTestClass);
            LOG.info("Found " + containerCandidates.size() + " classes with common tests");
            val commonCount = containerCandidates.size();
            if (side.isClient()) {
                table.getAll(Client.class.getName()).forEach(this::registerTestClass);
                LOG.info("Found " + (containerCandidates.size() - commonCount) + " classes with client tests");
            } else {
                table.getAll(Server.class.getName()).forEach(this::registerTestClass);
                LOG.info("Found " + (containerCandidates.size() - commonCount) + " classes with server tests");
            }
            gameStopState = containerCandidates.keySet().stream().max(Comparator.comparingInt(Enum::ordinal))
                    .orElse(LoaderState.AVAILABLE);
            val forced = Config.getForcedGameStopState();
            if (forced != null) gameStopState = forced;
            LOG.info("All tests ends on " + gameStopState + " state, game will stopped after this state!");
        } catch (Exception any) {
            throw new ReportedException(CrashReport.makeCrashReport(any, "Error while MCInGameTesting loading"));
        }
    }

    public void onFMLStateEvent(@NotNull FMLStateEvent event) {
        currentState = getLoaderStateFromEventClass(event.getClass());
        val candidates = containerCandidates.get(currentState);
        if (candidates != null) {
            LOG.info("Running " + currentState + " tests...");
            for (var candidate : candidates) {
                TestContainer container;
                try {
                    container = TestFactory.buildTestContainerFromClassName(candidate.getClassName());
                    runner.runTestsFromContainer(container);
                } catch (Exception e) {
                    LOG.error("Error while loading and/or running test container " + candidate.getClassName(), e);
                    MCInGameTester.stopTheGame(false);
                }
            }
        }
        if (gameStopState != null)
            if (gameStopState.equals(currentState)) {
                LOG.info("Congratulations, all tests ended success");
                if (Config.isShouldStopAfterSuccess()) {
                    LOG.info("Game will be stopped now, you can prevent this by running with -D" +
                            PROP_STOP_AFTER_SUCCESS + "=false JVM argument or " + ENV_STOP_AFTER_SUCCESS +
                            "=false environment variable");
                    FMLCommonHandler.instance().exitJava(0, true);
                } else LOG.warn("You run tests with -D" +
                        PROP_STOP_AFTER_SUCCESS + "=false JVM argument (or " + ENV_STOP_AFTER_SUCCESS +
                        "=false environment variable), game not will stopped");
            }
    }

    private void registerTestClass(@NotNull ASMData data) {
        val state = parseLoaderStateFromAnnotationInfo(data);
        if (!containerCandidates.containsKey(state))
            containerCandidates.put(state, new HashSet<>());
        containerCandidates.get(state).add(data);
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
