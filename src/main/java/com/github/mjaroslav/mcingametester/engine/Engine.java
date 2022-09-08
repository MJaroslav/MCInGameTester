package com.github.mjaroslav.mcingametester.engine;

import com.github.mjaroslav.mcingametester.api.Client;
import com.github.mjaroslav.mcingametester.api.Common;
import com.github.mjaroslav.mcingametester.api.Server;
import com.github.mjaroslav.mcingametester.util.Utils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import cpw.mods.fml.common.event.FMLStateEvent;
import lombok.Getter;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.github.mjaroslav.mcingametester.lib.ModInfo.*;

public final class Engine {
    public static final Engine INSTANCE = new Engine(); // Prevention from double initialization

    public final @NotNull Runner runner = new Runner();

    @Getter
    private LoaderState currentState, gameStopState;

    private final Map<LoaderState, Set<ASMData>> candidates = new HashMap<>();

    private Engine() {
        LOG.info("Setting up MCInGameTester");
        if (FMLCommonHandler.instance().getSide().isClient()) LOG.info("Client side environment!");
        else LOG.info("Server side environment!");
    }

    public void findCandidates(@NotNull ASMDataTable table) {
        table.getAll(Common.class.getName()).forEach(this::addTestCandidate);
        val commonCount = getCandidatesCount();
        LOG.info("Found " + commonCount + " common test container candidates");

        if (FMLCommonHandler.instance().getSide().isClient()) {
            table.getAll(Client.class.getName()).forEach(this::addTestCandidate);
            LOG.info("Found " + (getCandidatesCount() - commonCount) + " client test container candidates");
        } else {
            table.getAll(Server.class.getName()).forEach(this::addTestCandidate);
            LOG.info("Found " + (getCandidatesCount() - commonCount) + " server test container candidates");
        }
        gameStopState = candidates.keySet().stream().max(Comparator.comparingInt(Enum::ordinal))
                .orElse(LoaderState.AVAILABLE);
        val forced = Config.getForcedGameStopState();
        if (forced != null) gameStopState = forced;
        LOG.info("All tests ends on " + gameStopState + " state, game will stopped after this state!");
        LOG.info("MCInGameTester ready");
    }

    public void onFMLStateEvent(@NotNull FMLStateEvent event) {
        currentState = Utils.getLoaderStateFromEventClass(event.getClass());
        val currentCandidates = this.candidates.get(currentState);
        if (currentCandidates != null)
            for (var candidate : currentCandidates) {
                TestContainer container;
                try {
                    LOG.info("Compiling test candidate " + candidate.getClassName());
                    container = Parser.parseFromClassName(candidate.getClassName());
                    runner.runTestsFromContainer(container);
                } catch (Exception e) {
                    LOG.error("Error while loading and/or running test container " + candidate.getClassName(), e);
                    Utils.stopTheGame(false);
                    return;
                }
            }
        if (gameStopState != null)
            if (gameStopState.equals(currentState)) {
                LOG.info("Congratulations, all tests ended success");
                if (Config.isShouldStopAfterSuccess()) {
                    LOG.info("Game will be stopped now, you can prevent this by running with -D" +
                            PROP_STOP_AFTER_SUCCESS + "=false JVM argument or " + ENV_STOP_AFTER_SUCCESS +
                            "=false environment variable");
                    Utils.stopTheGame();
                } else LOG.warn("You run tests with -D" +
                        PROP_STOP_AFTER_SUCCESS + "=false JVM argument (or " + ENV_STOP_AFTER_SUCCESS +
                        "=false environment variable), game not will stopped");
            }
    }

    private int getCandidatesCount() {
        return candidates.values().stream().mapToInt(Set::size).sum();
    }

    private void addTestCandidate(@NotNull ASMData data) {
        val state = data.getAnnotationInfo().containsKey("when") ?
                Utils.getEnumFromHolderValue(LoaderState.class, (EnumHolder) data.getAnnotationInfo().get("when")) :
                LoaderState.AVAILABLE;
        if (!candidates.containsKey(state)) candidates.put(state, new HashSet<>());
        candidates.get(state).add(data);
    }
}
