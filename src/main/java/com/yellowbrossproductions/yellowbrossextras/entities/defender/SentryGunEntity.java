package com.yellowbrossproductions.yellowbrossextras.entities.defender;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.client.model.animation.ICanBeAnimated;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.entities.YextrasEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.SentryBulletEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.ConverslinBulletEntity;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
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
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class SentryGunEntity extends YExtrasMob implements IsDefenderAligned {
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(SentryGunEntity.class, EntityDataSerializers.BOOLEAN);

    public AnimationState anim_shoot = new AnimationState();
    public AnimationState anim_intro = new AnimationState();
    public AnimationState anim_flying = new AnimationState();
    public AnimationState anim_mitosis = new AnimationState();

    int setupTicks = 60;
    int mitosisTicks = 11;
    int explodeTimer = YellowbrossExtrasConfig.defender_sentryGun_mitosisTimer.get() * 20;
    boolean mitosisInitiated = false;

    public SentryGunEntity(EntityType<? extends YExtrasMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ShootGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, IsDefenderAligned.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Enemy;
        }));
        super.registerGoals();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
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

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        return false;
    }

    @Override
    public void updateAnimations() {
        EntityUtil.animateWhen(this.anim_shoot, this.getAnimationState().equals("shoot"), this.tickCount);
        EntityUtil.animateWhen(this.anim_intro, this.getAnimationState().equals("intro"), this.tickCount);
        EntityUtil.animateWhen(this.anim_flying, this.getAnimationState().equals("flying"), this.tickCount);
        EntityUtil.animateWhen(this.anim_mitosis, this.getAnimationState().equals("mitosis"), this.tickCount);
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
        if (this.isActive()) this.setDeltaMovement(0.0, this.getDeltaMovement().y, 0.0);
        super.tick();

        if (!this.isActive() && this.setupTicks == 60) {
            if (!this.isOnGround()) this.setAnimationState("flying");
            else {
                this.setupTicks = 59;
                this.setAnimationState("intro");
            }
        }
        if (this.setupTicks < 60) this.setupTicks -= 1;
        if (this.setupTicks < 1) this.setActive(true);

        if (this.isActive() && this.isAlive()) {
            if (this.getTarget() != null && !this.getTarget().isRemoved() && this.getTarget().isAlive() && this.tickCount % 5 == 0 && !this.mitosisInitiated) {
                this.setAnimationState("none");
                this.setAnimationState("shoot");
                this.stopShootingSound(this.level);
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SENTRY_SHOOT.get(), 3.0F, 1.0F);
                this.performRangedAttack(this.getTarget());
            }
            explodeTimer--;
            if (explodeTimer < 1 && this.random.nextInt(4) == 0) {
                List<SentryGunEntity> list = this.level.getEntitiesOfClass(SentryGunEntity.class, this.getBoundingBox().inflate(50.0D), p -> {
                    return p != this;
                });
                if (list.size() < YellowbrossExtrasConfig.defender_sentryGun_mitosisCap.get() && !this.mitosisInitiated) {
                    if (!this.level.isClientSide) {
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SENTRY_MITOSIS.get(), 2.0F, 1.0F);
                        this.setAnimationState("mitosis");
                        this.mitosisInitiated = true;
                    }
                } else {
                    explodeTimer = 3 * 20;
                }
            }
            if (this.mitosisInitiated) {
                this.mitosisTicks -= 1;
                if (this.mitosisTicks < 1 && !this.level.isClientSide) {
                    this.dead = true;
                    this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SENTRY_POP.get(), 2.0F, 1.0F);
                    this.explode(5.0d);
                    this.makePopParticles();
                    CameraShakeEntity.cameraShake(this.level, position(), 35, 0.2f, 0, 10);
                    for (int i = 0; i < 2; i++) {
                        SentryGunEntity iGaveBirth = ModEntityTypes.SentryGun.get().create(this.level);
                        assert iGaveBirth != null;
                        iGaveBirth.setPos(this.getX(), this.getY() + 0.5D, this.getZ());

                        double mult = 1.0d;
                        iGaveBirth.setDeltaMovement(
                                (-0.5 + this.random.nextDouble()) * mult,
                                0.3d + (this.random.nextDouble() * 0.5d),
                                (-0.5 + this.random.nextDouble()) * mult
                        );

                        iGaveBirth.setTarget(this.getTarget());

                        if (this.level instanceof ServerLevel serverLevel) iGaveBirth.finalizeSpawn(serverLevel, this.level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.REINFORCEMENT, (SpawnGroupData)null, (CompoundTag)null);

                        if (this.getTeam() != null) {
                            this.level.getScoreboard().addPlayerToTeam(iGaveBirth.getStringUUID(),
                                    this.level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                        }
                        this.level.addFreshEntity(iGaveBirth);
                    }
                    this.discard();
                }
            }
        }
    }

    public void makePopParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    EntityUtil.makeCircleParticles(this.level, this, ParticleTypes.POOF, 30, 0.4D, 1.0F);
                    for(int i = 0; i < 30; ++i) {
                        packet.queueParticle(ParticleTypes.POOF, false, new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()), new Vec3(0, this.random.nextGaussian(), 0));
                    }
                    packet.queueParticle(ParticleTypes.EXPLOSION, false, new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()), new Vec3(0, 0, 0));

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public int getExplodeTimer() {
        return this.explodeTimer;
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level, size, this, Entity::isAlive);

        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = EntityUtil.canHurtThisMob(living, this) && !(living instanceof IsDefenderAligned);
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.hurt(DamageSource.explosion(this), 24.0F);
                }
            }
        }
    }

    public void performRangedAttack(LivingEntity target) {
        double x = this.getX();
        double z = this.getZ();
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

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        if (!this.isActive() && !this.isOnGround()) this.setAnimationState("flying");
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    public boolean isActive() {
        return this.entityData.get(ACTIVE);
    }

    public void setActive(boolean a) {
        this.entityData.set(ACTIVE, a);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.isProjectile() && source.getEntity() instanceof Player) {
            amount = Float.MAX_VALUE;
        }
        if (source.getEntity() instanceof IsDefenderAligned && EntityUtil.canHurtThisMob(source.getEntity(), this)) {
            return false;
        }
        return super.hurt(source, amount);
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
