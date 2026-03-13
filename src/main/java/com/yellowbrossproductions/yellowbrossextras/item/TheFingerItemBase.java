package com.yellowbrossproductions.yellowbrossextras.item;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.YextrasEntity;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TheFingerItemBase extends Item {
    Random random = new Random();

    public TheFingerItemBase() {
        super(new Properties().tab(YellowbrossExtras.YELLOWBROSSEXTRAS_GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("tooltip.yellowbrossextras.the_finger"));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        this.yeetMobs(player.getLevel(), 8.0D, player);
        return true;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack p_41398_, Player player, LivingEntity p_41400_, InteractionHand p_41401_) {
        this.yeetMobs(player.getLevel(), 8.0D, player);
        return super.interactLivingEntity(p_41398_, player, p_41400_, p_41401_);
    }

    @Override
    public boolean canAttackBlock(BlockState p_41441_, Level p_41442_, BlockPos p_41443_, Player p_41444_) {
        return false;
    }

    public void yeetMobs(Level level, double size, Entity attacker) {
        List<Entity> entities = level.getEntities(attacker, new AABB(attacker.getX() - size, attacker.getY() - size, attacker.getZ() - size, attacker.getX() + size, attacker.getY() + size, attacker.getZ() + size), predicate -> (predicate != attacker));
        Vec3 viewVec = attacker.getViewVector(1.0F);
        for (Entity hit : entities) {
            Vec3 vector2 = hit.position().vectorTo(attacker.position().normalize());
            if (vector2.dot(viewVec) < 0.0D) {
                attacker.playSound(YellowbrossExtrasSoundEvents.YEET.get(), 2.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                hit.setDeltaMovement(attacker.position().subtract(hit.position()).normalize().scale(-1.0));
            }
        }
    }
}
