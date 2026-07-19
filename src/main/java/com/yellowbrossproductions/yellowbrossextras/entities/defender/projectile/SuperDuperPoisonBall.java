package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.IsDefenderAligned;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.CustomAbstractHurtingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.*;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SuperDuperPoisonBall extends CustomAbstractHurtingProjectile implements ItemSupplier {

    public SuperDuperPoisonBall(EntityType<? extends CustomAbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SuperDuperPoisonBall(Level level, LivingEntity livingEntity, double vx, double vy, double vz) {
        super(YEEntityTypes.SuperDuperPoisonBall.get(), livingEntity, vx, vy, vz, level);
    }

    @Override
    protected boolean canHitEntity(Entity pEntity) {
        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        boolean team = true;
        if (shouldCareAboutTeams) {
            team = EntityUtil.canHurtThisMob(pEntity, (Mob) this.getOwner()) && !(pEntity instanceof IsDefenderAligned);
        }
        return team && pEntity != this.getOwner() && !(pEntity instanceof Projectile) && super.canHitEntity(pEntity);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.explode(2.0D);
            this.discard();
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return YEParticleTypes.SUPERDUPERPOISON_DRIP.get();
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level, size, this, Entity::isAlive);

        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        this.playSound(YESoundEvents.SUPERDUPERPOISON_NOSCREAM.get(), 1.0F, 1.0F);
        EntityUtil.makeAParticle(this.level, YEParticleTypes.SUPERDUPERPOISON_EXPLOSION.get(), false, this.position(), Vec3.ZERO);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = true;
                if (shouldCareAboutTeams) {
                    team = EntityUtil.canHurtThisMob(living, (Mob) this.getOwner()) && entity != this.getOwner() && !(living instanceof IsDefenderAligned);
                }
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.addEffect(new MobEffectInstance(YEEffects.SUPER_DUPER_POISON.get(), 25 * 20, 0));
                }
            }
        }
        if (!this.level.isClientSide) {
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > 80) {
            if (!this.level.isClientSide) {
                this.level.broadcastEntityEvent(this, (byte)3);
                this.explode(2.0D);
                this.discard();
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return YEItemsAndBlocks.SUPERDUPERPOISON_BALL.get().getDefaultInstance();
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource == DamageSource.OUT_OF_WORLD) {
            return super.hurt(pSource, pAmount);
        }
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
