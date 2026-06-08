package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.projectile.CustomAbstractHurtingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class DefenderAxeEntity extends CustomAbstractHurtingProjectile {

    public DefenderAxeEntity(EntityType<? extends DefenderAxeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public DefenderAxeEntity(Level level, LivingEntity shooter, Vec3 shootDir) {
        super(YEEntityTypes.DefenderAxe.get(), shooter, shootDir.x, shootDir.y, shootDir.z, level);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        super.onHitEntity(p_37259_);
        this.explode(null, 2.5D);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        this.explode(null, 2.5D);
    }

    @Override
    public void tick() {
        this.setInvulnerable(true);

        this.makeParticles();

        if (this.tickCount >= 100 && !this.level.isClientSide) this.discard();

        float oldOldYRot = this.yRotO;
        super.tick();

        this.setYRot((float) Math.toDegrees(Math.atan2(this.getDeltaMovement().z, this.getDeltaMovement().x)) - 90);
        this.setXRot(0);
        this.yRotO = oldOldYRot;
        this.xRotO = 0;
    }

    public void makeParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 1; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.CRIT, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 1;
    }

    public void makeExplodeParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 10; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 20; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.CRIT, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 6; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.EXPLOSION, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return source != DamageSource.OUT_OF_WORLD && super.hurt(source, amount);
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    private void explode(@Nullable Entity hitEntity, double size) {
        if (this.level.isClientSide) return;

        this.makeExplodeParticles();
        this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 2.0F, 1.0F);

        List<Entity> list = this.level.getEntities(this, new AABB(this.position().subtract(size, size, size), this.position().add(size, size, size)), p -> p instanceof LivingEntity && p != this.getOwner());
        if (hitEntity instanceof LivingEntity) list.add(hitEntity);

        for (Entity entity : list) {
            boolean canHurt = !(this.getOwner() instanceof Mob owner) || EntityUtil.canHurtThisMob(entity, owner);
            if (canHurt) {
                DamageSource damageSource = new IndirectEntityDamageSource("thrown", this, this.getOwner()).setProjectile();
                entity.invulnerableTime = 0;
                entity.hurt(damageSource, 8.0F);
            }
        }

        this.discard();
    }
}
