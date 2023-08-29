package com.mod.anxshouts.registry;

import com.mod.anxshouts.block.WordBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class BlockRegister {
    public static final Collection<Block> WORDS = new ArrayList<>();

    public static final Block WORD_OF_POWER_A = new WordBlock(FabricBlockSettings.create()
            .breakInstantly()
            .dropsNothing()
            .noCollision()
            .nonOpaque()
            .replaceable()
            .pistonBehavior(PistonBehavior.DESTROY)
    );
    public static final Block WORD_OF_POWER_B = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_D = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_E = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_F = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_G = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_H = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_I = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_J = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_K = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_L = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_M = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_N = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_O = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_P = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_Q = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_R = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_S = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_T = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_U = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_V = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_W = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_X = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_Y = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_Z = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_AA = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_AH = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_EI = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_EY = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_II = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_IR = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_OO = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_UU = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));
    public static final Block WORD_OF_POWER_UR = new WordBlock(FabricBlockSettings.copyOf(WORD_OF_POWER_A));

    public static void registerBlocks() {
        register("word_of_power_a", WORD_OF_POWER_A);
        register("word_of_power_b", WORD_OF_POWER_B);
        register("word_of_power_d", WORD_OF_POWER_D);
        register("word_of_power_e", WORD_OF_POWER_E);
        register("word_of_power_f", WORD_OF_POWER_F);
        register("word_of_power_g", WORD_OF_POWER_G);
        register("word_of_power_h", WORD_OF_POWER_H);
        register("word_of_power_i", WORD_OF_POWER_I);
        register("word_of_power_j", WORD_OF_POWER_J);
        register("word_of_power_k", WORD_OF_POWER_K);
        register("word_of_power_l", WORD_OF_POWER_L);
        register("word_of_power_m", WORD_OF_POWER_M);
        register("word_of_power_n", WORD_OF_POWER_N);
        register("word_of_power_o", WORD_OF_POWER_O);
        register("word_of_power_p", WORD_OF_POWER_P);
        register("word_of_power_q", WORD_OF_POWER_Q);
        register("word_of_power_r", WORD_OF_POWER_R);
        register("word_of_power_s", WORD_OF_POWER_S);
        register("word_of_power_t", WORD_OF_POWER_T);
        register("word_of_power_u", WORD_OF_POWER_U);
        register("word_of_power_v", WORD_OF_POWER_V);
        register("word_of_power_w", WORD_OF_POWER_W);
        register("word_of_power_x", WORD_OF_POWER_X);
        register("word_of_power_y", WORD_OF_POWER_Y);
        register("word_of_power_z", WORD_OF_POWER_Z);
        register("word_of_power_aa", WORD_OF_POWER_AA);
        register("word_of_power_ah", WORD_OF_POWER_AH);
        register("word_of_power_ei", WORD_OF_POWER_EI);
        register("word_of_power_ey", WORD_OF_POWER_EY);
        register("word_of_power_ii", WORD_OF_POWER_II);
        register("word_of_power_ir", WORD_OF_POWER_IR);
        register("word_of_power_oo", WORD_OF_POWER_OO);
        register("word_of_power_uu", WORD_OF_POWER_UU);
        register("word_of_power_ur", WORD_OF_POWER_UR);
    }

    private static Block register(String id, Block block) {
        WORDS.add(block);
        return Registry.register(Registries.BLOCK, new Identifier(MODID, id), block);
    }
}
