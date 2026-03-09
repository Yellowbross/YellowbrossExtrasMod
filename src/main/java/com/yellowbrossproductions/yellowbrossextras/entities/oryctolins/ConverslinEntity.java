package com.yellowbrossproductions.yellowbrossextras.entities.oryctolins;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.ConverslinBulletEntity;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ConverslinEntity extends AbstractOryctolin {
    int frame;
    boolean AHHHHHH = false;
    LivingEntity runAwayFrom = null;
    boolean celebrating = false;
    private boolean isBreakDoorsTaskSet = true;
    int runAwayTimer;
    boolean canAttack = false;

    private int ATTACK1 = 1;

    public AnimationState anim_attack1 = new AnimationState();

    public ConverslinEntity(EntityType<? extends AbstractOryctolin> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new Attack1Goal());

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LiveLifeWithoutASingleCareGoal());

        this.goalSelector.addGoal(1, new OryctolinAvoidGoal(this, Ocelot.class, 6.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(1, new OryctolinAvoidGoal(this, Cat.class, 6.0F, 1.2D, 1.2D));
        this.goalSelector.addGoal(1, new OryctolinAvoidGoal(this, Wolf.class, 6.0F, 1.2D, 1.2D));

        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new AvoidEntityGoal(this, LivingEntity.class, 15.0F, 1.5, 1.5, (p_29932_) -> {
            return this.getTarget() == p_29932_ || (this.getTeam() != null && EntityUtil.canHurtThisMob((LivingEntity) p_29932_, this));
        }));
        this.goalSelector.addGoal(2, new AvoidEntityGoal(this, LivingEntity.class, 50.0F, 1.0, 1.5, (p_29932_) -> {
            return this.AHHHHHH && (this.getTarget() == p_29932_ || (this.getTeam() != null && EntityUtil.canHurtThisMob((LivingEntity) p_29932_, this)));
        }));
        this.goalSelector.addGoal(3, new AttackGoal());

        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new MoveThroughVillageGoal(this, 0.7D, false, 4, this::isBreakDoorsTaskSet));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F, 1.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AbstractOryctolin.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Vex.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Raider.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        if (this.getFace() == 1) {
            this.setFace(2);
        }
        return super.causeFallDamage(p_147187_, p_147188_, p_147189_);
    }

    @Override
    public boolean hasLineOfSight(Entity p_147185_) {
        if (this.getFace() == 2) {
            if (p_147185_.level != this.level) {
                return false;
            } else {
                Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
                Vec3 vec31 = new Vec3(p_147185_.getX(), p_147185_.getEyeY(), p_147185_.getZ());
                if (vec31.distanceTo(vec3) > 128.0D) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return super.hasLineOfSight(p_147185_);
    }

    @Override
    protected float getStandingEyeHeight(Pose p_21131_, EntityDimensions p_21132_) {
        return 1.25F;
    }

    @Override
    public void applyRaidBuffs(int var1, boolean var2) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    public float getStepHeight() {
        return 1.0F;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.celebrating) {
            return this.getCelebrateSound();
        }
        return super.getAmbientSound();
    }

    public boolean isBreakDoorsTaskSet() {
        return this.isBreakDoorsTaskSet;
    }

    @Override
    public void tick() {
        if (this.attackType > 0) {
            LivingEntity target = this.getTarget();
            if (target != null) {
                if (this.attackType == ATTACK1) {
                    if (this.attackTicks == 4) {
                        this.setFace(5);
                    }
                    if (this.attackTicks == 6) {
                        this.setFace(3);
                        double d1 = target.getX() - this.getX();
                        double d3 = target.getZ() - this.getZ();
                        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
                        this.stuckSpeedMultiplier = Vec3.ZERO;
                        float distance = (float) this.distanceToSqr(target);
                        float mult = 2.0F;
                        if (distance > 0) {
                            this.setDeltaMovement(
                                    ((d1 * d4) * mult) / distance,
                                    0.8D,
                                    ((d3 * d4) * mult) / distance);
                        }
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_CONVERSLIN_JUMP.get(), 1.0F, this.getVoicePitch());
                    }
                    if (this.attackTicks == 16) {
                        float radius2 = 1.1f;
                        double x = this.getX() + 0.8F * Math.sin(-this.getYRot() * Math.PI / 180) + radius2 * Math.sin(-this.yHeadRot * Math.PI / 180) * Math.cos(-this.getXRot() * Math.PI / 180);
                        double y = this.getY() + 1.0 + radius2 * Math.sin(-this.getXRot() * Math.PI / 180);
                        double z = this.getZ() + 0.8F * Math.cos(-this.getYRot() * Math.PI / 180) + radius2 * Math.cos(-this.yHeadRot * Math.PI / 180) * Math.cos(-this.getXRot() * Math.PI / 180);
                        for (int i = 0; i < 5; ++i) {
                            double y1 = this.getY() + 0.75D;

                            float $$4 = target.yBodyRot * 0.017453292F;
                            float $$5 = Mth.cos($$4) * (i - 2);
                            float $$6 = Mth.sin($$4) * (i - 2);

                            double d0 = target.getEyeY();
                            double d1 = target.getX() + (double)$$5 - x;
                            double d2 = d0 - y1;
                            double d3 = target.getZ() + (double)$$6 - z;
                            double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;

                            ConverslinBulletEntity bullet = new ConverslinBulletEntity(this.level, this, d1, d2, d3);

                            bullet.setPos(x, y1, z);
                            bullet.setOwner(this);
                            this.level.addFreshEntity(bullet);
                        }
                    }
                }
            }
        }

        if (this.getHealth() < this.getMaxHealth() / 2 && this.getShakeMultiplier() == 0) {
            this.setShakeMultiplier(3);
        }

        super.tick();
        if (this.attackType > 0) {
            this.setYRot(this.getYHeadRot());
            this.yBodyRot = this.getYRot();
        }

        float moveX = (float) (this.getX() - this.xo);
        float moveZ = (float) (this.getZ() - this.zo);
        float speed = Mth.sqrt(moveX * moveX + moveZ * moveZ);
        if (speed > 0.1) {
            if (this.tickCount % 4 == 0 && this.isOnGround()) {
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_CONVERSLIN_STEP.get(), 0.25F, 1.0F);
            }
        }

        this.runAwayTimer--;
        if (this.AHHHHHH && this.runAwayTimer < 1) {
            this.AHHHHHH = false;
            this.setFace(0);
        }

        if (this.getTarget() != null && !this.getTarget().isAlive()) this.setTarget(null);

        if (this.isAggressive() && this.getFace() == 0) {
            this.setFace(3);
        } else if (this.getTarget() == null && this.getFace() == 3) {
            this.setFace(0);
        }

        if (this.getFace() == 1 && this.isInWater()) {
            this.setFace(2);
        }

        if (this.AHHHHHH && this.runAwayFrom != null && this.getFace() != 1 && this.getFace() != 2) {
            this.playSound(YellowbrossExtrasSoundEvents.ENTITY_CONVERSLIN_SHRIEK.get(), 1.5F, this.getVoicePitch());
            this.setFace(1);
            this.setAnimationState(0);
            double d1 = this.runAwayFrom.getX() - this.getX();
            double d3 = this.runAwayFrom.getZ() - this.getZ();
            double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
            this.stuckSpeedMultiplier = Vec3.ZERO;
            float distance = (float) this.distanceToSqr(this.runAwayFrom);
            float mult = 10.0F;
            if (distance > 0) {
                this.setDeltaMovement(
                        (-(d1 * d4) * mult) / distance,
                        1.2D,
                        (-(d3 * d4) * mult) / distance);
            }
        }

        if (YellowbrossExtrasConfig.oryctolin_victoryDance.get()) {
            this.celebrateAnim();
        }

        this.frame++;
    }

    public void celebrateAnim() {
        if (!this.level.isClientSide) {
            if (!this.AHHHHHH && this.getTarget() == null) {
                if (this.level instanceof ServerLevel) {
                    if (((ServerLevel) this.level).getRaidAt(this.blockPosition()) != null) {
                        Raid raid = ((ServerLevel) this.level).getRaidAt(this.blockPosition());
                        assert raid != null;
                        if (raid.isVictory()) {
                            this.celebrating = raid.celebrationTicks < 580;
                            if (this.celebrating && this.getTarget() == null) {
                                if (this.getAnimationState() == 0) {
                                    this.setFace(4);
                                    this.setAnimationState(1);
                                }
                            } else {
                                if (this.getAnimationState() == 1) {
                                    this.setAnimationState(0);
                                }
                            }
                        }
                    } else {
                        this.celebrating = false;
                    }
                }
            }
        }
    }

    @Override
    public AnimationState getAnimationState(String input) {
        if (input == "attack1") {
            return anim_attack1;
        }
        return super.getAnimationState(input);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ANIMATION_STATE.equals(p_21104_)) {
            if (this.level.isClientSide) {
                switch (this.entityData.get(ANIMATION_STATE)) {
                    case 2 :
                        this.stopAllAnimationStates();
                        this.anim_attack1.start(this.tickCount);
                        break;
                }
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    @Override
    public void stopAllAnimationStates() {
        this.anim_attack1.stop();
        super.stopAllAnimationStates();
    }

    @Override
    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {

    }

    public int getFrame() {
        return frame;
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        boolean soup = super.hurt(p_21016_, p_21017_);
        if (soup) {
            if (p_21016_.getEntity() instanceof LivingEntity entity && !this.level.isClientSide && !this.AHHHHHH) {
                this.runAwayTimer = 300;
                this.AHHHHHH = true;
                this.runAwayFrom = entity;
            }
        }
        return soup;
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        return false;
    }

    class OryctolinAvoidGoal extends AvoidEntityGoal {

        public OryctolinAvoidGoal(PathfinderMob p_25027_, Class p_25028_, float p_25029_, double p_25030_, double p_25031_) {
            super(p_25027_, p_25028_, p_25029_, p_25030_, p_25031_);
        }

        @Override
        public void start() {
            super.start();
            if (!ConverslinEntity.this.AHHHHHH) {
                ConverslinEntity.this.setFace(2);
            }
        }

        @Override
        public void stop() {
            super.stop();
            if (!ConverslinEntity.this.AHHHHHH) {
                ConverslinEntity.this.setFace(0);
            }
        }
    }



    class AttackGoal extends Goal {

        public AttackGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return ((getTarget() != null && ConverslinEntity.this.distanceTo(getTarget()) < 30.0D && getTarget().isAlive() && ConverslinEntity.this.hasLineOfSight(getTarget()))) && !ConverslinEntity.this.AHHHHHH;
        }

        @Override
        public void start() {
            setFace(3);
            ConverslinEntity.this.canAttack = true;
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public void tick() {
            ConverslinEntity.this.getNavigation().stop();

            if (getTarget() != null) {
                getLookControl().setLookAt(getTarget(), 30.0F, 30.0F);
            }

            ConverslinEntity.this.navigation.stop();
        }

        @Override
        public void stop() {
            ConverslinEntity.this.canAttack = false;
        }
    }

    class Attack1Goal extends CustomAttackGoal {

        @Override
        public boolean canUse() {
            return random.nextInt(16) == 0 && getTarget() != null && !ConverslinEntity.this.AHHHHHH && ConverslinEntity.this.distanceTo(getTarget()) > 15.0D;
        }

        @Override
        public void start() {
            setAnimationState(2);
            attackType = ATTACK1;
        }

        @Override
        public boolean canContinueToUse() {
            return attackTicks <= 30 && !ConverslinEntity.this.AHHHHHH;
        }

        @Override
        public void stop() {
            super.stop();
        }
    }

    class LiveLifeWithoutASingleCareGoal extends Goal {

        public LiveLifeWithoutASingleCareGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return ConverslinEntity.this.celebrating && !ConverslinEntity.this.AHHHHHH;
        }

        @Override
        public void start() {
            setFace(4);
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public void tick() {
            ConverslinEntity.this.getNavigation().stop();

            ConverslinEntity.this.navigation.stop();
        }

        @Override
        public void stop() {
            setFace(0);
        }
    }
}
