package com.mod.anxshouts.registry;

import com.mod.anxshouts.block.WordBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class BlockRegister {
    public static final Block WORD_BLOCK = new WordBlock(FabricBlockSettings.create()
            .breakInstantly()
            .dropsNothing()
            .noCollision()
            .nonOpaque()
            .replaceable()
            .pistonBehavior(PistonBehavior.DESTROY)
    );

    public static void registerBlocks() {
        register("word_block", WORD_BLOCK);
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, new Identifier(MODID, id), block);
    }
}
