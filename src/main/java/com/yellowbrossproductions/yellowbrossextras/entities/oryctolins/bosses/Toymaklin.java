package com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.bosses;

import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

public class Toymaklin extends BossOryctolin {
    private final int FACE_DEFAULT = 0;
    public AnimationState anim_idle = new AnimationState();
    private static final EntityDataAccessor<Integer> IDLE_TICKS = SynchedEntityData.defineId(Toymaklin.class, EntityDataSerializers.INT);

    public Toymaklin(EntityType<? extends YExtrasMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));

        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
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
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 96.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IDLE_TICKS, 0);
    }

    @Override
    public void applyRaidBuffs(int wave, boolean var2) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    public float getStepHeight() {
        return 1.0F;
    }

    @Override
    public void tick() {
        super.tick();

        int idleAnimTimer = this.getIdleTicks() % 30;
        if (idleAnimTimer == 0) {
            this.setAnimationState("none");
            this.setAnimationState("idle");
        }
        if (idleAnimTimer == 13) {
            this.playSound(YESoundEvents.ENTITY_TOYMAKLIN_SHIVER.get(), 1.0F, 1.0F);
            this.setShakeMultiplier(5);
        }
        if (idleAnimTimer == 26) {
            this.setShakeMultiplier(0);
        }
        this.setIdleTicks(this.getIdleTicks() + 1);


    }

    public int getIdleTicks() {
        return this.entityData.get(IDLE_TICKS);
    }

    public void setIdleTicks(int ticks) {
        if (!this.level().isClientSide) this.entityData.set(IDLE_TICKS, ticks);
    }

    @Override
    public void updateAnimations() {
        super.updateAnimations();
        this.anim_idle.animateWhen(this.getAnimationState().equals("idle"), this.tickCount);
    }
}
