package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
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

public class TNTProjectileEntity extends ThrowableItemProjectile {
    public Mob shooter = null;

    public TNTProjectileEntity(EntityType<? extends ThrowableItemProjectile> p_37442_, Level p_37443_) {
        super(p_37442_, p_37443_);
    }

    public TNTProjectileEntity(double p_37433_, double p_37434_, double p_37435_, Level p_37436_) {
        super(YEEntityTypes.TNTProjectile.get(), p_37433_, p_37434_, p_37435_, p_37436_);
    }

    public TNTProjectileEntity(Level p_37440_, LivingEntity p_37439_) {
        super(YEEntityTypes.TNTProjectile.get(), p_37439_, p_37440_);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TNT;
    }

    protected void onHitEntity(EntityHitResult p_37404_) {
        if (!(p_37404_.getEntity() instanceof CreeperInfection)) {
            super.onHitEntity(p_37404_);
            if (!this.level.isClientSide) {
                this.explode(2.5D);
                CameraShakeEntity.cameraShake(this.level, position(), 20, 0.05f, 0, 15);
            }
            Entity entity = p_37404_.getEntity();
            entity.hurt(DamageSource.thrown(this, this.getOwner()), 1.0F);
        }
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.explode(2.5D);
            this.discard();
        }
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level, size, this, Entity::isAlive);

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
                    living.hurt(DamageSource.thrown(this, this.getOwner()), 2.0F);
                    if (!this.level.isClientSide) {
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
            EntityUtil.makeAParticle(this.level, ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 3; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 6; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.EXPLOSION, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
    }
}
