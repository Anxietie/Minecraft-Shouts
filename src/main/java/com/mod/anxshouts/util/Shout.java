package com.mod.anxshouts.util;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.text.Text;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.NotNull;

public enum Shout implements StringIdentifiable {
	// most cooldowns are in line with the actual game
	NONE("none", 0, 0, null),
	FIRE("fire", 2, 100, ParticleTypes.FLAME),
	FROST("frost", 1, 100, ParticleTypes.SNOWFLAKE),
	FORCE("force", 2, 45, ParticleTypes.DRAGON_BREATH),
	AURA("aura", 1, 50, ParticleTypes.ENCHANT),
	STORM("storm", 2, 300, ParticleTypes.CRIT), // 300 instead of 600 since it isn't a constant downpour of lightning
	CLEAR("clear", 1, 15, null),
	DISARM("disarm", 2, 40, null),
	ETHEREAL("ethereal", 2, 40, ParticleTypes.EFFECT),
	DRAIN("drain", 1, 90,null),
	ICE("ice", 1, 120, ParticleTypes.SNOWFLAKE),
	SPRINT("sprint", 1, 35, ParticleTypes.FLASH),
	VALOR("valor", 2, 180, null),
	ASPECT("aspect", 3, 5, ParticleTypes.EFFECT), // dragon aspect has unique cooldown case
	REND("rend", 3, 120, ParticleTypes.DRAGON_BREATH); // from 15 to 120 because ender dragon is much weaker than skyrim dragon

	public static final Codec<Shout> CODEC;
	private final String id;
	private final Text name;
	private final int cost;
	private final int cooldown;
	private final ParticleEffect particle;

	Shout(String id, int cost, int cooldown /* in seconds */, ParticleEffect associatedParticle) {
		this.id = id;
		this.name = Text.translatable("anxshouts.shouts." + id);
		this.cost = cost;
		this.particle = associatedParticle;
		this.cooldown = cooldown;
	}

	@Override
	public String asString() {
		return this.id;
	}

	public Text getName() {
		return this.name;
	}

	@NotNull
	public String getId() { return this.id; }

	public int getCooldown() { return this.cooldown; }

	public static Shout fromOrdinal(int ordinal) {
		if (ordinal >= values().length || ordinal < 0) throw new IllegalArgumentException("Shout of index " + ordinal + " does not exist!");
		return values()[ordinal];
	}

	public ParticleEffect getParticleEffect() { return this.particle; }

	public int getCost() { return this.cost; }

	public static Shout byId(String id) {
		for (Shout shout : values())
			if (shout.getId().equals(id)) return shout;
		return NONE;
	}

	static {
		CODEC = StringIdentifiable.createCodec(Shout::values);
	}
}
