package com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.minions;

import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.IsOryctolinAligned;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;

public class CarrotMinionEntity extends YExtrasMob implements IsOryctolinAligned, Enemy {

    public CarrotMinionEntity(EntityType<? extends YExtrasMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
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
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        double x = entityIn.getX() - this.getX();
        double y = 0.5;
        double z = entityIn.getZ() - this.getZ();
        this.setDeltaMovement(x * 3, this.isOnGround() ? y : this.getDeltaMovement().y(), z * 3);
        entityIn.hurtMarked = true;
        entityIn.setDeltaMovement(entityIn.getDeltaMovement().add(x * 1.5, y * 0.5, z * 1.5));
        this.playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
        return super.doHurtTarget(entityIn);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }
}
