package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.MobAttack;
import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BoomerangEntity extends PathfinderMob implements MobAttack {
    public double accelerationX;
    public double accelerationY;
    public double accelerationZ;
    public Mob shooter = null;
    public LivingEntity goFor = null;
    private boolean shouldReturn = false;

    public double olderX;
    public double olderY;
    public double olderZ;

    public BoomerangEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
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

        List<Entity> checkList = EntityUtil.getEntitiesFromAABB(this.level, 0.4d, this,
                predicate -> predicate.isAlive() && !predicate.isRemoved() && predicate instanceof LivingEntity living && EntityUtil.canHurtThisMob(living, attacker) && predicate != attacker && predicate != this);
        if (!checkList.isEmpty()) {
            List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level, 3.0d, this,
                    predicate -> predicate.isAlive() && !predicate.isRemoved() && predicate instanceof LivingEntity living && EntityUtil.canHurtThisMob(living, attacker) && predicate != attacker && predicate != this);
            for (Entity entity : list) {
                LivingEntity living = (LivingEntity) entity;

                if (!entity.isInvulnerable() && !entity.isSpectator()) {
                    DamageSource damageSource = new IndirectEntityDamageSource("thrown", this, this.shooter){
                        @Nullable
                        @Override
                        public Vec3 getSourcePosition() {
                            return null;
                        }
                    }.setProjectile();
                    boolean flag = living.hurt(damageSource, 4.0F);
                    if (flag) {
                        this.playSound(YESoundEvents.ENTITY_DEFENDER_BOOMERANG_HIT.get(), 2.0F, this.getVoicePitch());
                        living.addEffect(new MobEffectInstance(YEEffects.KNOCKED_OUT.get(), 200, 0, true, true, true));
                        this.makeExplodeParticles();
                    }
                }
            }
        }

        this.makeParticles();

        this.setDeltaMovement(this.accelerationX, this.accelerationY, this.accelerationZ);

        if (this.tickCount % 4 == 0) {
            this.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, 0.5F);
        }

        if (this.tickCount > 120) {
            if (this.tickCount == 121) {
                this.setOlder();
            }
            this.shouldReturn = true;
        }

        if (this.shouldReturn && this.shooter != null) {
            LivingEntity entity = this.shooter;

            double x = this.getX() - entity.getX();
            double y = this.getY() - (entity.getY() + 2.2);
            double z = this.getZ() - entity.getZ();
            double d = Math.sqrt(x * x + y * y + z * z);
            float power = (float) 8.0F;
            double motionX = -(x / d * (double) power * 0.2D);
            double motionY = -(y / d * (double) power * 0.2D);
            double motionZ = -(z / d * (double) power * 0.2D);
            this.setAcceleration(motionX, motionY, motionZ);

            if (this.distanceToSqr(entity) < 6.0D) {
                if (!this.level.isClientSide) {
                    this.discard();
                }
            }
        }

        if (!this.shouldReturn) {
            if (this.goFor != null && !this.goFor.hasEffect(YEEffects.KNOCKED_OUT.get()) && this.goFor.isAlive() && !this.goFor.isRemoved()) {
                if (!(this.goFor instanceof Player)) {
                    LivingEntity entity = this.goFor;

                    double x = this.getX() - entity.getX();
                    double y = this.getY() - (entity.getY() + entity.getEyeHeight());
                    double z = this.getZ() - entity.getZ();
                    double d = Math.sqrt(x * x + y * y + z * z);
                    float power = (float) 5.0F;
                    double motionX = -(x / d * (double) power * 0.2D);
                    double motionY = -(y / d * (double) power * 0.2D);
                    double motionZ = -(z / d * (double) power * 0.2D);
                    this.setAcceleration(motionX, motionY, motionZ);
                }
            } else {
                LivingEntity t = null;
                List<Mob> attacklist = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(40.0D), p -> {
                    return p instanceof Enemy && EntityUtil.canHurtThisMob(p, this) && !p.hasEffect(YEEffects.KNOCKED_OUT.get()) && p.isAlive() && !p.isRemoved();
                });
                if (!attacklist.isEmpty()) {
                    t = attacklist.get(0);
                }
                if (t != null) {
                    this.setGoFor(t);
                }
            }
        }

        if (((this.tickCount == 40) || (this.tickCount == 80)) && this.goFor instanceof Player) {
            if ((distanceTo(this.goFor) > 8.0D)) {
                LivingEntity entity = this.goFor;

                double x = this.getX() - entity.getX();
                double y = this.getY() - (entity.getY() + 1.5);
                double z = this.getZ() - entity.getZ();
                double d = Math.sqrt(x * x + y * y + z * z);
                float power = (float) 5.0F;
                double motionX = -(x / d * (double) power * 0.2D);
                double motionY = -(y / d * (double) power * 0.2D);
                double motionZ = -(z / d * (double) power * 0.2D);
                this.setAcceleration(motionX, motionY, motionZ);

                this.setOlder();
            }
        }

        if (this.goFor instanceof Player) {
            if (!this.shouldReturn) {
                EntityUtil.makeSimpleTrail(this, ParticleTypes.ELECTRIC_SPARK, 30,
                        this.olderX, this.olderY, this.olderZ,
                        this.olderX + (this.getDeltaMovement().x * 40.0D),
                        this.olderY + (this.getDeltaMovement().y * 40.0D),
                        this.olderZ + (this.getDeltaMovement().z * 40.0D));
            } else {
                if (this.shooter != null) {
                    EntityUtil.makeSimpleTrail(this, ParticleTypes.ELECTRIC_SPARK, 30, this.olderX, this.olderY, this.olderZ, this.shooter.getX(), this.shooter.getY() + 2.2, this.shooter.getZ());
                }
            }
        }

        if (this.tickCount >= 240) {
            if (!this.level.isClientSide) {
                this.discard();
            }
        }

        if (this.shooter != null) {
            if (!this.shooter.isAlive()) {
                this.discard();
            }
        }

        super.tick();
        this.setYRot(this.getYHeadRot());
        this.yBodyRot = this.getYRot();
    }

    public void setOlder() {
        this.olderX = this.getX();
        this.olderY = this.getY();
        this.olderZ = this.getZ();
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

    public void setAcceleration(Vec3 vec) {
        this.setAcceleration(vec.x, vec.y, vec.z);
    }

    public void setGoFor(LivingEntity goFor) {
        this.goFor = goFor;
    }

    public void makeParticles() {
        for(int i = 0; i < 1; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.CRIT, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
    }

    public void makeExplodeParticles() {
        for(int i = 0; i < 20; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.CRIT, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 6; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.EXPLOSION, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
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

    @Override
    public boolean shouldRenderAtSqrDistance(double p_19883_) {
        return p_19883_ < 2048;
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
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
    public void setHealth(float p_21154_) {

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
}
