package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.mojang.math.Vector3f;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.AbstractSnipingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import java.util.List;
import java.util.Optional;

public class DeadlyArrow extends AbstractSnipingProjectile {
    SnipingProjectileHitResult hitResult = new SnipingProjectileHitResult();
    int i = 6;

    public DeadlyArrow(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public DeadlyArrow(Level level, LivingEntity shooter, Vec3 shootTo) {
        super(YEEntityTypes.DeadlyArrow.get(), level);
        this.setOwner(shooter);
        if (!level.isClientSide) {
            this.setCollisionPos(new BlockPos(shootTo));
        }
        this.setMaxTime(4);
        this.setPos(shooter.position().add(0, 1.25, 0));

        double x = this.getCollisionPos().getX() - this.getX();
        double y = this.getCollisionPos().getY() - this.getY();
        double z = this.getCollisionPos().getZ() - this.getZ();
        double d = Math.sqrt(x * x + z * z);
        this.setYRot(180.0F + (float)Math.toDegrees(Math.atan2(x, z)));
        if (this.getYRot() > 360.0F) {
            this.setYRot(this.getYRot() - 360.0F);
        }

        this.setXRot((float)Math.toDegrees(Math.atan2(y, d)));
    }

    public DeadlyArrow(PlayMessages.SpawnEntity packet, Level level) {
        this(YEEntityTypes.DeadlyArrow.get(), level);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount < 20) {
            DustParticleOptions red = new DustParticleOptions(new Vector3f(1, 0.2f, 0.2f), (this.tickCount / 15.0f));
            DustParticleOptions flash = new DustParticleOptions(new Vector3f(1, 1, 1), (this.tickCount / 15.0f));
            if (this.tickCount % i == i - 1) {
                if (i > 2) i -= 1;
            }
            EntityUtil.makeSimpleTrail(this, this.tickCount % i == i - 1 ? flash : red, 50, this.getX(), this.getY(), this.getZ(), this.getCollisionPos().getX(), this.getCollisionPos().getY(), this.getCollisionPos().getZ());
        }
        if (tickCount == 20) {
            this.playSound(YESoundEvents.ENTITY_DEFENDER_SNIPE_SHOOT.get(), 3.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            Vec3 from = this.position();
            Vec3 to = Vec3.atCenterOf(this.getCollisionPos());
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
                    this.hitResult.addEntityHit(entity);
                } else if (hit.isPresent()) {
                    this.hitResult.addEntityHit(entity);
                }
            }
        }
        if (tickCount == this.getMaxTime() + 20) {
            DamageSource damageSource = new IndirectEntityDamageSource("thrown", this, this.getOwner()).setProjectile();
            for (LivingEntity living : this.hitResult.entities) {
                if (living.hurt(damageSource, Math.max(15, living.getMaxHealth() * 0.25F))) {
                    living.invulnerableTime = 0;
                    living.playSound(YESoundEvents.ENTITY_DEFENDER_SNIPE_HIT.get(), 3.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                }
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }
}
