package com.mod.anxshouts.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.util.Identifier;

import java.util.UUID;

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
    int getSoulCount();
    void incrementSoulCount();
    void setSoulCount(int souls);
    void decrementSoulCount();
    void setEtherealTicks(int ticks);
    void decrementEtherealTicks();
    boolean isEthereal();
    void setValorTicks(int ticks);
    int getValorTicks();
    int decrementValorTicks();
    boolean hasActiveValor();
    void setValorUUID(UUID uuid);
    UUID getValorUUID();
    boolean hasActiveDA(); // DA = dragon aspect
    int getDATicks();
    void setDATicks(int ticks);
    int decrementDATicks();
    int getDACooldown();
    void setDACooldown(int ticks);
    void decrementDACooldown();
    boolean companionSummoned();
    UUID getCompanionUUID();
    void setCompanionUUID(UUID uuid);
}

