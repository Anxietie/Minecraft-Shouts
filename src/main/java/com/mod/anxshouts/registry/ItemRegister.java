package com.mod.anxshouts.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class ItemRegister {
    public static final Collection<ItemStack> WORDS = new ArrayList<>();

    // public static final Item WORD_OF_POWER_A = new BlockItem(BlockRegister.WORD_OF_POWER_A, new Item.Settings());

    private static final ItemGroup WORDS_OF_POWER = FabricItemGroup.builder()
            .icon(() -> new ItemStack(BlockRegister.WORD_OF_POWER_A.asItem()))
            .displayName(Text.literal("Words of Power"))
            .entries(((displayContext, entries) -> entries.addAll(WORDS)))
            .build();

    public static void registerItems() {
        BlockRegister.WORDS.forEach(block -> register(Registries.BLOCK.getId(block).getPath(), new BlockItem(block, new Item.Settings())));
    }

    private static Item register(String id, Item item) {
        WORDS.add(item.getDefaultStack());
        return Registry.register(Registries.ITEM, new Identifier(MODID, id), item);
    }

    public static void registerItemGroups() {
        register("words_of_power", WORDS_OF_POWER);
    }

    private static ItemGroup register(String id, ItemGroup itemGroup) {
        return Registry.register(Registries.ITEM_GROUP, new Identifier(MODID, id), itemGroup);
    }
}
