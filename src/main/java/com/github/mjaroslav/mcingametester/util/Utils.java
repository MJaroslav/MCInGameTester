package com.github.mjaroslav.mcingametester.util;

import com.github.mjaroslav.mcingametester.engine.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.discovery.asm.ModAnnotation.EnumHolder;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@UtilityClass
public class Utils {
    // WTF? Why EnumHolder exists and have private fields?
    public @NotNull <T extends Enum<T>> T getEnumFromHolderValue(@NotNull Class<T> enumClass,
                                                                 @NotNull EnumHolder holder) {
        return Enum.valueOf(enumClass, ReflectionHelper.getPrivateValue(EnumHolder.class, holder, "value"));
    }

    public @NotNull LoaderState getLoaderStateFromEventClass(@NotNull Class<? extends FMLStateEvent> clazz) {
        //noinspection OptionalGetWithoutIsPresent
        return Arrays.stream(LoaderState.values()).filter(state -> clazz.equals(getEventClassFromState(state)))
                .findFirst().get(); // Null is impossible
    }

    public @Nullable Class<? extends FMLStateEvent> getEventClassFromState(@NotNull LoaderState state) {
        return ReflectionHelper.getPrivateValue(LoaderState.class, state, "eventClass");
    }

    public void stopTheGame(@NotNull TestTask test) {
        Printer.printResultFromTest(test);
        stopTheGame(false);
    }

    public void stopTheGame() {
        for (var container : Engine.INSTANCE.runner.containers)
            Printer.printResultsFromContainer(container);
        stopTheGame(Engine.INSTANCE.runner.getState() == TestState.SUCCESS);
    }

    public void stopTheGame(boolean normal) {
        FMLCommonHandler.instance().exitJava(normal ? 0 : 1, Config.isShouldHaltExit());
    }
}
