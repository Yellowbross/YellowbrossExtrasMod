package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.MobAttack;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class DefenderAxeEntity extends PathfinderMob implements MobAttack {
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public Mob shooter = null;
    private boolean canExplode = false;

    public DefenderAxeEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, (double)0.0F).add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.ATTACK_DAMAGE, 0.0D).add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource bullcrap) {
        if (!this.level.isClientSide) {
            this.discard();
        }
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance p_21197_) {
        return false;
    }

    @Override
    public void tick() {
        this.setInvulnerable(true);
        this.noPhysics = true;

        Mob attacker = this.shooter != null ? this.shooter : this;

        List<Entity> list = this.level.getEntities(this, new AABB(this.getX() - 0.4D, this.getY() - 0.4D, this.getZ() - 0.4D, this.getX() + 0.4D, this.getY() + 0.4D, this.getZ() + 0.4D), Entity::isAlive);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living && EntityUtil.canHurtThisMob(living, attacker) && entity != attacker) {
                if (entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    this.canExplode = true;
                }
            }
        }

        if (this.canExplode) {
            this.explode(2.5D);
        }

        this.makeParticles();

        this.setDeltaMovement(this.accelerationX, this.accelerationY, this.accelerationZ);

        if (this.tickCount >= 100) {
            if (!this.level.isClientSide) {
                this.discard();
            }
        }

        super.tick();
        this.setYRot(this.getYHeadRot());
        this.yBodyRot = this.getYRot();
    }

    @Override
    public void kill() {
        super.kill();
        if (!this.level.isClientSide) {
            this.discard();
        }
    }

    public void setAcceleration(double x, double y, double z) {
        this.accelerationX = x;
        this.accelerationY = y;
        this.accelerationZ = z;
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
    public boolean shouldRenderAtSqrDistance(double p_19883_) {
        return p_19883_ < 2048;
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
    public void setHealth(float p_21154_) {

    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        Vec3 vector3d = (new Vec3(x, y, z)).normalize().add(this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy, this.random.nextGaussian() * (double)0.0075F * (double)inaccuracy).scale((double)velocity);
        this.setDeltaMovement(vector3d);
        float f = (float) vector3d.horizontalDistanceSqr();
        this.setYHeadRot((float)(Mth.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
        this.setYRot((float)(Mth.atan2(vector3d.x, vector3d.z) * (double)(180F / (float)Math.PI)));
        this.setXRot((float)(Mth.atan2(vector3d.y, (double)f) * (double)(180F / (float)Math.PI)));
        this.setYRot(this.yRotO);
        this.setXRot(this.xRotO);
    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -Mth.sin(p_234612_3_ * ((float)Math.PI / 180F)) * Mth.cos(p_234612_2_ * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((p_234612_2_ + p_234612_4_) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(p_234612_3_ * ((float)Math.PI / 180F)) * Mth.cos(p_234612_2_ * ((float)Math.PI / 180F));
        this.shoot((double)f, (double)f1, (double)f2, p_234612_5_, p_234612_6_);
        Vec3 vector3d = p_234612_1_.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vector3d.x, p_234612_1_.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
        this.accelerationX = this.getDeltaMovement().x;
        this.accelerationY = this.getDeltaMovement().y;
        this.accelerationZ = this.getDeltaMovement().z;
    }

    public void setShooter(Mob shooter) {
        this.shooter = shooter;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return null;
    }

    protected SoundEvent getDeathSound() {
        return null;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source != DamageSource.OUT_OF_WORLD) {
            return false;
        } else {
            return super.hurt(source, amount);
        }
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {

    }

    @Override
    public void push(Entity entityIn) {

    }

    private void explode(double size) {
        List<Entity> list = this.level.getEntities(this, new AABB(this.getX() - size, this.getY() - size, this.getZ() - size, this.getX() + size, this.getY() + size, this.getZ() + size), Entity::isAlive);

        Mob attacker = this.shooter != null ? this.shooter : this;
        this.makeExplodeParticles();
        this.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 2.0F, 1.0F);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living && EntityUtil.canHurtThisMob(living, attacker) && entity != attacker) {
                if (entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    DamageSource damageSource = new IndirectEntityDamageSource("thrown", this, attacker).setProjectile();
                    living.hurt(damageSource, 8.0F);
                    living.invulnerableTime = 0;
                    if (!this.level.isClientSide) {
                        this.discard();
                    }
                }
            }
        }
    }
}
