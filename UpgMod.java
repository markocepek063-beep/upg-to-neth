package com.upg;

import java.util.Map;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * /upg: upgrades the diamond armor and tools you have equipped (worn armor,
 * main hand, off hand) to netherite. Enchantments, durability damage, armor
 * trims, custom names, etc. are all kept, exactly like a smithing table.
 */
public class UpgMod implements ModInitializer {

    private static final Map<Item, Item> UPGRADES = Map.ofEntries(
            Map.entry(Items.DIAMOND_HELMET, Items.NETHERITE_HELMET),
            Map.entry(Items.DIAMOND_CHESTPLATE, Items.NETHERITE_CHESTPLATE),
            Map.entry(Items.DIAMOND_LEGGINGS, Items.NETHERITE_LEGGINGS),
            Map.entry(Items.DIAMOND_BOOTS, Items.NETHERITE_BOOTS),
            Map.entry(Items.DIAMOND_SWORD, Items.NETHERITE_SWORD),
            Map.entry(Items.DIAMOND_PICKAXE, Items.NETHERITE_PICKAXE),
            Map.entry(Items.DIAMOND_AXE, Items.NETHERITE_AXE),
            Map.entry(Items.DIAMOND_SHOVEL, Items.NETHERITE_SHOVEL),
            Map.entry(Items.DIAMOND_HOE, Items.NETHERITE_HOE),
            Map.entry(Items.DIAMOND_SPEAR, Items.NETHERITE_SPEAR)
    );

    private static final EquipmentSlot[] SLOTS = {
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET,
            EquipmentSlot.MAINHAND,
            EquipmentSlot.OFFHAND
    };

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(Commands.literal("upg").executes(ctx -> upgrade(ctx.getSource()))));
    }

    private static int upgrade(CommandSourceStack source) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        int upgraded = 0;

        for (EquipmentSlot slot : SLOTS) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty()) {
                continue;
            }
            Item target = UPGRADES.get(stack.getItem());
            if (target == null) {
                continue;
            }
            // Copies the stack's component changes (enchantments, damage, trim, name, ...)
            // onto a fresh netherite stack, the same way the smithing table does it.
            player.setItemSlot(slot, stack.transmuteCopy(target, stack.getCount()));
            upgraded++;
        }

        if (upgraded == 0) {
            source.sendFailure(Component.literal("You have no diamond armor or tools equipped."));
            return 0;
        }

        final int count = upgraded;
        source.sendSuccess(() -> Component.literal(
                "Upgraded " + count + " item" + (count == 1 ? "" : "s") + " to netherite."), false);
        return count;
    }
}
