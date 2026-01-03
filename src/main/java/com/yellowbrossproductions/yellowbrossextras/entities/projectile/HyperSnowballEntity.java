package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.AmoebicDevourerEntity;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class HyperSnowballEntity extends ThrowableItemProjectile {
    public HyperSnowballEntity(EntityType<? extends ThrowableItemProjectile> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public HyperSnowballEntity(Level p_37399_, LivingEntity p_37400_) {
        super(ModEntityTypes.HyperSnowball.get(), p_37400_, p_37399_);
    }

    protected Item getDefaultItem() {
        return Items.SNOWBALL;
    }

    private ParticleOptions getParticle() {
        ItemStack itemstack = this.getItemRaw();
        return (ParticleOptions)(itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack));
    }

    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.level.explode(this.getOwner(), this.getX(), this.getY(), this.getZ(), (float)3.0F, Explosion.BlockInteraction.NONE);
            this.discard();
        }
    }
}
