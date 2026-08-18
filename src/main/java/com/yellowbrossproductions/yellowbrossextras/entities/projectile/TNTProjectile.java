package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.CreeperInfection;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.List;

public class TNTProjectile extends ThrowableItemProjectile {
    public Mob shooter = null;

    public TNTProjectile(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TNTProjectile(double x, double y, double z, Level level) {
        super(YEEntityTypes.TNTProjectile.get(), x, y, z, level);
    }

    public TNTProjectile(Level level, LivingEntity livingEntity) {
        super(YEEntityTypes.TNTProjectile.get(), livingEntity, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TNT;
    }

    protected void onHitEntity(EntityHitResult pResult) {
        if (!(pResult.getEntity() instanceof CreeperInfection)) {
            super.onHitEntity(pResult);
            if (!this.level().isClientSide) {
                this.explode(2.5D);
                CameraShake.cameraShake(this.level(), position(), 20, 0.05f, 0, 15);
            }
            Entity entity = pResult.getEntity();
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), 1.0F);
        }
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.explode(2.5D);
            this.discard();
        }
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level(), size, this, Entity::isAlive);

        boolean shouldCareAboutTeams = this.shooter != null;
        this.makeExplodeParticles();
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 1.5F);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = true;
                if (shouldCareAboutTeams) {
                    team = EntityUtil.canHurtThisMob(living, this.shooter) && entity != this.shooter;
                }
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.hurt(this.damageSources().thrown(this, this.getOwner()), 2.0F);
                    if (!this.level().isClientSide) {
                        this.discard();
                    }
                }
            }
        }
    }

    public void makeExplodeParticles() {
        for(int i = 0; i < 3; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level(), ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 3; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level(), ParticleTypes.SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 6; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level(), ParticleTypes.EXPLOSION, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
    }
}
