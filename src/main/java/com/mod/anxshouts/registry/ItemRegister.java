package com.mod.anxshouts.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class ItemRegister {
    public static final Item WORD_BLOCK_ITEM = new BlockItem(BlockRegister.WORD_BLOCK, new Item.Settings());

    public static void registerItems() {
        register("word_block_item", WORD_BLOCK_ITEM);
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(MODID, id), item);
    }
}
