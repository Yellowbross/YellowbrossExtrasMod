package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.AbstractSnipingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YEParticleTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SniperRifleEntity extends AbstractSnipingProjectile {

    public SniperRifleEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public SniperRifleEntity(Level level, LivingEntity shooter, Vec3 shootTo) {
        super(YEEntityTypes.SniperRifle.get(), level);
        this.setOwner(shooter);
        if (!level.isClientSide) {
            this.setCollisionPos(new BlockPos(shootTo));
        }
        this.setMaxTime(4);
        this.setPos(shooter.position().add(0, 1.25, 0).add(shooter.getLookAngle()));

        double x = this.getCollisionPos().getX() - this.getX();
        double y = this.getCollisionPos().getY() - this.getY();
        double z = this.getCollisionPos().getZ() - this.getZ();
        double d = Math.sqrt(x * x + z * z);
        this.setYRot(180.0F + (float)Math.toDegrees(Math.atan2(x, z)));
        if (this.getYRot() > 360.0F) {
            this.setYRot(this.getYRot() - 360.0F);
        }

        this.setXRot((float)Math.toDegrees(Math.atan2(y, d)));

        SnipingProjectileHitResult hitResult = new SnipingProjectileHitResult();
        Vec3 from = this.position();
        Vec3 to = Vec3.atCenterOf(this.getCollisionPos());
        hitResult.setBlockHit(this.level.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (!level.isClientSide) {
            if (hitResult.blockHit != null) {
                this.setCollisionPos(hitResult.getBlockHit().getBlockPos());
            } else {
                this.setCollisionPos((int)to.x, (int)to.y, (int)to.z);
            }
        }
        List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(
                Math.min(getX(), this.getCollisionPos().getX()),
                Math.min(getY(), this.getCollisionPos().getY()),
                Math.min(getZ(), this.getCollisionPos().getZ()),
                Math.max(getX(), this.getCollisionPos().getX()),
                Math.max(getY(), this.getCollisionPos().getY()),
                Math.max(getZ(), this.getCollisionPos().getZ())).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == this.getOwner()) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                hitResult.addEntityHit(entity);
            } else if (hit.isPresent()) {
                hitResult.addEntityHit(entity);
            }
        }

        double closestDistanceSq = Double.MAX_VALUE;
        LivingEntity closestDamaged = null;
        for (LivingEntity hit : hitResult.entities) {
            if (hit.isAlive() && !hit.isRemoved() && hit.isPickable()) {
                double distanceSq = hit.distanceToSqr(this);
                if (distanceSq < closestDistanceSq) {
                    closestDistanceSq = distanceSq;
                    closestDamaged = hit;
                }
            }
        }

        if (closestDamaged != null && hitResult.blockHit == null) {
            if (!level.isClientSide) this.setCollisionPos(closestDamaged.getBlockX(), (int)(closestDamaged.getBlockY() + closestDamaged.getBbHeight() / 2), closestDamaged.getBlockZ());
        }
    }

    public SniperRifleEntity(PlayMessages.SpawnEntity packet, Level level) {
        this(YEEntityTypes.SniperRifle.get(), level);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount == this.getMaxTime()) {
            this.setPos(Vec3.atCenterOf(this.getCollisionPos()));
            EntityUtil.makeAParticle(this.level, ParticleTypes.EXPLOSION_EMITTER, false, this.position(), Vec3.ZERO);
            EntityUtil.makeAParticle(this.level, YEParticleTypes.RIFLE_EXPLOSION_ONOMATOPOEIA.get(), false, this.position(), Vec3.ZERO);
            for (int i = 0; i < 30; ++i) {
                EntityUtil.makeAParticle(this.level, ParticleTypes.CAMPFIRE_COSY_SMOKE, false, this.position(), new Vec3(-0.5 + this.random.nextDouble(), -0.5 + this.random.nextDouble(), -0.5 + this.random.nextDouble()));
            }
            EntityUtil.makeCircleParticles(this.level, this.position(), ParticleTypes.POOF, 40, 0.75f, new Vec3(90.0f, -this.getYRot(), 0.0f), 0.0F);
            CameraShakeEntity.cameraShake(this.level, position(), 30, 0.1f, 0, 25);
            this.playSound(YESoundEvents.HUGE_EXPLOSION.get(), 2.5f, 1.2f);
            this.explode(null, 9.0d);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private void explode(@Nullable LivingEntity hitEntity, double size) {
        if (this.level.isClientSide) return;

        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(this.position().subtract(size, size, size), this.position().add(size, size, size)), p -> p != this.getOwner());
        if (hitEntity != null) list.add(hitEntity);

        for (LivingEntity entity : list) {
            boolean canHurt = (!(this.getOwner() instanceof Mob owner) || EntityUtil.canHurtThisMob(entity, owner)) && (!(entity instanceof Player) || entity.hasLineOfSight(this));
            if (canHurt) {
                DamageSource damageSource = new IndirectEntityDamageSource("thrown", this, this.getOwner()).setProjectile();
                entity.invulnerableTime = 0;
                entity.hurt(damageSource, Math.max(15, entity.getMaxHealth() * 0.25F) * EntityUtil.multiplyToScrewArmor(entity, 0.3f));
                double mult = 8.0d;
                entity.hurtMarked = true;
                entity.setDeltaMovement((this.random.nextDouble() - 0.5d) * mult, (this.random.nextDouble() - 0.15d) * 2.0d, (this.random.nextDouble() - 0.5d) * mult);
            }
        }
    }
}
