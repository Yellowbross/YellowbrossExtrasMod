package com.yellowbrossproductions.yellowbrossextras.entities.defender;

import com.yellowbrossproductions.yellowbrossextras.client.model.animation.ICanBeAnimated;
import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.YextrasEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.StareAtDefenderGoal;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.*;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase1.*;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.defender.phase2.*;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.*;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import com.yellowbrossproductions.yellowbrossextras.util.defender.AttacksPart1;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
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
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class DefenderEntity extends PathfinderMob implements ICanBeAnimated, YextrasEntity, IsDefenderAligned {
    int frame;
    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_10));
    private static final EntityDataAccessor<Boolean> SHOULD_SHOW_BOSSBAR = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WEAPON_TO_SHOW = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PHASE_LIMIT = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SHAKE_MULTIPLIER = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> KILLED_BY_VOID = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOULD_HIDE_ALL_HATS = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHOULD_IMMEDIATELY_TURN = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> CHAINSAW_LOOK_X = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> SECOND_HAT = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> STRETCH = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> USING_CUSTOM_RENDER = SynchedEntityData.defineId(DefenderEntity.class, EntityDataSerializers.INT);

    public AnimationState anim_jump = new AnimationState();
    public AnimationState anim_defeated = new AnimationState();

    // Phase 1
    public AnimationState anim_saws = new AnimationState();
    public AnimationState anim_sword = new AnimationState();
    public AnimationState anim_axes = new AnimationState();
    public AnimationState anim_boomerang = new AnimationState();
    public AnimationState anim_spikes = new AnimationState();
    public AnimationState anim_spikes_land = new AnimationState();
    public AnimationState anim_spikes_slam = new AnimationState();
    public AnimationState anim_shurikens = new AnimationState();
    public AnimationState anim_chainsaw = new AnimationState();
    public AnimationState anim_claws_start = new AnimationState();
    public AnimationState anim_claws_continue = new AnimationState();
    public AnimationState anim_claws_end = new AnimationState();
    public AnimationState anim_excalibur = new AnimationState();
    public AnimationState anim_claws_punch = new AnimationState();

    // Phase 2
    public AnimationState anim_ratatatabow = new AnimationState();
    public AnimationState anim_ratatatabow2 = new AnimationState();
    public AnimationState anim_poisondarts = new AnimationState();
    public AnimationState anim_forcegun = new AnimationState();
    public AnimationState anim_snipe = new AnimationState();
    public AnimationState anim_sentryguns = new AnimationState();
    public AnimationState anim_icethrower = new AnimationState();
    public AnimationState anim_witherbazooka = new AnimationState();
    public AnimationState anim_creepergun = new AnimationState();
    public AnimationState anim_flamethrower = new AnimationState();

    public int cooldown_saws;
    public int cooldown_axes;
    public int cooldown_boomerang;
    public int cooldown_spikes;
    public int cooldown_shurikens;
    public int cooldown_chainsaw;
    public int cooldown_claws;

    public int cooldown_ratatatabow;
    public int cooldown_poisondarts;
    public int cooldown_snipe;
    public int cooldown_sentryguns;
    public int cooldown_icethrower;
    public int cooldown_witherbazooka;
    public int cooldown_creepergun;

    public int attackType;
    public int attackTicks;

    public int attackTicks2;

    public int jumpTicks;
    private int inWaterJumpTicks;

    public int attack_saws = 1;
    public int attack_jump = 2;
    public int attack_sword = 3;
    public int attack_axes = 4;
    public int attack_boomerang = 5;
    public int attack_spikes = 6;
    public int attack_shurikens = 7;
    public int attack_chainsaw = 8;
    public int attack_claws = 9;
    public int attack_excalibur = 10;

    public int attack_ratatatabow = 11;
    public int attack_poisondarts = 12;
    public int attack_forcegun = 13;
    public int attack_snipe = 14;
    public int attack_sentryguns = 15;
    public int attack_icethrower = 16;
    public int attack_witherbazooka = 17;
    public int attack_creepergun = 18;
    public int attack_flamethrower = 19;

    public double chargeX;
    double chargeY;
    public double chargeZ;
    public int throwTimes = 1;
    public boolean jumpAttacking = false;
    public float damageTaken = 0;
    public double laserX;
    public double laserY;
    public double laserZ;
    public boolean shouldContinueAttacking = false;
    public int meleeAttackType = 0;
    public LivingEntity clawsTarget = null;
    public boolean itsTimeToClawTarget = false;
    int jumpStuckTicks;
    int setHealthIFrames;
    public int deathAttackTicks;
    public double stareYOffsetter = 0;
    public int lerpChainsawSteps;
    public float lerpChainsawX;
    public int clawsPunchTimer = 0;
    public double averageXCord = 0;
    public double averageZCord = 0;
    public double stretchHelper = 0;
    int guysKilled = 0;
    int gagTimer = 0;
    public int slamTicks;

    public DefenderEntity(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        // this.goalSelector.addGoal(0, new SentryGunsGoal(this));
        this.goalSelector.addGoal(0, new RatatatabowGoal(this));

        this.goalSelector.addGoal(0, new ExcaliburGoal(this));
        this.goalSelector.addGoal(0, new ClawsGoal(this));
        this.goalSelector.addGoal(0, new ChainsawGoal(this));
        this.goalSelector.addGoal(0, new ShurikensGoal(this));
        this.goalSelector.addGoal(0, new SpikesGoal(this));
        this.goalSelector.addGoal(0, new BoomerangGoal(this));
        this.goalSelector.addGoal(0, new AxesGoal(this));
        this.goalSelector.addGoal(0, new SwordGoal(this));
        this.goalSelector.addGoal(0, new SawsGoal(this));

        this.goalSelector.addGoal(0, new DefeatedGoal());
        this.goalSelector.addGoal(0, new JumpAwayGoal(this));
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(4, new CustomMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, IsDefenderAligned.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Enemy;
        }));
        super.registerGoals();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                .add(Attributes.MAX_HEALTH, 250.0D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 125.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D)
                .add(ForgeMod.SWIM_SPEED.get(), 2.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOULD_SHOW_BOSSBAR, true);
        this.entityData.define(ANIMATION_STATE, 0);
        this.entityData.define(WEAPON_TO_SHOW, 0);
        this.entityData.define(PHASE, 1);
        this.entityData.define(PHASE_LIMIT, 1);
        this.entityData.define(SHAKE_MULTIPLIER, 0);
        this.entityData.define(KILLED_BY_VOID, false);
        this.entityData.define(SHOULD_HIDE_ALL_HATS, false);
        this.entityData.define(SHOULD_IMMEDIATELY_TURN, false);
        this.entityData.define(CHAINSAW_LOOK_X, 0.0F);
        this.entityData.define(SECOND_HAT, 0);
        this.entityData.define(STRETCH, 0.0F);
        this.entityData.define(USING_CUSTOM_RENDER, 0);
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        if (this.jumpAttacking) {
            if (this.attackType == attack_spikes) {
                this.setAnimationState(7);
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.7F);
                this.playSound(YellowbrossExtrasSoundEvents.HUGE_EXPLOSION.get(), 3.0F, 1.0F);
                if (!this.level.isClientSide) {
                    this.level.explode(this, this.getX(), this.getY() + 0.3, this.getZ(), 6.0F, Explosion.BlockInteraction.NONE);
                    this.level.explode(this, this.getX(), this.getY() + 0.3, this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
                    this.level.explode(this, this.getX(), this.getY() + 0.3, this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
                }
                this.performSpellCasting(true);
                int thing = 2;
                this.performSpellCasting2(thing);
                CameraShakeEntity.cameraShake(this.level, position(), 50, 0.3f, 0, 10);
                this.makeSpikeParticles();
                // this.fireCircleProjectiles(this.level, this, 10, 0.4D, 1.0F);
                this.jumpTicks = 20;
                this.stretchHelper = 0;
                this.setStretch(0);
                averageXCord = 0;
                averageZCord = 0;
            }
            if (this.attackType == attack_claws) {
                if (this.clawsTarget != null) {
                    if (this.distanceToSqr(this.clawsTarget) < 7.5D) {
                        if (this.clawsTarget.hurt(DamageSource.mobAttack(this), 10.0F)) {
                            clawsPunchTimer = 13;
                            double x = this.getX() - this.clawsTarget.getX();
                            double y = this.getY() - this.clawsTarget.getY();
                            double z = this.getZ() - this.clawsTarget.getZ();
                            double d = Math.sqrt(x * x + y * y + z * z);

                            this.setAnimationState(15);
                            this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, 0.8F);
                            CameraShakeEntity.cameraShake(this.level, position(), 30, 0.1f, 0, 15);
                            this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SMACK.get(), 2.0F, this.getVoicePitch());
                            this.clawsTarget.invulnerableTime = 0;
                            this.clawsTarget.hurtMarked = true;
                            double mult = 3.5d;
                            this.clawsTarget.setDeltaMovement(
                                    this.clawsTarget.getDeltaMovement().add(-x / d * mult,
                                            (-y / d * 0.4D) + 0.5D,
                                            -z / d * mult));
                        }
                    }
                }
                this.shouldContinueAttacking = false;
            }
            this.jumpAttacking = false;
        }
        return false;
    }

    @Override
    protected void playStepSound(BlockPos p_20135_, BlockState p_20136_) {
        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_STEP.get(), 0.5F, 1.0F);
    }

    public int getFrame() {
        return frame;
    }

    @Override
    public boolean hasLineOfSight(Entity p_147185_) {
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

    public boolean isInAttackSight(Entity p_147185_) {
        if (p_147185_.level != this.level) {
            return false;
        } else {
            Vec3 vec3 = new Vec3(this.getX(), this.getEyeY(), this.getZ());
            Vec3 vec31 = new Vec3(p_147185_.getX(), p_147185_.getEyeY(), p_147185_.getZ());
            if (vec31.distanceTo(vec3) > 128.0D) {
                return false;
            } else {
                return this.level.clip(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)).getType() == HitResult.Type.MISS;
            }
        }
    }

    @Override
    public float getStepHeight() {
        return 1.0F;
    }

    public boolean isCarnageAttack() {
        return this.attackType == attack_excalibur;
    }

    @Nullable
    public LivingEntity tryToFindTarget() {
        LivingEntity t = null;
        if (this.getTarget() != null && this.getTarget().isAlive()) {
            t = getTarget();
        } else {
            List<Mob> list = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(40.0D), p -> {
                return p instanceof Enemy && EntityUtil.canHurtThisMob(p, this) && this.isInAttackSight(p) && p.isAlive();
            });
            if (!list.isEmpty()) {
                t = list.get(0);
            }
        }
        return t;
    }

    @Override
    public void tick() {
        if (this.getPhase() == 1) {
            this.cooldown_saws--;
            this.cooldown_axes--;
            this.cooldown_boomerang--;
            this.cooldown_spikes--;
            this.cooldown_shurikens--;
            this.cooldown_chainsaw--;
            this.cooldown_claws--;
        } else if (this.getPhase() == 2) {
            this.cooldown_ratatatabow--;
            this.cooldown_poisondarts--;
            this.cooldown_snipe--;
            this.cooldown_sentryguns--;
            this.cooldown_icethrower--;
            this.cooldown_witherbazooka--;
            this.cooldown_creepergun--;
        }

        if (this.attackType > 0) {
            this.attackTicks += 1;
        } else {
            this.attackTicks = 0;
        }

        if (this.attackTicks2 > 0) {
            this.attackTicks2 += 1;
        }

        this.jumpTicks--;
        this.setHealthIFrames--;
        this.clawsPunchTimer--;
        this.gagTimer--;

        if (this.gagTimer == 0 && this.guysKilled >= 10) {
            int randomGag = this.random.nextInt(3);
            this.guysKilled = 0;
            if (randomGag == 0) {
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_GAG1.get(), 3.0F, 1.0F);
            } else if (randomGag == 1) {
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_GAG2.get(), 3.0F, 1.0F);
            } else {
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_GAG3.get(), 3.0F, 1.0F);
            }
        }
        if (this.gagTimer > 8 && this.guysKilled >= 7) {
            if (this.random.nextInt(4) == 0) {
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CARNAGE.get(), 2.0F, 1.0F);
            }
        }

        if (this.getLeashHolder() != null && !(this.getLeashHolder() instanceof Player)) {
            this.getLeashHolder().kill();
        }

        if (this.isInWater()) {
            this.inWaterJumpTicks += 1;
        } else {
            this.inWaterJumpTicks = 0;
        }

        if (this.jumpAttacking || this.shouldContinueAttacking) {
            this.jumpStuckTicks += 1;
        } else {
            this.jumpStuckTicks = 0;
        }

        if ((this.jumpAttacking || this.shouldContinueAttacking) && (this.inWaterJumpTicks > 20 || (this.jumpStuckTicks > 100))) {
            this.jumpAttacking = false;
            this.shouldContinueAttacking = false;
        }

        if (this.isAlive()) {
            if (this.getPhase() == 1) AttacksPart1.tickPhase1Attacks(this);
            if (this.getPhase() == 2) AttacksPart1.tickPhase2Attacks(this);

            if (this.attackType == attack_jump) {
                if (this.attackTicks == 4) {
                    this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_JUMP.get(), 2.0F, 1.0F);
                    if (this.getTarget() != null) {
                        double x = this.getX() - this.getTarget().getX();
                        double y = this.getY() - this.getTarget().getY();
                        double z = this.getZ() - this.getTarget().getZ();
                        double d = Math.sqrt(x * x + y * y + z * z);
                        float power = (float) 6.5F;
                        double motionX = (x / d * (double) power * 0.2D);
                        double motionY = (y / d * (double) power * 0.2D);
                        double motionZ = (z / d * (double) power * 0.2D);
                        this.setDeltaMovement(motionX, 0.5D, motionZ);
                    }
                }
            }

            if (this.doesAttackMeetNormalRequirements() && this.getTarget() != null && this.distanceTo(getTarget()) < 3.0D && this.meleeAttackType == 0) {
                this.meleeAttackType = 1;
                if ((this.random.nextInt(5) == 0 || this.cooldown_saws >= 140) && this.getPhase() == 1 && this.cooldown_claws < 1) {
                    if (this.getTarget() != null && (this.getTarget().getMaxHealth() >= 40.0F || this.getTarget() instanceof Player)) {
                        this.meleeAttackType = 2;
                    }
                }
            }
        }
        if (!this.isRemoved()) {
            if (this.deathAttackTicks > 0) {
                this.deathAttackTicks += 1;

                if (this.getPhase() == 0) {
                    if (this.getTarget() != null) {
                        this.lookAtWhileDead(this.getTarget(), 100.0F, 100.0F);
                    }

                    if (this.deathAttackTicks == 91) {
                        this.playSound(SoundEvents.UI_BUTTON_CLICK, 10000.0F, 1.0F);
                        EntityUtil.broadcastMessage(this.level, Component.translatable("yellowbrossextras.defenderLeaveGame", this.getDisplayName()).withStyle(ChatFormatting.YELLOW));
                        super.die(DamageSource.GENERIC);
                        if (!this.level.isClientSide()) {
                            this.remove(RemovalReason.KILLED);
                        }
                    }
                }

                if (this.getPhase() == 1) {
                    this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
                    if (this.deathAttackTicks == 10) {
                        this.setWeaponToShow(8);
                    }

                    if (this.deathAttackTicks == 20 + 1) {
                        this.stareYOffsetter = 4.1;
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.8F);
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.5F);
                        this.playSound(SoundEvents.GENERIC_EXPLODE, 3.0F, 0.7F);
                        CameraShakeEntity.cameraShake(this.level, position(), 50, 0.4f, 0, 15);
                        EntityUtil.makeCircleParticles(this.level, this, ParticleTypes.POOF, 50, 0.3D, 1.0F);

                        for (Entity entity : level.getEntities(this, getBoundingBox().inflate(15.0F))) {
                            if (EntityUtil.canHurtThisMob(entity, this) && entity instanceof LivingEntity && entity.isAlive()) {
                                double x = this.getX() - entity.getX();
                                double y = this.getY() - entity.getY();
                                double z = this.getZ() - entity.getZ();
                                double d = Math.sqrt(x * x + y * y + z * z);
                                if (this.distanceTo(entity) < 3.0D) {
                                    this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_BOOMERANG_HIT.get(), 2.0F, this.getVoicePitch());
                                    entity.hurt(DamageSource.mobAttack(this), 30.0F);
                                    entity.hurtMarked = true;
                                    entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 2.5D, (-y / d * 0.4D) + 0.8D, -z / d * 2.5D));
                                }
                            }
                        }
                    }
                    if (this.deathAttackTicks == 35 + 1) {
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 1.2F);
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.7F);
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SPIKE.get(), 3.0F, 0.7F);
                        CameraShakeEntity.cameraShake(this.level, position(), 50, 0.2f, 0, 15);

                        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
                        assert lightning != null;
                        lightning.setPos(this.getX(), this.getY() + 2.7D, this.getZ());
                        lightning.setVisualOnly(true);
                        this.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT, 3.0F, 1.0F);
                        this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 10000.0F, 1.0F);
                        this.level.addFreshEntity(lightning);
                    }
                    if (this.deathAttackTicks >= 35 + 1 && this.deathAttackTicks < 45 + 1) {
                        this.makeExcaliburLandParticles();
                    }

                    if (this.deathAttackTicks >= 59 + 1 && this.deathAttackTicks < 81 + 1) {
                        this.stareYOffsetter += 0.05D;
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_EARTH_RUMBLE.get(), 3.0F, this.getVoicePitch());
                        CameraShakeEntity.cameraShake(this.level, position(), 50, 0.02f, 0, 15);
                    }

                    if (this.deathAttackTicks == 81 + 1) {
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 1.2F);
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.7F);
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SPIKE.get(), 3.0F, 0.5F);
                        CameraShakeEntity.cameraShake(this.level, position(), 50, 0.3f, 0, 15);
                    }

                    if (this.deathAttackTicks == 102 + 1) {
                        this.stareYOffsetter = -0.4;
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.8F);
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CRASH.get(), 3.0F, 0.5F);
                        this.playSound(YellowbrossExtrasSoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.0F);
                        this.playSound(SoundEvents.GENERIC_EXPLODE, 3.0F, 0.7F);
                        CameraShakeEntity.cameraShake(this.level, position(), 30, 0.4f, 0, 15);
                        EntityUtil.makeCircleParticles(this.level, this, ParticleTypes.POOF, 50, 0.3D, 1.0F);
                        this.makeRockExplodeParticles();

                        if (!this.level.isClientSide) {
                            this.level.explode(this, this.getX(), this.getY() + 0.3, this.getZ(), 6.0F, Explosion.BlockInteraction.NONE);
                            this.level.explode(this, this.getX(), this.getY() + 0.3, this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
                            this.level.explode(this, this.getX(), this.getY() + 0.3, this.getZ(), 3.0F, Explosion.BlockInteraction.NONE);
                        }
                    }

                    if (this.deathAttackTicks == 114 + 1) {
                        this.stareYOffsetter = 0.0;
                    }

                    if (this.deathAttackTicks >= 125 + 1) {
                        this.setHealth(this.getHealth() + 2.0F);
                        this.setHealthIFrames = 10;
                    }

                    if (this.deathAttackTicks >= 125 + 1 && this.deathAttackTicks < 288 + 1) {
                        if ((this.deathAttackTicks - (125 + 1)) % 4 == 0) {
                            this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_QUICK_WHOOSH.get(), 3.0F, 1.0F);
                            CameraShakeEntity.cameraShake(this.level, position(), 30, 0.2f, 0, 8);
                        }

                        LivingEntity t = null;
                        List<Mob> list = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(50.0D), p -> {
                            return p instanceof Enemy && EntityUtil.canHurtThisMob(p, this) && ((p.getBlockStateOn() != Blocks.AIR.defaultBlockState())) && this.isInAttackSight(p);
                        });
                        if (this.getTarget() != null && (list.isEmpty() || this.distanceTo(this.getTarget()) < 25.0D)) {
                            t = getTarget();
                        } else {
                            if (!list.isEmpty()) {
                                t = list.get(0);
                            }
                        }
                        if (t != null && (!(t instanceof Player) || ((this.deathAttackTicks == 125 + 1) || (this.distanceTo(t) > 30.0D)))) {
                            double chargex = getX() - t.getX();
                            double chargey = getY() - (t.getY() + t.getEyeHeight());
                            double chargez = getZ() - t.getZ();
                            double charged = Math.sqrt(chargex * chargex + chargey * chargey + chargez * chargez);
                            float power = (float) 4.0F;
                            double motionX = -(chargex / charged * (double) power * 0.2D);
                            double motionY = -(chargey / charged * (double) power * 0.3D);
                            double motionZ = -(chargez / charged * (double) power * 0.2D);
                            setCharge(motionX, motionY, motionZ);
                        }
                        this.setDeltaMovement(chargeX, chargeY, chargeZ);

                        for (Entity entity : level.getEntities(this, getBoundingBox().inflate(100.0F))) {
                            if (EntityUtil.canHurtThisMob(entity, this) && entity instanceof LivingEntity && entity.isAlive() && EntityUtil.isMobNotInCreativeMode(entity) && !(entity instanceof Player)) {
                                double x = this.getX() - entity.getX();
                                double y = this.getY() - entity.getY();
                                double z = this.getZ() - entity.getZ();
                                double d = Math.sqrt(x * x + y * y + z * z);

                                double distance = this.distanceTo(entity) * 0.1D;

                                if (this.distanceTo(entity) < 40.0D && this.distanceTo(entity) > 5.0D && entity.invulnerableTime == 0) {
                                    if (entity.fallDistance > 10) {
                                        entity.fallDistance = 10;
                                    }
                                    entity.hurtMarked = true;
                                    entity.setDeltaMovement(entity.getDeltaMovement().add(
                                            (x / d * 0.5D) / distance,
                                            (y / d * 1.0D) / distance,
                                            (z / d * 0.5D) / distance));
                                }
                            }
                        }

                        for (Entity entity : level.getEntities(this, getBoundingBox().inflate(15.0F))) {
                            if (EntityUtil.canHurtThisMob(entity, this) && entity instanceof LivingEntity && entity.isAlive() && EntityUtil.isMobNotInCreativeMode(entity)) {
                                double x = this.getX() - entity.getX();
                                double y = this.getY() - entity.getY();
                                double z = this.getZ() - entity.getZ();
                                double d = Math.sqrt(x * x + y * y + z * z);
                                if (this.distanceTo(entity) < 5.0D) {
                                    if (entity.hurt(DamageSource.mobAttack(this).bypassArmor(), 25.0F)) {
                                        entity.hurt(DamageSource.mobAttack(this).bypassArmor(), 25.0F);
                                        entity.hurt(DamageSource.mobAttack(this).bypassArmor(), 25.0F);
                                        entity.hurt(DamageSource.mobAttack(this).bypassArmor(), 25.0F);
                                        entity.hurt(DamageSource.mobAttack(this).bypassArmor(), 25.0F);
                                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SWORD_HIT.get(), 2.0F, this.getVoicePitch() - 0.2F);
                                        CameraShakeEntity.cameraShake(this.level, position(), 10, 0.2f, 0, 8);
                                        entity.hurtMarked = true;
                                        if (this.distanceTo(entity) < 2.0D) {
                                            entity.setDeltaMovement(entity.getDeltaMovement().add(
                                                    (-0.5D + this.random.nextDouble()) * 3.0D,
                                                    (-0.5D + this.random.nextDouble()) * 3.0D,
                                                    (-0.5D + this.random.nextDouble()) * 3.0D));
                                        } else {
                                            entity.setDeltaMovement(entity.getDeltaMovement().add(
                                                    (x / d * 3.0D),
                                                    (y / d * 3.0D),
                                                    (z / d * 3.0D)));
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (this.deathAttackTicks >= 296 + 1) {
                        this.setSecondHat(this.getPhaseLimit() > 1 ? 2 : 0);
                    }

                    if (this.deathAttackTicks == 320 + 1) {
                        this.setDeltaMovement(0.0D, -1.0D, 0.0D);
                    }

                    if (this.deathAttackTicks >= 400 + 1 || (this.deathAttackTicks >= 320 + 1 && this.isOnGround())) {
                        this.deathAttackTicks = 0;
                        this.attackType = 0;
                        this.setPhase(this.getPhaseLimit() > 1 ? 2 : 0);
                        this.setSecondHat(0);
                        this.frame = 0;
                    }
                }
            }

            if (this.wasKilledByVoid()) {
                this.setAnimationState(0);
            }
            if (!this.wasKilledByVoid() && this.getHealth() <= 0 && this.deathAttackTicks == 0) {
                this.processDeath();
            }
        }

        super.tick();
        if (this.shouldImmediatelyTurn()) {
            this.setYRot(this.getYHeadRot());
            this.yBodyRot = this.getYRot();
        }
        this.frame++;

        if (this.tickCount % 20 == 1 && ((this.getTarget() != null && !this.getTarget().isAlive()) || this.getTarget() == null) && this.isAlive()) {
            if (!this.level.isClientSide) {
                this.heal(4.0F);
            }
        }
    }

    public void fireProjectile(LivingEntity p_82196_1_, float p_82196_2_, float inaccuracy) {
        AbstractArrow projectile1 = this.getArrow(Items.BOW.getDefaultInstance(), p_82196_2_);
        if (this.getMainHandItem().getItem() instanceof BowItem)
            projectile1 = ((BowItem)this.getMainHandItem().getItem()).customArrow(projectile1);
        projectile1.setBaseDamage(2.5D);
        projectile1.setCritArrow(true);

        Snowball projectile2 = EntityType.SNOWBALL.create(this.level);

        ThrownTrident projectile3 = EntityType.TRIDENT.create(this.level);
        assert projectile3 != null;
        projectile3.setCritArrow(true);

        ThrownPotion projectile4 = new ThrownPotion(this.level, this);

        double d0 = p_82196_1_.getX() - this.getX();
        double d1 = p_82196_1_.getY(0.3333333333333333D) - projectile1.getY() - (p_82196_1_.getBbHeight() / 2.0D);
        double d2 = p_82196_1_.getZ() - this.getZ();
        double d3 = (double)Mth.sqrt((float) (d0 * d0 + d2 * d2));
        float speed;
        if (this.distanceToSqr(p_82196_1_) > 22.5F) {
            speed = 2.5F;
        } else {
            speed = (float) (this.distanceToSqr(p_82196_1_) / 9);
        }
        assert projectile2 != null;

        projectile1.setOwner(this);
        projectile2.setOwner(this);
        projectile3.setOwner(this);
        projectile4.setOwner(this);

        projectile1.setPierceLevel((byte) 50);
        projectile3.setPierceLevel((byte) 50);
        projectile1.setCritArrow(true);
        projectile3.setCritArrow(true);
        projectile1.setBaseDamage(3.0D);
        projectile3.setBaseDamage(3.0D);

        projectile4.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), Potions.STRONG_HARMING));

        projectile1.shoot(d0, (d1 + d3 * (double)0.2F), d2, speed, inaccuracy);
        projectile2.shoot(d0, (d1 + d3 * (double)0.2F), d2, speed, inaccuracy);
        projectile3.shoot(d0, (d1 + d3 * (double)0.2F), d2, speed, inaccuracy);
        projectile4.shoot(d0, (d1 + d3 * (double)0.35F), d2, speed, inaccuracy);

        projectile1.setPos(this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ());
        projectile2.setPos(this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ());
        projectile3.setPos(this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ());
        projectile4.setPos(this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ());
        int rand = this.random.nextInt(4);
        if (rand == 0) {
            this.level.addFreshEntity(projectile1);
            this.playSound(SoundEvents.ARROW_SHOOT, 2.0F, this.getVoicePitch());
        } else if (rand == 1) {
            this.level.addFreshEntity(projectile2);
            this.playSound(SoundEvents.SNOWBALL_THROW, 2.0F, this.getVoicePitch());
        } else if (rand == 2) {
            this.level.addFreshEntity(projectile3);
            this.playSound(SoundEvents.TRIDENT_THROW, 2.0F, this.getVoicePitch());
        } else {
            this.level.addFreshEntity(projectile4);
            this.playSound(SoundEvents.WITCH_THROW, 2.0F, this.getVoicePitch());
        }
    }

    protected AbstractArrow getArrow(ItemStack p_213624_1_, float p_213624_2_) {
        return ProjectileUtil.getMobArrow(this, p_213624_1_, p_213624_2_);
    }


    public boolean shouldImmediatelyTurn() {
        return this.entityData.get(SHOULD_IMMEDIATELY_TURN);
    }

    public void setImmediateTurn(boolean turn) {
        if (!this.level.isClientSide) {
            this.entityData.set(SHOULD_IMMEDIATELY_TURN, turn);
        }
    }

    public int getCustomRender() {
        return this.entityData.get(USING_CUSTOM_RENDER);
    }

    public void setCustomRender(int turn) {
        if (!this.level.isClientSide) {
            this.entityData.set(USING_CUSTOM_RENDER, turn);
        }
    }

    @Override
    public boolean canBeLeashed(Player p_21418_) {
        return false;
    }

    public void lookAtWhileDead(Entity p_21392_, float p_21393_, float p_21394_) {
        double d0 = p_21392_.getX() - this.getX();
        double d2 = p_21392_.getZ() - this.getZ();
        double d1;
        if (p_21392_ instanceof LivingEntity) {
            LivingEntity livingentity = (LivingEntity)p_21392_;
            d1 = livingentity.getEyeY() - this.getEyeY();
        } else {
            d1 = (p_21392_.getBoundingBox().minY + p_21392_.getBoundingBox().maxY) / 2.0D - this.getEyeY();
        }

        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(Mth.atan2(d2, d0) * 57.2957763671875D) - 90.0F;
        float f1 = (float)(-(Mth.atan2(d1, d3) * 57.2957763671875D));
        this.setXRot(this.rotlerp(this.getXRot(), f1, p_21394_));
        this.setYRot(this.rotlerp(this.getYRot(), f, p_21393_));
        this.yHeadRot = this.rotlerp(this.getYRot(), f, p_21393_);
    }

    private float rotlerp(float p_21377_, float p_21378_, float p_21379_) {
        float f = Mth.wrapDegrees(p_21378_ - p_21377_);
        if (f > p_21379_) {
            f = p_21379_;
        }

        if (f < -p_21379_) {
            f = -p_21379_;
        }

        return p_21377_ + f;
    }

    public void lerpChainsawLookX(Vec3 vec3, float xMax, int steps) {
        double d0 = this.getX() - vec3.x;
        double d1 = this.getY() + 1.125 - vec3.y;
        double d2 = this.getZ() - vec3.z;
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f1 = (float)((Mth.atan2(d1, d3) * 57.2957763671875D));
        if (!this.level.isClientSide) {
            this.lerpChainsawX = this.rotlerp(this.getChainsawLookX(), f1, xMax);
            this.lerpChainsawSteps = steps;
        }
    }

    public float getChainsawLookX() {
        return this.entityData.get(CHAINSAW_LOOK_X);
    }

    @Override
    public void setHealth(float p_21154_) {
        float healthValue = p_21154_ - this.getHealth();
        if (healthValue < 0) {
            if (Math.abs(healthValue) > 15.0F && Math.abs(healthValue) < 1000000000000.0F) {
                p_21154_ = this.getHealth() - 15.0F;
            }
            if (this.setHealthIFrames < 1) {
                super.setHealth(p_21154_);
                this.setHealthIFrames = 10;
            }
        } else {
            super.setHealth(p_21154_);
        }
    }

    public void setLaserPosition(double x, double y, double z) {
        this.laserX = x;
        this.laserY = y;
        this.laserZ = z;
    }

    public void performSpellCasting(boolean circleOrNot) {
        LivingEntity livingentity = this.getTarget();
        if (livingentity != null) {
            double d0 = Math.min(livingentity.getY(), this.getY());
            double d1 = Math.max(livingentity.getY(), this.getY()) + 1.0D;
            float f = (float) Mth.atan2(livingentity.getZ() - this.getZ(), livingentity.getX() - this.getX());
            if (circleOrNot) {
                for (int i = 0; i < 5; ++i) {
                    float f1 = f + (float) i * (float) Math.PI * 0.4F;
                    this.createSpellEntity(this.getX() + (double) Mth.cos(f1) * 1.5D, this.getZ() + (double) Mth.sin(f1) * 1.5D, d0, d1, f1, 0);
                }

                for (int k = 0; k < 8; ++k) {
                    float f2 = f + (float) k * (float) Math.PI * 2.0F / 8.0F + 1.2566371F;
                    this.createSpellEntity(this.getX() + (double) Mth.cos(f2) * 2.5D, this.getZ() + (double) Mth.sin(f2) * 2.5D, d0, d1, f2, 3);
                }
            } else {
                for (int l = 0; l < 32; ++l) {
                    double d2 = 1.25D * (double) (l + 1);
                    int j = 1 * l;
                    this.createSpellEntity(this.getX() + (double) Mth.cos(f) * d2, this.getZ() + (double) Mth.sin(f) * d2, d0, d1, f, j);
                }
            }

        }
    }

    protected void performSpellCasting2(double thing) {
        LivingEntity livingentity = this.getTarget();
        if (livingentity != null) {
            double d0 = Math.min(livingentity.getY(), this.getY());
            double d1 = Math.max(livingentity.getY(), this.getY()) + 1.0D;
            float f = 1.0F;
            for (int l = 0; l < 32; ++l) {
                double d2 = 1.25D * (double) (l + 1);
                int j = 1 * l;
                this.createSpellEntity(this.getX() + (double) thing * d2, this.getZ() + (double) 0 * d2, d0, d1, f, j);
                this.createSpellEntity(this.getX() + (double) -thing * d2, this.getZ() + (double) 0 * d2, d0, d1, f, j);
                this.createSpellEntity(this.getX() + (double) 0 * d2, this.getZ() + (double) thing * d2, d0, d1, f, j);
                this.createSpellEntity(this.getX() + (double) 0 * d2, this.getZ() + (double) -thing * d2, d0, d1, f, j);
                this.createSpellEntity(this.getX() + (double) thing * d2, this.getZ() + (double) -thing * d2, d0, d1, f, j);
                this.createSpellEntity(this.getX() + (double) -thing * d2, this.getZ() + (double) thing * d2, d0, d1, f, j);
                this.createSpellEntity(this.getX() + (double) thing * d2, this.getZ() + (double) thing * d2, d0, d1, f, j);
                this.createSpellEntity(this.getX() + (double) -thing * d2, this.getZ() + (double) -thing * d2, d0, d1, f, j);
            }

        }
    }

    public void performSpellWarn2(double thing) {
        LivingEntity livingentity = this.getTarget();
        if (livingentity != null) {
            double d0 = Math.min(livingentity.getY(), this.getY());
            double d1 = Math.max(livingentity.getY(), this.getY()) + 1.0D;
            float f = 1.0F;
            for (int l = 0; l < 32; ++l) {
                double d2 = 1.25D * (double) (l + 1);
                int j = 1 * l;
                this.createSpikeWarnParticles(this.getX() + (double) thing * d2, this.getZ() + (double) 0 * d2, d0, d1, f, j);
                this.createSpikeWarnParticles(this.getX() + (double) -thing * d2, this.getZ() + (double) 0 * d2, d0, d1, f, j);
                this.createSpikeWarnParticles(this.getX() + (double) 0 * d2, this.getZ() + (double) thing * d2, d0, d1, f, j);
                this.createSpikeWarnParticles(this.getX() + (double) 0 * d2, this.getZ() + (double) -thing * d2, d0, d1, f, j);
                this.createSpikeWarnParticles(this.getX() + (double) thing * d2, this.getZ() + (double) -thing * d2, d0, d1, f, j);
                this.createSpikeWarnParticles(this.getX() + (double) -thing * d2, this.getZ() + (double) thing * d2, d0, d1, f, j);
                this.createSpikeWarnParticles(this.getX() + (double) thing * d2, this.getZ() + (double) thing * d2, d0, d1, f, j);
                this.createSpikeWarnParticles(this.getX() + (double) -thing * d2, this.getZ() + (double) -thing * d2, d0, d1, f, j);
            }

        }
    }

    private void createSpellEntity(double p_32673_, double p_32674_, double p_32675_, double p_32676_, float p_32677_, int p_32678_) {
        BlockPos blockpos = new BlockPos(p_32673_, p_32676_, p_32674_);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = this.level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(this.level, blockpos1, Direction.UP)) {
                if (!this.level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = this.level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(this.level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(p_32675_) - 1);

        if (flag) {
            this.level.addFreshEntity(new SpikeEntity(this.level, p_32673_, (double)blockpos.getY() + d0, p_32674_, p_32677_, p_32678_, this));
        }

    }

    private void createSpikeWarnParticles(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
        BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
        boolean flag = false;
        double dthing = 0.5D;

        do {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = level.getBlockState(blockpos1);
            if (blockstate.isFaceSturdy(level, blockpos1, Direction.UP)) {
                if (!level.isEmptyBlock(blockpos)) {
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShape(level, blockpos);
                    if (!voxelshape.isEmpty()) {
                        dthing = voxelshape.max(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.below();
        } while(blockpos.getY() >= Mth.floor(p_190876_5_) - 1);

        if (flag) {
            if (!this.level.isClientSide) {
                for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                    if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                        ParticlePacket packet = new ParticlePacket();

                        for(int i = 0; i < 2; ++i) {
                            double d0 = (-0.5 + this.random.nextGaussian()) / 4;
                            double d1 = (-0.5 + this.random.nextGaussian()) / 4;
                            double d2 = (-0.5 + this.random.nextGaussian()) / 4;
                            packet.queueParticle(ParticleTypes.ELECTRIC_SPARK, false, new Vec3(p_190876_1_ + 0.5F, (double)blockpos.getY() + dthing, p_190876_3_ + 0.5F), new Vec3(d0, d1, d2));
                        }

                        PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                    }
                }
            }
        }

    }

    public void fireCircleProjectiles(Level level, LivingEntity spawner, int amount, double y, float velocity) {
        final Random random = new Random();
        if (!level.isClientSide) {
            // code adapted from Mowzie's Mobs
            for (int i = 0; i < amount; i++) {
                float TAU = (float) (2 * StrictMath.PI);

                float yaw = i * (TAU / amount);
                float vy = random.nextFloat() * 0.1F - 0.05f;
                float vx = velocity * Mth.cos(yaw);
                float vz = velocity * Mth.sin(yaw);
                ConverslinBulletEntity bullet = new ConverslinBulletEntity(spawner.level, spawner, vx, vy, vz);

                bullet.setPos(spawner.getX(), spawner.getY() + y, spawner.getZ());
                bullet.setOwner(spawner);
                level.addFreshEntity(bullet);
            }
        }
    }

    public void increaseDefendersCarnage() {
        this.guysKilled += 1;
        this.gagTimer = 6 + this.random.nextInt(10);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        this.setShowBossBar(true);
        this.setPhase(1);
        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }

    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        return false;
    }

    public void setCharge(double x, double y, double z) {
        this.chargeX = x;
        this.chargeY = y;
        this.chargeZ = z;
    }

    public void setCharge(Vec3 vec) {
        this.setCharge(vec.x, vec.y, vec.z);
    }

    protected SoundEvent getHurtSound(DamageSource p_21239_) {
        return YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_HURT.get();
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (this.deathAttackTicks > 0) {
            return false;
        }
        if (p_21017_ > 15.0F) {
            if (!this.killedByCommand(p_21016_, p_21017_)) {
                p_21017_ = 15.0F;
            } else {
                this.setKilledByVoid(true);
            }
        }
        if (!this.killedByCommand(p_21016_, p_21017_)) {
            if (this.attackType == attack_claws && this.shouldContinueAttacking) {
                return false;
            }
            if (this.attackType == attack_saws) {
                return false;
            }
            if (this.attackType == attack_chainsaw) {
                p_21017_ /= 2;
            }
            if (this.attackType == attack_ratatatabow && this.attackTicks2 > 0) {
                p_21017_ = 0;
            }
        }
        if (p_21016_.getEntity() != null && p_21016_.getEntity() != this) {
            if (EntityUtil.canHurtThisMob(p_21016_.getEntity(), this) && EntityUtil.isMobNotInCreativeMode(p_21016_.getEntity()) && p_21016_.getEntity() instanceof LivingEntity) {
                this.setTarget((LivingEntity) p_21016_.getEntity());
            }
            this.damageTaken += p_21017_;
        }
        return super.hurt(p_21016_, p_21017_);
    }

    private boolean killedByCommand(DamageSource damageSource, float value) {
        if (damageSource != DamageSource.OUT_OF_WORLD) {
            return false;
        } else {
            return value >= 1000000000000.0F;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.getTarget() != null && this.getTarget().isAlive() && this.attackType == 0) {
            this.getLookControl().setLookAt(this.getTarget(), 30.0F, 30.0F);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);
        p_21484_.putBoolean("ShouldShowBossBar", this.shouldShowBossBar());
        p_21484_.putInt("Phase", this.getPhase());
        p_21484_.putInt("PhaseLimit", this.getPhaseLimit());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);
        this.setShowBossBar(p_21450_.getBoolean("ShouldShowBossBar"));
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }

        this.setPhase(p_21450_.getInt("Phase"));

        this.setPhaseLimit(p_21450_.getInt("PhaseLimit"));

        if (p_21450_.contains("Health", 99)) {
            this.entityData.set(DATA_HEALTH_ID, Mth.clamp(p_21450_.getFloat("Health"), 0.0F, this.getMaxHealth()));
        }
    }

    public boolean shouldShowBossBar() {
        return this.entityData.get(SHOULD_SHOW_BOSSBAR);
    }

    public void setShowBossBar(boolean showBossBar) {
        this.entityData.set(SHOULD_SHOW_BOSSBAR, showBossBar);
    }

    public int getWeaponToShow() {
        return this.entityData.get(WEAPON_TO_SHOW);
    }

    public void setWeaponToShow(int show) {
        if (!this.level.isClientSide) {
            this.entityData.set(WEAPON_TO_SHOW, show);
        }
    }

    public boolean wasKilledByVoid() {
        return this.entityData.get(KILLED_BY_VOID);
    }

    public void setKilledByVoid(boolean killedByVoid) {
        if (!this.level.isClientSide) {
            this.entityData.set(KILLED_BY_VOID, killedByVoid);
        }
    }

    public boolean shouldHideAllHats() {
        return this.entityData.get(SHOULD_HIDE_ALL_HATS);
    }

    public void setHideAllHats(boolean hideAllHats) {
        if (!this.level.isClientSide) {
            this.entityData.set(SHOULD_HIDE_ALL_HATS, hideAllHats);
        }
    }

    @Override
    protected void tickDeath() {
        if (this.wasKilledByVoid()) {
            super.tickDeath();
        }
        if (!this.isRemoved()) {
            if (this.getPhase() != 0) {
                this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            }
        }
    }

    @Override
    public void die(DamageSource p_21014_) {
        if (!this.wasKilledByVoid()) {
            this.processDeath();
        } else {
            super.die(p_21014_);
        }
    }

    private void processDeath() {
        List<Mob> list = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(50.0D), p -> {
            return p instanceof Enemy && EntityUtil.canHurtThisMob(p, this);
        });
        for (Mob entity : list) {
            Set<WrappedGoal> goals = entity.goalSelector.getAvailableGoals();
            boolean hasGoal = goals.stream().anyMatch(goal -> goal.getGoal() instanceof StareAtDefenderGoal);
            if (!hasGoal) entity.goalSelector.addGoal(0, new StareAtDefenderGoal(entity, this));
        }
        if (this.getPhase() == 1) {
            if (!this.level.isClientSide) {
                this.goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
            }
            if (this.lastHurtByPlayerTime > 0) {
                this.lastHurtByPlayerTime = 10000;
            }
            this.deathAttackTicks = 1;
            this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_PHASE_ENDED.get(), 3.0F, 1.0F);
            CameraShakeEntity.cameraShake(this.level, position(), 30, 0.3f, 0, 15);
            this.makePhaseEndParticles();
            this.setAnimationState(13);
            this.attackType = attack_excalibur;
        }
    }

    public void makePhaseEndParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 10; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian()) / 4;
                        double d1 = (-0.5 + this.random.nextGaussian()) / 4;
                        double d2 = (-0.5 + this.random.nextGaussian()) / 4;
                        packet.queueParticle(ParticleTypes.ANGRY_VILLAGER, false, new Vec3(
                                this.getRandomX(1.0D) + ((-0.5D + this.random.nextDouble()) * 2.5D),
                                this.getRandomY() + ((-0.5D + this.random.nextDouble()) * 1.5D),
                                this.getRandomZ(1.0D) + ((-0.5D + this.random.nextDouble()) * 2.5D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public void makeExcaliburLandParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 5; ++i) {
                        packet.queueParticle(ParticleTypes.TOTEM_OF_UNDYING, false, new Vec3(
                                this.getRandomX(1.0D) + ((-0.5D + this.random.nextDouble()) * 1.5D),
                                this.getRandomY() + ((-0.5D + this.random.nextDouble()) * 1.5D) + 2.4D,
                                this.getRandomZ(1.0D) + ((-0.5D + this.random.nextDouble()) * 1.5D)), new Vec3(0.0D, 2.0D, 0.0D));
                        packet.queueParticle(ParticleTypes.CRIT, false, new Vec3(
                                this.getRandomX(1.0D) + ((-0.5D + this.random.nextDouble()) * 1.5D),
                                this.getRandomY() + ((-0.5D + this.random.nextDouble()) * 1.5D) + 2.4D,
                                this.getRandomZ(1.0D) + ((-0.5D + this.random.nextDouble()) * 1.5D)), new Vec3(0.0D, 2.0D, 0.0D));
                        packet.queueParticle(ParticleTypes.ELECTRIC_SPARK, false, new Vec3(
                                this.getRandomX(1.0D) + ((-0.5D + this.random.nextDouble()) * 1.5D),
                                this.getRandomY() + ((-0.5D + this.random.nextDouble()) * 1.5D) + 2.4D,
                                this.getRandomZ(1.0D) + ((-0.5D + this.random.nextDouble()) * 1.5D)), new Vec3(0.0D, 2.0D, 0.0D));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public void makeRockExplodeParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 250; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d1 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d2 = (-0.5 + this.random.nextGaussian()) / 2;
                        packet.queueParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 200; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d1 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d2 = (-0.5 + this.random.nextGaussian()) / 2;
                        packet.queueParticle(ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 150; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d1 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d2 = (-0.5 + this.random.nextGaussian()) / 2;
                        packet.queueParticle(ParticleTypes.LARGE_SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    public void setCustomName(@Nullable Component p_20053_) {
        super.setCustomName(p_20053_);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.getPhase() != 0) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
        }
        this.bossEvent.setVisible(this.shouldShowBossBar());
    }

    public void startSeenByPlayer(ServerPlayer p_31483_) {
        super.startSeenByPlayer(p_31483_);
        this.bossEvent.addPlayer(p_31483_);
    }

    public void stopSeenByPlayer(ServerPlayer p_31488_) {
        super.stopSeenByPlayer(p_31488_);
        this.bossEvent.removePlayer(p_31488_);
    }

    public void setAnimationState(int input) {
        this.entityData.set(ANIMATION_STATE, input);
    }

    public void makeExplodeParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 3; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.EXPLOSION_EMITTER, false, new Vec3(this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public void makeSpikeParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    EntityUtil.makeCircleParticles(this.level, this, ParticleTypes.LARGE_SMOKE, 50, 0.4D, 1.0F);
                    for(int i = 0; i < 100; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 100; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.LARGE_SMOKE, false, new Vec3(this.getX(), this.getY() + this.getEyeHeight(), this.getZ()), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public void makeSawParticles1() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 3; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.CRIT, false, new Vec3(this.getRandomX(0.5D), this.getRandomY(), this.getRandomZ(0.5D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public void makeSawParticles2(Entity caught) {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 12; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.POOF, false, new Vec3(caught.getRandomX(0.5D), caught.getRandomY(), caught.getRandomZ(0.5D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public void makeSpinParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 20; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.POOF, false, new Vec3(this.getRandomX(0.5D), this.getY() + 1.0D, this.getRandomZ(0.5D)), new Vec3(d0, 0.0D, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return false;
    }

    @Override
    public AnimationState getAnimationState(String input) {
        return switch (input) {
            case "jump" -> anim_jump;
            case "defeated" -> anim_defeated;

            case "saws" -> anim_saws;
            case "sword" -> anim_sword;
            case "axes" -> anim_axes;
            case "boomerang" -> anim_boomerang;
            case "spikes" -> anim_spikes;
            case "spikes_land" -> anim_spikes_land;
            case "spikes_slam" -> anim_spikes_slam;
            case "shuriken_launcher" -> anim_shurikens;
            case "chainsaw" -> anim_chainsaw;
            case "claws_start" -> anim_claws_start;
            case "claws_continue" -> anim_claws_continue;
            case "claws_end" -> anim_claws_end;
            case "claws_punch" -> anim_claws_punch;
            case "excalibur" -> anim_excalibur;

            case "ratatatabow" -> anim_ratatatabow;
            case "ratatatabow2" -> anim_ratatatabow2;
            case "poisondarts" -> anim_poisondarts;
            case "forcegun" -> anim_forcegun;
            case "snipe" -> anim_snipe;
            case "sentryguns" -> anim_sentryguns;
            case "icethrower" -> anim_icethrower;
            case "witherbazooka" -> anim_witherbazooka;
            case "creepergun" -> anim_creepergun;
            case "flamethrower" -> anim_flamethrower;

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
                        this.anim_saws.start(this.tickCount);
                        break;
                    case 2 :
                        this.stopAllAnimationStates();
                        this.anim_jump.start(this.tickCount);
                        break;
                    case 3 :
                        this.stopAllAnimationStates();
                        this.anim_sword.start(this.tickCount);
                        break;
                    case 4 :
                        this.stopAllAnimationStates();
                        this.anim_axes.start(this.tickCount);
                        break;
                    case 5 :
                        this.stopAllAnimationStates();
                        this.anim_boomerang.start(this.tickCount);
                        break;
                    case 6 :
                        this.stopAllAnimationStates();
                        this.anim_spikes.start(this.tickCount);
                        break;
                    case 7 :
                        this.stopAllAnimationStates();
                        this.anim_spikes_land.start(this.tickCount);
                        break;
                    case 8 :
                        this.stopAllAnimationStates();
                        this.anim_shurikens.start(this.tickCount);
                        break;
                    case 9 :
                        this.stopAllAnimationStates();
                        this.anim_chainsaw.start(this.tickCount);
                        break;
                    case 10 :
                        this.stopAllAnimationStates();
                        this.anim_claws_start.start(this.tickCount);
                        break;
                    case 11 :
                        this.stopAllAnimationStates();
                        this.anim_claws_continue.start(this.tickCount);
                        break;
                    case 12 :
                        this.stopAllAnimationStates();
                        this.anim_claws_end.start(this.tickCount);
                        break;
                    case 13 :
                        this.stopAllAnimationStates();
                        this.anim_excalibur.start(this.tickCount);
                        break;
                    case 14 :
                        this.stopAllAnimationStates();
                        this.anim_defeated.start(this.tickCount);
                        break;
                    case 15 :
                        this.stopAllAnimationStates();
                        this.anim_claws_punch.start(this.tickCount);
                        break;
                    case 16 :
                        this.stopAllAnimationStates();
                        this.anim_ratatatabow.start(this.tickCount);
                        break;
                    case 17 :
                        this.stopAllAnimationStates();
                        this.anim_spikes_slam.start(this.tickCount);
                        break;
                    case 18 :
                        this.stopAllAnimationStates();
                        this.anim_ratatatabow2.start(this.tickCount);
                        break;
                    case 19 :
                        this.stopAllAnimationStates();
                        this.anim_poisondarts.start(this.tickCount);
                        break;
                    case 20 :
                        this.stopAllAnimationStates();
                        this.anim_forcegun.start(this.tickCount);
                        break;
                    case 21 :
                        this.stopAllAnimationStates();
                        this.anim_snipe.start(this.tickCount);
                        break;
                    case 22 :
                        this.stopAllAnimationStates();
                        this.anim_sentryguns.start(this.tickCount);
                        break;
                    case 23 :
                        this.stopAllAnimationStates();
                        this.anim_icethrower.start(this.tickCount);
                        break;
                    case 24 :
                        this.stopAllAnimationStates();
                        this.anim_witherbazooka.start(this.tickCount);
                        break;
                    case 25 :
                        this.stopAllAnimationStates();
                        this.anim_creepergun.start(this.tickCount);
                        break;
                    case 26 :
                        this.stopAllAnimationStates();
                        this.anim_flamethrower.start(this.tickCount);
                        break;
                }
            }
        }

        super.onSyncedDataUpdated(p_21104_);
    }

    public void stopAllAnimationStates() {
        this.anim_saws.stop();
        this.anim_jump.stop();
        this.anim_sword.stop();
        this.anim_axes.stop();
        this.anim_boomerang.stop();
        this.anim_spikes.stop();
        this.anim_spikes_land.stop();
        this.anim_spikes_slam.stop();
        this.anim_shurikens.stop();
        this.anim_chainsaw.stop();
        this.anim_claws_start.stop();
        this.anim_claws_continue.stop();
        this.anim_claws_end.stop();
        this.anim_excalibur.stop();
        this.anim_defeated.stop();
        this.anim_claws_punch.stop();
        this.anim_ratatatabow.stop();
        this.anim_ratatatabow2.stop();
        this.anim_poisondarts.stop();
        this.anim_forcegun.stop();
        this.anim_snipe.stop();
        this.anim_sentryguns.stop();
        this.anim_icethrower.stop();
        this.anim_witherbazooka.stop();
        this.anim_creepergun.stop();
        this.anim_flamethrower.stop();
    }

    public boolean doesJumpMeetNormalRequirements() {
        return this.attackType == 0 && this.getTarget() != null && this.isInAttackSight(getTarget()) && this.getTarget().isAlive() && !this.isRemoved() && this.deathAttackTicks <= 0;
    }

    public int getPhase() {
        return this.entityData.get(PHASE);
    }

    public void setPhase(int phase) {
        this.entityData.set(PHASE, phase);
    }

    public int getPhaseLimit() {
        return this.entityData.get(PHASE_LIMIT);
    }

    public void setPhaseLimit(int phase) {
        this.entityData.set(PHASE_LIMIT, phase);
    }

    public int getSecondHat() {
        return this.entityData.get(SECOND_HAT);
    }

    public void setSecondHat(int phase) {
        if (!this.level.isClientSide) {
            this.entityData.set(SECOND_HAT, phase);
        }
    }

    public int getShakeMultiplier() {
        return this.entityData.get(SHAKE_MULTIPLIER);
    }

    public void setShakeMultiplier(int shake) {
        if (!this.level.isClientSide) {
            this.entityData.set(SHAKE_MULTIPLIER, shake);
        }
    }

    public float getStretch() {
        return this.entityData.get(STRETCH);
    }

    public void setStretch(float stretch) {
        if (!this.level.isClientSide) {
            this.entityData.set(STRETCH, stretch);
        }
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public boolean shouldJumpAway(LivingEntity entity) {
        if (this.damageTaken >= 30.0F) {
            return true;
        }
        if (entity instanceof Mob) {
            return ((Mob) entity).getTarget() == this;
        }
        return false;
    }

    class JumpAwayGoal extends CustomAttackGoal {

        public JumpAwayGoal(DefenderEntity defender) {
            super(defender);
        }

        @Override
        public boolean canUse() {
            return doesJumpMeetNormalRequirements() && getTarget() != null && ((distanceTo(getTarget()) < 10.0D && random.nextInt(16) == 0) || damageTaken >= 30.0F) && isOnGround() && shouldJumpAway(getTarget());
        }

        @Override
        public void start() {
            setAnimationState(2);
            attackType = attack_jump;
        }

        @Override
        public boolean canContinueToUse() {
            return attackTicks <= 12 || (!isOnGround() && !isInWater());
        }

        @Override
        public void stop() {
            super.stop();
            if (damageTaken >= 30.0F) {
                damageTaken = 0;
            }
        }
    }

    public boolean doesAttackMeetNormalRequirements() {
        return this.attackType == 0 && this.getTarget() != null && this.isInAttackSight(this.getTarget()) && this.getTarget().isAlive() && !((this.damageTaken >= 30.0F) && this.isOnGround()) && !this.isRemoved() && this.deathAttackTicks <= 0;
    }

    class DefeatedGoal extends Goal {

        @Override
        public boolean canUse() {
            return doesAttackMeetNormalRequirements() && random.nextInt(8) == 0 && getPhase() == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            setAnimationState(14);
            playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_DEFEATED.get(), 2.0F, 1.0F);
            entityData.set(DATA_HEALTH_ID, 0.0F);
            if (lastHurtByPlayerTime > 0) {
                lastHurtByPlayerTime = 10000;
            }
            deathAttackTicks = 1;

            getNavigation().stop();

            LivingEntity entity = tryToFindTarget();
            if (entity != null) {
                getLookControl().setLookAt(entity, 100.0F, 100.0F);
            }

            navigation.stop();
        }
    }

    public void setChainsawLookX(float amount) {
        if (!level.isClientSide) {
            this.entityData.set(CHAINSAW_LOOK_X, amount);
        }
    }

    public int howShouldDefenderApproachHisTarget() {
        return switch (this.getPhase()) {
            default -> 1;
            case 2 -> 2;
        };
    }

    // code borrowed from Mowzie's Mobs
    private void circleEnemy(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset, float moveSpeedMultiplier) {
        int directionInt = direction ? 1 : 1;
        double t = directionInt * circleFrame * 0.5 * speed / radius + offset;
        Vec3 movePos = target.position().add(radius * Math.cos(t), 0, radius * Math.sin(t));
        this.getNavigation().moveTo(movePos.x(), movePos.y(), movePos.z(), speed * moveSpeedMultiplier);
    }

    class CustomMeleeAttackGoal extends MeleeAttackGoal {
        final DefenderEntity defender;
        int circleTime;

        public CustomMeleeAttackGoal(DefenderEntity defender, double speed, boolean followEvenIfNotSeen) {
            super(defender, speed, followEvenIfNotSeen);
            this.defender = defender;
        }

        @Override
        public void tick() {
            if (this.defender.getTarget() != null) {
                LivingEntity target = this.defender.getTarget();
                double d0 = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());

                if (this.defender.howShouldDefenderApproachHisTarget() == 1) {
                    super.tick();
                }
                if (this.defender.howShouldDefenderApproachHisTarget() == 2) {
                    if (this.defender.tickCount % 15 == 0) {
                        this.circleTime += 1;
                    }
                    this.mob.getLookControl().setLookAt(target, 100.0F, 30.0F);
                    this.defender.circleEnemy(target, 18, 1.2f, true, this.circleTime, Mth.cos((this.defender.tickCount / 15.0F)), 1);
                    this.checkAndPerformAttack(target, d0);
                }
            }
        }
    }
}
