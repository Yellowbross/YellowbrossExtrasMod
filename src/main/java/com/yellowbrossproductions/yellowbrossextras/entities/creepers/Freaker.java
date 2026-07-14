package com.yellowbrossproductions.yellowbrossextras.entities.creepers;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class Freaker extends AbstractCreeperEntity implements CreeperInfection, Enemy {

    public Freaker(EntityType<? extends YExtrasMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CreeperExplodeFurtherGoal(this));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, CreeperInfection.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Mob && !(p_29932_ instanceof CreeperInfection);
        }));
    }

    @Override
    public float getStepHeight() {
        return 2.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.MAX_HEALTH, 150.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
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
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            this.dead = true;
            for (int i = 0; i < 3; ++i) {
                Crawler creeper = new Crawler(YEEntityTypes.Crawler.get(), this.level);
                creeper.copyPosition(this);
                creeper.setPos(creeper.getX(), creeper.getY() + 1, creeper.getZ());
                creeper.setDeltaMovement(this.random.nextDouble() - 0.5D,
                        this.random.nextDouble() - 0.5D,
                        this.random.nextDouble() - 0.5D);
                if (this.getTeam() != null) {
                    level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                            level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                }
                this.level.addFreshEntity(creeper);
            }
            this.makeExplodeParticles();
            this.playSound(YESoundEvents.HUGE_EXPLOSION.get(), 4.0F, 2.0F);
            this.playSound(YESoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.5F);
            this.playSound(YESoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.0F);
            this.playSound(YESoundEvents.HUGE_EXPLOSION.get(), 4.0F, 0.6F);
            CameraShake.cameraShake(this.level, position(), 50, 0.4f, 0, 40);
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)(this.explosionRadius * 5.0F * f), explosion$blockinteraction);
            int rad = 6;
            this.level.explode(this, this.getX() - rad, this.getY(), this.getZ() - rad, (float)(this.explosionRadius * 3.0F * f), explosion$blockinteraction);
            this.level.explode(this, this.getX() + rad, this.getY(), this.getZ() - rad, (float)(this.explosionRadius * 3.0F * f), explosion$blockinteraction);
            this.level.explode(this, this.getX() - rad, this.getY(), this.getZ() + rad, (float)(this.explosionRadius * 3.0F * f), explosion$blockinteraction);
            this.level.explode(this, this.getX() + rad, this.getY(), this.getZ() + rad, (float)(this.explosionRadius * 3.0F * f), explosion$blockinteraction);
            this.level.explode(this, this.getX(), this.getY() + rad, this.getZ(), (float)(this.explosionRadius * 3.0F * f), explosion$blockinteraction);
            this.level.explode(this, this.getX(), this.getY() - rad, this.getZ(), (float)(this.explosionRadius * 3.0F * f), explosion$blockinteraction);
            this.discard();
            this.spawnLingeringCloud();
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

    class CreeperExplodeFurtherGoal extends Goal {
        private final AbstractCreeperEntity creeper;
        @Nullable
        private LivingEntity target;

        public CreeperExplodeFurtherGoal(AbstractCreeperEntity p_25919_) {
            this.creeper = p_25919_;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            LivingEntity livingentity = this.creeper.getTarget();
            return this.creeper.getSwellDir() > 0 || livingentity != null && this.creeper.distanceToSqr(livingentity) < 36.0D && this.creeper.isInAttackSight(livingentity);
        }

        public void start() {
            this.creeper.getNavigation().stop();
            this.target = this.creeper.getTarget();
        }

        public void stop() {
            this.target = null;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.target == null) {
                this.creeper.setSwellDir(-1);
            } else if (this.creeper.distanceToSqr(this.target) > 49.0D) {
                this.creeper.setSwellDir(-1);
            } else if (!this.creeper.isInAttackSight(this.target)) {
                this.creeper.setSwellDir(-1);
            } else if (!this.target.isAlive() && !(this.creeper.swell >= 27)) {
                this.creeper.setSwellDir(-1);
            } else {
                this.creeper.setSwellDir(1);
            }
        }
    }
}
