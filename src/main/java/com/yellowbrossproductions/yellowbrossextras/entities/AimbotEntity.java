package com.yellowbrossproductions.yellowbrossextras.entities;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.CreeperInfection;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class AimbotEntity extends YExtrasMob implements Enemy {
    public AnimationState anim_shoot = new AnimationState();
    private static final EntityDataAccessor<Integer> KARMA = SynchedEntityData.defineId(AimbotEntity.class, EntityDataSerializers.INT);
    Vec3 stareAt = Vec3.ZERO;
    int shotTick = 0;

    public AimbotEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RuinTheGameForEveryoneGoal(this));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new StareAtTheSkyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, AimbotEntity.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 1, true, false, null));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 1.0d)
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D)
                .add(Attributes.FOLLOW_RANGE, 96.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(KARMA, 0);
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        return !(entity instanceof AimbotEntity) && super.canAttack(entity);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public int getKarma() {
        return this.entityData.get(KARMA);
    }

    public void setKarma(int i) {
        this.entityData.set(KARMA, i);
    }

    public boolean canFreeze() {
        return false;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource p_33579_) {
        return SoundEvents.SKELETON_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    protected void playStepSound(BlockPos p_32159_, BlockState p_32160_) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    protected SoundEvent getStepSound() {
        return SoundEvents.SKELETON_STEP;
    }

    @Override
    protected boolean isSunBurnTick() {
        return false;
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source != DamageSource.OUT_OF_WORLD) amount = 1.0F;
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));

        if (!this.level.isClientSide) this.heal(10.0F);

        if (this.getKarma() > 0 && this.random.nextInt(Math.max(YellowbrossExtrasConfig.aimbot_randomBanChance.get() - this.getKarma(), 1)) == 0 && !this.level.isClientSide) {
            this.stopShootingSound(this.level);
            this.playSound(YellowbrossExtrasSoundEvents.AIMBOT_BANNED.get(), 10.0F, 1.6F);
            EntityUtil.broadcastMessage(this.level, Component.translatable("yellowbrossextras.aimbotBanned" + (this.random.nextInt(11) + 1), this.getDisplayName()).withStyle(ChatFormatting.YELLOW));
            this.dead = true;
            this.discard();
        }
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    @Override
    public boolean wasKilled(ServerLevel level, LivingEntity living) {
        if (this.getKarma() != -1) this.setKarma(this.getKarma() + 1);
        return super.wasKilled(level, living);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Karma", this.getKarma());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setKarma(tag.getInt("Karma"));
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    public void stopShootingSound(Level world) {
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        ClientboundStopSoundPacket sstopsoundpacket = new ClientboundStopSoundPacket(YellowbrossExtrasSoundEvents.AIMBOT_SHOOT.get().getLocation(), SoundSource.HOSTILE);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(sstopsoundpacket);
        }
    }

    @Override
    public void updateAnimations() {
        EntityUtil.animateWhen(this.anim_shoot, this.getAnimationState().equals("shoot"), this.tickCount);
    }

    @Nullable
    public LivingEntity tryToFindTarget() {
        LivingEntity t = null;
        if (this.getTarget() != null && this.getTarget().isAlive()) t = getTarget();
        else {
            List<Mob> list = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(40.0D), p -> {
                return EntityUtil.canHurtThisMob(p, this) && !(p instanceof AimbotEntity) && p.isAlive();
            });
            if (!list.isEmpty()) t = list.get(0);
        }
        return t;
    }

    class StareAtTheSkyGoal extends Goal {
        final AimbotEntity aimbot;

        protected StareAtTheSkyGoal(AimbotEntity aimbot) {
            this.setFlags(EnumSet.of(Flag.LOOK));
            this.aimbot = aimbot;
        }

        @Override
        public boolean canUse() {
            return this.aimbot.getTarget() == null;
        }

        @Override
        public void tick() {
            int timer = this.aimbot.tickCount % 7;
            this.aimbot.getLookControl().setLookAt(this.aimbot.getX() + Mth.sin(timer) * 2, this.aimbot.getY() + 5, this.aimbot.getZ() + Mth.cos(timer) * 2, 999.0F, 100.0F);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    class RuinTheGameForEveryoneGoal extends Goal {
        final AimbotEntity aimbot;

        public RuinTheGameForEveryoneGoal(AimbotEntity aimbot) {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
            this.aimbot = aimbot;
        }

        @Override
        public boolean canUse() {
            return this.aimbot.getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.aimbot.shotTick > 0;
        }

        @Override
        public void start() {
            LivingEntity target = this.aimbot.getTarget();
            if (target != null) {
                this.aimbot.stopShootingSound(this.aimbot.level);
                this.aimbot.playSound(YellowbrossExtrasSoundEvents.AIMBOT_SHOOT.get(), 4.0F, 1.0F);
                this.aimbot.stareAt = target.getPosition(0).add(0, target.getEyeHeight(), 0);
                this.aimbot.getLookControl().setLookAt(this.aimbot.stareAt.x, this.aimbot.stareAt.y, this.aimbot.stareAt.z, 999.0F, 100.0F);
                this.aimbot.setOldPosAndRot();
                this.aimbot.setAnimationState("none");
                this.aimbot.setAnimationState("shoot");
                this.aimbot.shotTick = this.aimbot.tryToFindTarget() != null ? 1 : 10;

                EntityUtil.makeSimpleTrail(this.aimbot, ParticleTypes.CRIT, 80,
                        this.aimbot.getX(), this.aimbot.getY() + 1.5d, this.aimbot.getZ(),
                        target.getX(),
                        target.getEyeY(),
                        target.getZ());

                if (target.hurt(new EntityDamageSource("arrow", this.aimbot).setProjectile(), Float.MAX_VALUE)) {
                    target.invulnerableTime = 0;
                    if (target.isDeadOrDying()) target.setDeltaMovement(target.position().subtract(this.aimbot.position()));
                }
            }
        }

        @Override
        public void tick() {
            this.aimbot.shotTick--;
            this.aimbot.setOldPosAndRot();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
