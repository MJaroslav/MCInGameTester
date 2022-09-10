package com.github.mjaroslav.mcingametester.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use it on non-static {@link net.minecraft.world.World World} typed field for replacing its
 * value to server Overworld (dim 0) object. Requires
 * {@link cpw.mods.fml.common.LoaderState#SERVER_STARTED SERVER_STARTED} state for working.
 * Its equivalent of field assigning to <code>MinecraftServer.getServer().worldServers[0]</code>
 *
 * @see Server
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WorldShadow {
}
