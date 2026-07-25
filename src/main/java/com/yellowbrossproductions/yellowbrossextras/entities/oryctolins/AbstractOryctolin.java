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

    public AbstractOryctolin(EntityType<? extends YExtrasMob> entityType, Level level) {
        super(entityType, level);
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
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
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
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getEntity() instanceof AbstractOryctolin oryctolin) {
            if (oryctolin.getTeam() != null || this.getTeam() != null) {
                return EntityUtil.canHurtThisMob(oryctolin, this) && super.hurt(pSource, pAmount);
            } else {
                return super.hurt(pSource, pAmount);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    public abstract SoundEvent getCelebrateSound();

    public void updateAnimations() {
        EntityUtil.animateWhen(this.anim_celebrate, this.getAnimationState().equals("celebrate"), this.tickCount);
    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new OryctolinNavigation(this, pLevel);
    }

    static class OryctolinNavigation extends GroundPathNavigation {
        public OryctolinNavigation(Mob mob, Level level) {
            super(mob, level);
        }

        protected PathFinder createPathFinder(int pMaxVisitedNodes) {
            this.nodeEvaluator = new OryctolinNodeEvaluator();
            return new PathFinder(this.nodeEvaluator, pMaxVisitedNodes);
        }
    }

    static class OryctolinNodeEvaluator extends WalkNodeEvaluator {
        OryctolinNodeEvaluator() {
        }

        protected BlockPathTypes evaluateBlockPathType(BlockGetter pLevel, boolean pCanOpenDoors, boolean pCanEnterDoors, BlockPos pPos, BlockPathTypes pNodeType) {
            return super.evaluateBlockPathType(pLevel, true, true, pPos, pNodeType);
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
