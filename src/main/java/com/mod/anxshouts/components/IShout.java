package com.mod.anxshouts.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public interface IShout extends ComponentV3 {
    ComponentKey<IShout> KEY = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier(MODID, "shouts"), IShout.class);

    int getSelectedShout();
    void setSelectedShout(int shoutOrdinal);
    int[] getUnlockedShouts();
    void unlockShout(int shoutOrdinal);
    void unlockAllShouts();
    void lockShout(int shoutOrdinal);
    void lockAllShouts();
    int[] getObtainedShouts();
    void obtainShout(int shoutOrdinal);
    void obtainAllShouts();
    void removeShout(int shoutOrdinal);
    void removeAllShouts();
    boolean hasObtainedShout(int shoutOrdinal);
    boolean hasUnlockedShout(int shoutOrdinal);
    int getShoutCooldown();
    void setShoutCooldown(int ticks);
    default void resetShoutCooldown() { setShoutCooldown(0); }
    void decrementShoutCooldown();
}

