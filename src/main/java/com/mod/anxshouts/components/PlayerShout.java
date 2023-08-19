package com.mod.anxshouts.components;

import com.mod.anxshouts.client.util.ShoutHandler;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerShout implements IShout, ServerTickingComponent, AutoSyncedComponent {
    protected int selectedShout;
    protected int[] unlockedShouts;
    protected int[] obtainedShouts;
    protected int shoutCooldown;
    private final Object provider;

    public PlayerShout(PlayerEntity provider) {
        this.provider = provider;
        this.selectedShout = 0;
        this.unlockedShouts = new int[ShoutHandler.Shout.values().length];;
        this.obtainedShouts = new int[ShoutHandler.Shout.values().length];;
        this.shoutCooldown = 0;
    }

    @Override
    public int getSelectedShout() { return this.selectedShout; }
    @Override
    public void setSelectedShout(int shoutOrdinal) {
        this.selectedShout = shoutOrdinal;
        IShout.KEY.sync(this.provider);
    }
    @Override
    public int[] getUnlockedShouts() { return this.unlockedShouts; }
    @Override
    public void unlockShout(int shoutOrdinal) {
        this.unlockedShouts[shoutOrdinal] = 1;
        IShout.KEY.sync(this.provider);
    }
    @Override
    public int[] getObtainedShouts() { return this.obtainedShouts; }
    @Override
    public void obtainShout(int shoutOrdinal) {
        this.obtainedShouts[shoutOrdinal] = 1;
        IShout.KEY.sync(this.provider);
    }
    @Override
    public void removeShout(int shoutOrdinal) {
        this.obtainedShouts[shoutOrdinal] = 0;
        IShout.KEY.sync(this.provider);
    }
    @Override
    public boolean hasShout(int shoutOrdinal) { return this.obtainedShouts[shoutOrdinal] == 1; }
    @Override
    public int getShoutCooldown() { return this.shoutCooldown; }
    @Override
    public void setShoutCooldown(int ticks) {
        this.shoutCooldown = ticks;
        IShout.KEY.sync(this.provider);
    }
    @Override
    public void decrementShoutCooldown() {
        --this.shoutCooldown;
        IShout.KEY.sync(this.provider);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.selectedShout = tag.getInt("SelectedShout");
        this.unlockedShouts = tag.getIntArray("UnlockedShouts");
        this.obtainedShouts = tag.getIntArray("ObtainedShouts");
        this.shoutCooldown = tag.getInt("ShoutCooldown");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putInt("SelectedShout", this.selectedShout);
        tag.putIntArray("UnlockedShouts", this.unlockedShouts);
        tag.putIntArray("ObtainedShouts", this.obtainedShouts);
        tag.putInt("ShoutCooldown", this.shoutCooldown);
    }

    @Override
    public void serverTick() {
        if (this.getShoutCooldown() > 0)
            this.decrementShoutCooldown();
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        // only synchronize the information you need!
        buf.writeVarInt(this.selectedShout);
        buf.writeIntArray(this.unlockedShouts);
        buf.writeIntArray(this.obtainedShouts);
        buf.writeInt(this.shoutCooldown);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.selectedShout = buf.readInt();
        this.unlockedShouts = buf.readIntArray();
        this.obtainedShouts = buf.readIntArray();
        this.shoutCooldown = buf.readInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.provider; // only sync with the provider itself
    }
}
