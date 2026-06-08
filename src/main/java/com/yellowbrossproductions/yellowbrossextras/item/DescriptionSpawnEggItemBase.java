package com.yellowbrossproductions.yellowbrossextras.item;

import com.yellowbrossproductions.yellowbrossextras.init.YEItemsAndBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeSpawnEggItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class DescriptionSpawnEggItemBase extends ForgeSpawnEggItem {

    public DescriptionSpawnEggItemBase(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties props) {
        super(type, backgroundColor, highlightColor, props);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (stack.getItem() == YEItemsAndBlocks.AIMBOT_SPAWN_EGG.get().asItem()) tooltip.add(Component.translatable("tooltip.yellowbrossextras.aimbot_spawn_egg"));
    }
}
