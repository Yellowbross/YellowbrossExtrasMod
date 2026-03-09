package com.yellowbrossproductions.yellowbrossextras.entities.defender;

import com.yellowbrossproductions.yellowbrossextras.client.model.animation.ICanBeAnimated;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

import java.util.EnumSet;

public class SentryGunEntity extends PathfinderMob implements ICanBeAnimated {
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(SentryGunEntity.class, EntityDataSerializers.INT);

    public AnimationState animationState_shoot = new AnimationState();
    public AnimationState animationState_intro = new AnimationState();
    public AnimationState animationState_flying = new AnimationState();

    public SentryGunEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ShootGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Enemy;
        }));
        super.registerGoals();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_STATE, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.0F)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    public void setAnimationState(int input) {
        this.entityData.set(ANIMATION_STATE, input);
    }

    @Override
    public AnimationState getAnimationState(String input) {
        return switch (input) {
            case "shoot" -> animationState_shoot;
            case "intro" -> animationState_intro;
            case "flying" -> animationState_flying;

            default -> new AnimationState();
        };
    }

    public int getAnimationState() {
        return this.entityData.get(ANIMATION_STATE);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_21104_) {
        if (ANIMATION_STATE.equals(p_21104_)) {
            if (this.level.isClientSide) {
                switch (this.entityData.get(ANIMATION_STATE)) {
                    case 0 :
                        this.stopAllAnimationStates();
                        break;
                    case 1 :
                        this.stopAllAnimationStates();
                        this.animationState_shoot.start(this.tickCount);
                        break;
                    case 2 :
                        this.stopAllAnimationStates();
                        this.animationState_intro.start(this.tickCount);
                        break;
                    case 3 :
                        this.stopAllAnimationStates();
                        this.animationState_flying.start(this.tickCount);
                        break;
                }
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.animationState_shoot.stop();
        this.animationState_intro.stop();
        this.animationState_flying.stop();
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    class ShootGoal extends Goal {
        private final SentryGunEntity gun;

        public ShootGoal(SentryGunEntity gun) {
            this.gun = gun;
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return gun.getTarget() != null && gun.hasLineOfSight(gun.getTarget());
        }

        @Override
        public boolean canContinueToUse() {
            return canUse();
        }

        @Override
        public void tick() {
            LivingEntity target = gun.getTarget();
            if (target == null) return;

            gun.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ(), 100.0F, 100.0F);

            if (gun.tickCount % 5 == 0) {
                gun.setAnimationState(1);
            }
        }
    }
}
