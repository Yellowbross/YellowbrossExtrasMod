package com.yellowbrossproductions.yellowbrossextras.entities.creepers;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.TNTProjectileEntity;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SprayerEntity extends AbstractCreeperEntity implements CreeperInfection, Enemy, RangedAttackMob {

    public SprayerEntity(EntityType<? extends YExtrasMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CreeperExplodeGoal(this));
        this.goalSelector.addGoal(3, new MergeWithMyLoveGoal(this, SprayerEntity.class));
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 4, 15.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, CreeperInfection.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Mob && !(p_29932_ instanceof CreeperInfection);
        }));
    }

    @Override
    public float getStepHeight() {
        return 1.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.4F)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public void performRangedAttack(LivingEntity p_33317_, float p_33318_) {
        if (this.distanceTo(p_33317_) <= 20.0F) {
            TNTProjectileEntity tnt = new TNTProjectileEntity(this.level, this);
            double d0 = p_33317_.getEyeY() - (double)1.1F;
            double d1 = p_33317_.getX() - this.getX();
            double d2 = d0 - tnt.getY();
            double d3 = p_33317_.getZ() - this.getZ();
            double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
            tnt.shoot(d1, d2 + d4, d3, 1.6F, 12.0F);
            this.playSound(SoundEvents.SNOWBALL_THROW, 1.0F, 1.0F + (this.random.nextFloat() / 3.0F));
            this.playSound(SoundEvents.TNT_PRIMED, 1.0F, 1.0F + (this.random.nextFloat() / 3.0F));
            tnt.shooter = this;
            this.level.addFreshEntity(tnt);
        }
    }

    @Override
    protected int getMaxAbsorbs() {
        return 4;
    }

    @Override
    public void die(DamageSource p_21014_) {
        this.ignite();
    }

    @Override
    protected void tickDeath() {
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
    }

    @Override
    public void explodeCreeper() {
        super.explodeCreeper();
        if (this.getAbsorbedCreepers() >= this.getMaxAbsorbs()) {
            if (!this.level.isClientSide) {
                CrawlerEntity creeper = new CrawlerEntity(ModEntityTypes.Crawler.get(), this.level);
                creeper.copyPosition(this);
                if (this.getTeam() != null) {
                    level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                            level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                }
                this.level.addFreshEntity(creeper);

                this.makeExplodeParticles();
                this.playSound(YellowbrossExtrasSoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.2F);
                CameraShakeEntity.cameraShake(this.level, position(), 40, 0.2f, 0, 30);
            }
        }
    }

    public void makeExplodeParticles() {
        for(int i = 0; i < 250; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian()) / 2;
            double d1 = (-0.5 + this.random.nextGaussian()) / 2;
            double d2 = (-0.5 + this.random.nextGaussian()) / 2;
            EntityUtil.makeAParticle(this.level, ParticleTypes.CAMPFIRE_COSY_SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 200; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian()) / 2;
            double d1 = (-0.5 + this.random.nextGaussian()) / 2;
            double d2 = (-0.5 + this.random.nextGaussian()) / 2;
            EntityUtil.makeAParticle(this.level, ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 150; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian()) / 2;
            double d1 = (-0.5 + this.random.nextGaussian()) / 2;
            double d2 = (-0.5 + this.random.nextGaussian()) / 2;
            EntityUtil.makeAParticle(this.level, ParticleTypes.LARGE_SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
    }
}
