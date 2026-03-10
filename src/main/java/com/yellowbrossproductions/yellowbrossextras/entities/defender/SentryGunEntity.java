package com.yellowbrossproductions.yellowbrossextras.entities.defender;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.ICanBeAnimated;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SentryBulletEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.ConverslinBulletEntity;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
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
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class SentryGunEntity extends PathfinderMob implements ICanBeAnimated, IsDefenderAligned {
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(SentryGunEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(SentryGunEntity.class, EntityDataSerializers.BOOLEAN);

    public AnimationState anim_shoot = new AnimationState();
    public AnimationState anim_intro = new AnimationState();
    public AnimationState anim_flying = new AnimationState();

    int setupTicks = 60;

    public SentryGunEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ShootGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, IsDefenderAligned.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Enemy;
        }));
        super.registerGoals();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMATION_STATE, 0);
        this.entityData.define(ACTIVE, false);
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
            case "shoot" -> anim_shoot;
            case "intro" -> anim_intro;
            case "flying" -> anim_flying;

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
                        this.anim_shoot.start(this.tickCount);
                        break;
                    case 2 :
                        this.stopAllAnimationStates();
                        this.anim_intro.start(this.tickCount);
                        break;
                    case 3 :
                        this.stopAllAnimationStates();
                        this.anim_flying.start(this.tickCount);
                        break;
                }
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.anim_shoot.stop();
        this.anim_intro.stop();
        this.anim_flying.stop();
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_HURT.get();
    }

    @Override
    public void tick() {
        this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
        super.tick();

        if (this.isOnGround() && !this.isActive()) {
            if (this.setupTicks == 60) {
                this.setupTicks = 59;
                this.setAnimationState(2);
            }
            if (this.setupTicks < 1) this.setActive(true);
        }
        if (this.setupTicks < 60) this.setupTicks -= 1;

        if (this.getTarget() != null && this.isActive() && this.isAlive()) {
            if (this.tickCount % 5 == 0) {
                this.setAnimationState(0);
                this.setAnimationState(1);
                this.stopShootingSound(this.level);
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SENTRY_SHOOT.get(), 3.0F, 1.0F);
                this.performRangedAttack(this.getTarget());
            }
        }
    }

    public void performRangedAttack(LivingEntity target) {
        double x = this.getX();
        double z = this.getZ();
        for (int i = 0; i < 5; ++i) {
            double y1 = this.getY() + 0.75D;

            double d0 = target.getEyeY();
            double d1 = target.getX() - x;
            double d2 = d0 - y1;
            double d3 = target.getZ() - z;
            double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;

            SentryBulletEntity bullet = new SentryBulletEntity(this.level, this, d1, d2, d3);

            bullet.setPos(x, y1, z);
            bullet.setOwner(this);
            this.level.addFreshEntity(bullet);
        }
    }

    public void stopShootingSound(Level world) {
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        ClientboundStopSoundPacket sstopsoundpacket = new ClientboundStopSoundPacket(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SENTRY_SHOOT.get().getLocation(), SoundSource.NEUTRAL);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(sstopsoundpacket);
        }
    }

    public boolean isActive() {
        return this.entityData.get(ACTIVE);
    }

    public void setActive(boolean a) {
        this.entityData.set(ACTIVE, a);
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
        }
    }
}
