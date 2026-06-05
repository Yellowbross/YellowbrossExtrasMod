package com.yellowbrossproductions.yellowbrossextras.entities.oryctolins;

import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.EnumSet;

public abstract class AbstractOryctolin extends YExtrasMob implements IsOryctolinAligned, Enemy {
    protected static final EntityDataAccessor<Boolean> IS_CELEBRATING = SynchedEntityData.defineId(AbstractOryctolin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SHAKE_MULTIPLIER = SynchedEntityData.defineId(AbstractOryctolin.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> FACE_STATE = SynchedEntityData.defineId(AbstractOryctolin.class, EntityDataSerializers.INT);
    private int wave;
    private boolean canJoinRaid;
    private int ticksOutsideRaid;
    protected int attackType;
    protected int attackTicks;

    public AnimationState anim_celebrate = new AnimationState();

    public AbstractOryctolin(EntityType<? extends YExtrasMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        super.registerGoals();
    }

    protected void customServerAiStep() {
        if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
            ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
        }

        super.customServerAiStep();
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CELEBRATING, false);
        this.entityData.define(SHAKE_MULTIPLIER, 0);
        this.entityData.define(FACE_STATE, 0);
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }

    public abstract void applyRaidBuffs(int var1, boolean var2);

    public boolean canJoinRaid() {
        return this.canJoinRaid;
    }

    public void setCanJoinRaid(boolean p_37898_) {
        this.canJoinRaid = p_37898_;
    }

    public int getShakeMultiplier() {
        return this.entityData.get(SHAKE_MULTIPLIER);
    }

    public void setShakeMultiplier(int shake) {
        if (!this.level.isClientSide) {
            this.entityData.set(SHAKE_MULTIPLIER, shake);
        }
    }

    public int getFace() {
        return this.entityData.get(FACE_STATE);
    }

    public void setFace(int face) {
        if (!this.level.isClientSide) {
            this.entityData.set(FACE_STATE, face);
        }
    }

    @Override
    public void tick() {
        if (this.attackType > 0) {
            this.attackTicks += 1;
        } else {
            this.attackTicks = 0;
        }
        super.tick();
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.getEntity() instanceof AbstractOryctolin oryctolin) {
            if (oryctolin.getTeam() != null || this.getTeam() != null) {
                return EntityUtil.canHurtThisMob(oryctolin, this) && super.hurt(p_21016_, p_21017_);
            } else {
                return super.hurt(p_21016_, p_21017_);
            }
        }
        return super.hurt(p_21016_, p_21017_);
    }

    public abstract SoundEvent getCelebrateSound();

    public void updateAnimations() {
        EntityUtil.animateWhen(this.anim_celebrate, this.getAnimationState().equals("celebrate"), this.tickCount);
    }

    protected PathNavigation createNavigation(Level p_33348_) {
        return new OryctolinNavigation(this, p_33348_);
    }

    static class OryctolinNavigation extends GroundPathNavigation {
        public OryctolinNavigation(Mob p_33379_, Level p_33380_) {
            super(p_33379_, p_33380_);
        }

        protected PathFinder createPathFinder(int p_33382_) {
            this.nodeEvaluator = new OryctolinNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, p_33382_);
        }
    }

    static class OryctolinNodeEvaluator extends WalkNodeEvaluator {
        OryctolinNodeEvaluator() {
        }

        protected BlockPathTypes evaluateBlockPathType(BlockGetter p_33387_, boolean p_33388_, boolean p_33389_, BlockPos p_33390_, BlockPathTypes p_33391_) {
            if (p_33391_ == BlockPathTypes.DOOR_WOOD_CLOSED) return BlockPathTypes.OPEN;
            if (p_33391_ == BlockPathTypes.DOOR_OPEN) return BlockPathTypes.OPEN;
            return super.evaluateBlockPathType(p_33387_, p_33388_, p_33389_, p_33390_, p_33391_);
        }
    }

    class CustomAttackGoal extends Goal {
        public CustomAttackGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return false;
        }

        @Override
        public void tick() {
            getNavigation().stop();

            LivingEntity entity = AbstractOryctolin.this.getTarget();
            if (entity != null) {
                getLookControl().setLookAt(entity, 30.0F, 30.0F);
            }

            navigation.stop();
        }

        @Override
        public void stop() {
            attackTicks = 0;
            attackType = 0;
            setAnimationState("none");
            setShakeMultiplier(0);
            setFace(0);
        }
    }
}
