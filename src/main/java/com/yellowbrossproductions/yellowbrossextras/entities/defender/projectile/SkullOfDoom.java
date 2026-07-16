package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.WitherExplosion;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.CustomAbstractHurtingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.LoopingSound;
import com.yellowbrossproductions.yellowbrossextras.util.TimeBombSound;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SkullOfDoom extends CustomAbstractHurtingProjectile {
    List<LivingEntity> caught = new ArrayList<>();
    public final int timer = 100;

    public SkullOfDoom(EntityType<? extends CustomAbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public SkullOfDoom(Level level, LivingEntity livingEntity, Vec3 targetPos) {
        super(YEEntityTypes.SkullOfDoom.get(), livingEntity, targetPos.x, targetPos.y, targetPos.z, level);
        this.xPower *= 0.5f;
        this.yPower *= 0.0f;
        this.zPower *= 0.5f;
        this.setYRot(livingEntity.getYRot());
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {

    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) this.hurtMarked = true;

        for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5d))) {
            boolean canHurt = !(this.getOwner() instanceof Mob owner) || EntityUtil.canHurtThisMob(livingEntity, owner);

            if (canHurt && !this.caught.contains(livingEntity) && livingEntity.hasLineOfSight(this)) {
                this.caught.add(livingEntity);
                livingEntity.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 2.0F, livingEntity.getVoicePitch());
                livingEntity.hurt(DamageSource.thrown(this, this.getOwner()), 10.0F);
            }
        }

        ProjectileUtil.rotateTowardsMovement(this, 0.7F);
        super.tick();

        Iterator<LivingEntity> iterator = this.caught.iterator();

        while (iterator.hasNext()) {
            LivingEntity caught = iterator.next();

            if (this.distanceTo(caught) > this.getBoundingBox().getSize() + 0.75d) iterator.remove();

            this.setDeltaMovement(this.getDeltaMovement().scale(0.995d));
            caught.hurtMarked = true;
            caught.setDeltaMovement(this.getDeltaMovement().scale(1.1d));
        }

        if (!this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getMaterial().blocksMotion()) {
            this.setPos(this.position().add(0, -0.2, 0));
        }

        Vec3 flyAway = new Vec3(-xPower, -yPower, -zPower).scale(3.0d).scale(this.getDeltaMovement().length());
        EntityUtil.makeAParticle(this.level, ParticleTypes.LARGE_SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), flyAway);
        EntityUtil.makeAParticle(this.level, ParticleTypes.SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), flyAway);
        for (int i = 0; i < 5; ++i) EntityUtil.makeAParticle(this.level, ParticleTypes.ASH, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), flyAway);

        if (this.tickCount > this.timer && !this.level.isClientSide) {
            this.explode();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (this.level.getBlockState(pResult.getBlockPos().above()).getMaterial().blocksMotion()) this.explode();
        else this.setPos(this.position().add(0, 1, 0));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return super.hurt(pSource, pAmount);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    private void explode() {
        if (this.level.isClientSide) return;

        BlockPos.MutableBlockPos yPosition = this.blockPosition().mutable();
        while (yPosition.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(yPosition).getMaterial().blocksMotion()) {
            yPosition.move(Direction.DOWN);
        }
        Vec3 position = new Vec3(this.getX(), yPosition.getY() + 1, this.getZ());
        WitherExplosion explosion = new WitherExplosion(this.level, position, this.getOwner() instanceof Mob mob ? mob : null);
        this.level.addFreshEntity(explosion);
        this.playSound(YESoundEvents.ENTITY_DEFENDER_WITHERBAZOOKA_EXPLOSION.get(), 10.0f, 1.0f);
        this.discard();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        if (this.level.isClientSide) Minecraft.getInstance().getSoundManager().play(new TimeBombSound(this, YESoundEvents.ENTITY_DEFENDER_WITHERBAZOOKA_LOOP.get(), 2.5F, this.timer));
    }
}
