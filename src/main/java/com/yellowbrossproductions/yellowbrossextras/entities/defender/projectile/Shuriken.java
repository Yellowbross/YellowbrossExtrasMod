package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YEItemsAndBlocks;
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

public class Shuriken extends ThrowableItemProjectile {

    public Shuriken(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public Shuriken(double x, double y, double z, Level level) {
        super(YEEntityTypes.Shuriken.get(), x, y, z, level);
    }

    public Shuriken(Level level, LivingEntity owner) {
        super(YEEntityTypes.Shuriken.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return YEItemsAndBlocks.SHURIKEN.get();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > 100) {
            if (!this.level().isClientSide) {
                this.level().broadcastEntityEvent(this, (byte)3);
                this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float)1.0F, Level.ExplosionInteraction.NONE);
                this.discard();
            }
        }
    }

    @Override
    protected float getGravity() {
        return 0.0F;
    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        Entity entity = pResult.getEntity();
        if (entity instanceof LivingEntity) {
            float amount = ((LivingEntity) entity).getMaxHealth() / 12.5F;
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4.0F + amount);
        } else {
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4.0F);
        }
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!(pResult instanceof EntityHitResult)) {
            if (!this.level().isClientSide) {
                this.level().broadcastEntityEvent(this, (byte)3);
                this.discard();
            }
        }
    }
}
