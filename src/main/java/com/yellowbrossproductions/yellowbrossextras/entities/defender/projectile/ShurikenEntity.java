package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.RegistryHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ShurikenEntity extends ThrowableItemProjectile {

    public ShurikenEntity(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }

    public ShurikenEntity(double p_37433_, double p_37434_, double p_37435_, Level p_37436_) {
        super(ModEntityTypes.Shuriken.get(), p_37433_, p_37434_, p_37435_, p_37436_);
    }

    public ShurikenEntity(Level p_37440_, LivingEntity p_37439_) {
        super(ModEntityTypes.Shuriken.get(), p_37439_, p_37440_);
    }

    @Override
    protected Item getDefaultItem() {
        return RegistryHandler.SHURIKEN.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 100) {
            if (!this.level.isClientSide) {
                this.level.broadcastEntityEvent(this, (byte)3);
                this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)1.0F, Explosion.BlockInteraction.NONE);
                this.discard();
            }
        }
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    protected void onHitEntity(EntityHitResult p_37404_) {
        super.onHitEntity(p_37404_);
        Entity entity = p_37404_.getEntity();
        if (entity instanceof LivingEntity) {
            float amount = ((LivingEntity) entity).getMaxHealth() / 12.5F;
            entity.hurt(DamageSource.thrown(this, this.getOwner()), 4.0F + amount);
        } else {
            entity.hurt(DamageSource.thrown(this, this.getOwner()), 4.0F);
        }
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
        if (!(p_37406_ instanceof EntityHitResult)) {
            if (!this.level.isClientSide) {
                this.level.broadcastEntityEvent(this, (byte)3);
                this.discard();
            }
        }
    }
}
