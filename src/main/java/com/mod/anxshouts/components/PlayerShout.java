package com.mod.anxshouts.components;

import com.mod.anxshouts.client.util.ShoutHandler;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.UUID;

public class PlayerShout implements IShout, AutoSyncedComponent {
    protected int selectedShout;
    protected int[] unlockedShouts;
    protected int[] obtainedShouts;
    protected int shoutCooldown;
    protected int souls;
    protected int etherealTicks;
    protected int valorTicks;
    protected UUID valorUUID = null;
    private final Object provider;

    public PlayerShout(PlayerEntity provider) {
        this.provider = provider;
        this.selectedShout = 0;
        this.unlockedShouts = new int[ShoutHandler.Shout.values().length];
        this.obtainedShouts = new int[ShoutHandler.Shout.values().length];
        this.shoutCooldown = 0;
        this.souls = 0;
        this.etherealTicks = 0;
        this.valorTicks = 0;
    }

    @Override
    public int getSelectedShout() { return this.selectedShout; }

    @Override
    public void setSelectedShout(int shoutOrdinal) {
        this.selectedShout = shoutOrdinal;
        obtainShout(shoutOrdinal);
    }

    @Override
    public int[] getUnlockedShouts() { return this.unlockedShouts; }

    @Override
    public void unlockShout(int shoutOrdinal) {
        this.unlockedShouts[shoutOrdinal] = 1;
        sync();
    }

    @Override
    public void unlockAllShouts() {
        Arrays.fill(this.unlockedShouts, 1);
        sync();
    }

    @Override
    public void lockShout(int shoutOrdinal) {
        this.unlockedShouts[shoutOrdinal] = 0;
        removeShout(shoutOrdinal);
    }

    @Override
    public void lockAllShouts() {
        this.unlockedShouts = new int[ShoutHandler.Shout.values().length];
        removeAllShouts();
    }

    @Override
    public int[] getObtainedShouts() { return this.obtainedShouts; }

    @Override
    public void obtainShout(int shoutOrdinal) {
        this.obtainedShouts[shoutOrdinal] = 1;
        unlockShout(shoutOrdinal);
    }

    public void obtainAllShouts() {
        Arrays.fill(this.obtainedShouts, 1);
        unlockAllShouts();
    }

    @Override
    public void removeShout(int shoutOrdinal) {
        if (this.selectedShout == shoutOrdinal)
            this.selectedShout = 0;
        this.obtainedShouts[shoutOrdinal] = 0;
        sync();
    }

    @Override
    public void removeAllShouts() {
        this.obtainedShouts = new int[ShoutHandler.Shout.values().length];
        this.selectedShout = 0;
        sync();
    }

    @Override
    public boolean hasObtainedShout(int shoutOrdinal) {
        return this.obtainedShouts[shoutOrdinal] == 1;
    }

    @Override
    public boolean hasUnlockedShout(int shoutOrdinal) {
        return this.unlockedShouts[shoutOrdinal] == 1;
    }

    @Override
    public int getShoutCooldown() { return this.shoutCooldown; }

    @Override
    public void setShoutCooldown(int ticks) {
        this.shoutCooldown = ticks;
        sync();
    }

    @Override
    public void decrementShoutCooldown() {
        setShoutCooldown(this.shoutCooldown - 1);
    }

    @Override
    public int getSoulCount() { return this.souls; }

    @Override
    public void incrementSoulCount() {
        setSoulCount(this.souls + 1);
    }

    @Override
    public void setSoulCount(int souls) {
        this.souls = souls;
        sync();
    }

    @Override
    public void decrementSoulCount() {
        setSoulCount(this.souls - 1);
    }

    @Override
    public void setEtherealTicks(int ticks) {
        this.etherealTicks = ticks;
        sync();
    }

    @Override
    public void decrementEtherealTicks() {
        setEtherealTicks(this.etherealTicks - 1);
    }

    @Override
    public boolean isEthereal() {
        return this.etherealTicks > 0;
    }

    @Override
    public void setValorTicks(int ticks) {
        this.valorTicks = ticks;
        sync();
    }

    @Override
    public int getValorTicks() {
        return this.valorTicks;
    }

    @Override
    public int decrementValorTicks() {
        setValorTicks(this.valorTicks - 1);
        return this.valorTicks;
    }

    @Override
    public boolean hasActiveValor() {
        return this.valorTicks > 0;
    }

    @Override
    public void setValorUUID(UUID uuid) {
        this.valorUUID = uuid;
    }

    @Override
    public UUID getValorUUID() {
        return this.valorUUID;
    }

    @Override
    public void readFromNbt(NbtCompound nbt) {
        this.selectedShout = nbt.getInt("SelectedShout");
        this.unlockedShouts = nbt.getIntArray("UnlockedShouts");
        this.obtainedShouts = nbt.getIntArray("ObtainedShouts");
        this.shoutCooldown = nbt.getInt("ShoutCooldown");
        this.souls = nbt.getInt("SoulCount");
        this.etherealTicks = nbt.getInt("EtherealTicks");
        this.valorTicks = nbt.getInt("ValorTicks");
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.putInt("SelectedShout", this.selectedShout);
        nbt.putIntArray("UnlockedShouts", this.unlockedShouts);
        nbt.putIntArray("ObtainedShouts", this.obtainedShouts);
        nbt.putInt("ShoutCooldown", this.shoutCooldown);
        nbt.putInt("SoulCount", this.souls);
        nbt.putInt("EtherealTicks", this.etherealTicks);
        nbt.putInt("ValorTicks", this.valorTicks);
    }

    @Override
    public void writeSyncPacket(PacketByteBuf buf, ServerPlayerEntity player) {
        // only synchronize the information you need!
        buf.writeInt(this.selectedShout);
        buf.writeIntArray(this.unlockedShouts);
        buf.writeIntArray(this.obtainedShouts);
        buf.writeInt(this.shoutCooldown);
        buf.writeInt(this.souls);
        buf.writeInt(this.etherealTicks);
        buf.writeInt(this.valorTicks);
    }

    @Override
    public void applySyncPacket(PacketByteBuf buf) {
        this.selectedShout = buf.readInt();
        this.unlockedShouts = buf.readIntArray();
        this.obtainedShouts = buf.readIntArray();
        this.shoutCooldown = buf.readInt();
        this.souls = buf.readInt();
        this.etherealTicks = buf.readInt();
        this.valorTicks = buf.readInt();
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.provider; // only sync with the provider itself
    }

    private void sync() { IShout.KEY.sync(this.provider); }
}
