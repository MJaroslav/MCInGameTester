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
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Log4j2
public final class TestLoader {
    @Getter
    private final @NotNull Side side;
    private final Map<LoaderState, Set<ASMData>> testCandidates = new HashMap<>();
    @Getter
    private @Nullable LoaderState gameStopState;

    public TestLoader() {
        side = FMLCommonHandler.instance().getSide();
    }

    // I hope this table contains information from all modifications :)
    public void parseASMTable(@NotNull ASMDataTable table) {
        table.getAll(Common.class.getName()).forEach(this::registerTestClass);
        if (side.isClient())
            table.getAll(Client.class.getName()).forEach(this::registerTestClass);
        else
            table.getAll(Server.class.getName()).forEach(this::registerTestClass);
        gameStopState = testCandidates.keySet().stream().max(Comparator.comparingInt(Enum::ordinal)).orElse(null);
        System.out.println(gameStopState);
    }

    public void onFMLStateEvent(@NotNull FMLStateEvent event) {
        val eventState = getLoaderStateFromEventClass(event.getClass());
        if (gameStopState != null)
            if (gameStopState.equals(eventState))
                FMLCommonHandler.instance().exitJava(0, true);
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
