package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.IsOryctolinAligned;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.init.YEItemsAndBlocks;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;

import java.util.List;

public class ConverslinBullet extends CustomAbstractHurtingProjectile implements ItemSupplier {

    public ConverslinBullet(EntityType<? extends CustomAbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ConverslinBullet(EntityType<? extends CustomAbstractHurtingProjectile> entityType, double startX, double startY, double startZ, double shootX, double shootY, double shootZ, Level level) {
        super(YEEntityTypes.ConverslinBullet.get(), startX, startY, startZ, shootX, shootY, shootZ, level);
    }

    public ConverslinBullet(Level level, LivingEntity owner, double x, double y, double z) {
        super(YEEntityTypes.ConverslinBullet.get(), owner, x, y, z, level);
    }

    protected void onHitEntity(EntityHitResult pResult) {
        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        boolean team = true;
        if (shouldCareAboutTeams) {
            team = EntityUtil.canHurtThisMob(pResult.getEntity(), (Mob) this.getOwner()) && !(pResult.getEntity() instanceof AbstractOryctolin);
        }
        if (team && pResult.getEntity() != this.getOwner() && !(pResult.getEntity() instanceof Projectile)) {
            super.onHitEntity(pResult);
            if (!this.level().isClientSide) {
                this.explode(3.0D);
            }
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.explode(3.0D);
            this.discard();
        }
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level(), size, this, Entity::isAlive);

        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        this.makeExplodeParticles();
        this.playSound(SoundEvents.GENERIC_EXPLODE, 2.0F, 0.8F);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = true;
                if (shouldCareAboutTeams) {
                    team = EntityUtil.canHurtThisMob(living, (Mob) this.getOwner()) && entity != this.getOwner() && !(living instanceof IsOryctolinAligned);
                }
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 15.0F);
                    if (!this.level().isClientSide) {
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > 80) {
            if (!this.level().isClientSide) {
                this.level().broadcastEntityEvent(this, (byte)3);
                this.explode(3.0D);
                this.discard();
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
        for(int i = 0; i < 1; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level(), ParticleTypes.EXPLOSION_EMITTER, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
    }

    @Override
    public ItemStack getItem() {
        return YEItemsAndBlocks.CONVERSLIN_BULLET.get().getDefaultInstance();
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.GENERIC_KILL)) {
            return super.hurt(pSource, pAmount);
        }
        return false;
    }
}
