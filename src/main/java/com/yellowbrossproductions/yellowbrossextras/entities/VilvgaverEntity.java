package com.yellowbrossproductions.yellowbrossextras.entities;

import com.google.common.collect.Sets;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.RegistryHandler;
import com.yellowbrossproductions.yellowbrossextras.util.LoopingSound;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class VilvgaverEntity extends YExtrasMob implements IEntityAdditionalSpawnData, Enemy {
    private static final EntityDataAccessor<Boolean> CHALLENGE = SynchedEntityData.defineId(VilvgaverEntity.class, EntityDataSerializers.BOOLEAN);
    int stuckTicks;
    int sayTicks;
    boolean canDestroyBlocks = false;
    Vec3 targetedJump;
    int targetJumpTime;
    int slowdownTime;
    int jumpWaitTime;
    double xMot;
    double yMot;
    double zMot;
    double speedBackUp = 1;

    public VilvgaverEntity(EntityType<? extends YExtrasMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.0D, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Mob && !(p_29932_ instanceof VilvgaverEntity);
        }));
        super.registerGoals();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CHALLENGE, false);
    }

    @Override
    public boolean causeFallDamage(float p_147187_, float p_147188_, DamageSource p_147189_) {
        this.canDestroyBlocks = false;
        return super.causeFallDamage(p_147187_, p_147188_, p_147189_);
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
        return 3.0F;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)1.0F)
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.FOLLOW_RANGE, 125.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag p_21484_) {
        super.addAdditionalSaveData(p_21484_);

        p_21484_.putBoolean("challenge", this.isChallenge());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag p_21450_) {
        super.readAdditionalSaveData(p_21450_);

        this.setChallenge(p_21450_.getBoolean("challenge"));
    }

    public boolean isChallenge() {
        return this.entityData.get(CHALLENGE);
    }

    public void setChallenge(boolean challenge) {
        this.entityData.set(CHALLENGE, challenge);
    }

    @Override
    public void tick() {
        int delay = YellowbrossExtrasConfig.vilvgaverChallenge_delayTime.get() * 20;
        if (this.isChallenge()) {
            if (this.tickCount < delay) {
                this.setDeltaMovement(0.0D, 0.0D, 0.0D);
            } else {
                if (this.speedBackUp < 1.0) {
                    this.speedBackUp += 0.025;
                }
            }
        }
        super.tick();

        if (this.isChallenge()) {
            if (this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).is(Blocks.BEDROCK)) {
                Player player = this.level.getNearestEntity(Player.class, TargetingConditions.DEFAULT, this, this.getX(), this.getY(), this.getZ(), this.getBoundingBox().inflate(150.0D));
                if (player != null) {
                    this.setPos(this.getX(), player.getY(), this.getZ());
                }
            }
            List<Player> list = this.level.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(90.0D));
            if (list.isEmpty() && this.tickCount > 20) {
                this.kill();
            }

            List<VilvgaverEntity> list2 = this.level.getEntitiesOfClass(VilvgaverEntity.class, this.getBoundingBox().inflate(100.0D), predicate -> {
                return predicate.isChallenge() && predicate != this;
            });
            if (!list2.isEmpty() && this.tickCount > 10) {
                this.kill();
            }

            CameraShakeEntity.cameraShake(this.level, position(), 35, 0.1f, 1, 0);
        }

        this.sayTicks--;
        if (this.targetedJump != null) {
            this.targetJumpTime += 1;
        } else {
            this.targetJumpTime = 0;
        }
        if (this.jumpWaitTime > 0) {
            this.jumpWaitTime += 1;
        } else {
            this.jumpWaitTime = 0;
        }

        for (Entity entity : level.getEntities(this, getBoundingBox().inflate(15.0F))) {
            if (EntityUtil.canHurtThisMob(entity, this) && entity instanceof LivingEntity && entity.isAlive() && EntityUtil.isMobNotInCreativeMode(entity) && !(entity instanceof VilvgaverEntity)) {
                double x = this.getX() - entity.getX();
                double y = this.getY() - entity.getY();
                double z = this.getZ() - entity.getZ();
                double d = Math.sqrt(x * x + y * y + z * z);
                if (this.tickCount > delay || !this.isChallenge()) {
                    if (this.distanceToSqr(entity) < 4.5D) {
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_ATTACK.get(), 0.6F, 1.0F);

                        if (this.sayTicks < 1) {
                            this.playSound(YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_SAY.get(), 2.0F, 1.0F);
                        }
                        this.sayTicks = 60;

                        entity.hurt(DamageSource.mobAttack(this).bypassArmor(), Float.MAX_VALUE);
                        entity.hurtMarked = true;
                        entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 0.4D, (-y / d * 0.4D) + 0.2D, -z / d * 0.4D));
                        entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 0.4D, (-y / d * 0.4D) + 0.2D, -z / d * 0.4D));
                        entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 0.4D, (-y / d * 0.4D) + 0.2D, -z / d * 0.4D));
                    }
                }
            }
        }

        if (this.getLeashHolder() != null && !(this.getLeashHolder() instanceof Player)) {
            this.getLeashHolder().kill();
        }

        if (this.getTarget() != null && this.getDeltaMovement().x != 0  && this.getDeltaMovement().z != 0) {
            if (this.getTarget().getY() > (this.getY() + 2)) {
                double jumpMult = this.getTarget().getY() - (this.getY() + 2);
                if ((this.distanceToSqr(this.getTarget().position().x, this.getY(), this.getTarget().position().z)) <= jumpMult * 3 * 3 &&
                        this.distanceToSqr(this.getTarget().position().x, this.getY(), this.getTarget().position().z) < 60.0D) {
                    if (this.targetedJump == null) {
                        this.targetedJump = new Vec3(this.getTarget().getX(), this.getTarget().getY(), this.getTarget().getZ());
                    }
                    if (YellowbrossExtrasConfig.vilvgaverChallenge_jumpDelay.get() * 20 > 0 && this.isChallenge() && this.jumpWaitTime < 1) {
                        this.jumpWaitTime = 1;
                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_WARN.get(), 6.0F, 1.0F);
                    }
                }
            }
        }
        if (this.targetedJump != null && (this.jumpWaitTime > YellowbrossExtrasConfig.vilvgaverChallenge_jumpDelay.get() * 20 || !this.isChallenge())) {
            if (!this.canDestroyBlocks) {
                this.playSound(YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_JUMP.get(), 6.0F, 1.0F);
                this.canDestroyBlocks = true;

                double x = getX() - this.targetedJump.x;
                double y = getY() - this.targetedJump.y;
                double z = getZ() - this.targetedJump.z;
                double d = Math.sqrt(x * x + y * y + z * z);
                float power = (float) 7.5F;
                double motionX = -(x / d * (double) power * 0.4D);
                double motionY = -(y / d * (double) power * 0.2D);
                double motionZ = -(z / d * (double) power * 0.4D);

                this.xMot = motionX;
                this.yMot = motionY;
                this.zMot = motionZ;
            }
            if (this.xMot != 0 || this.yMot != 0 || this.zMot != 0) {
                this.setDeltaMovement(this.xMot,
                        this.yMot,
                        this.zMot);
            }
            if (this.distanceToSqr(this.targetedJump) < 18.0D || this.targetJumpTime > 80) {
                this.targetedJump = null;
                this.jumpWaitTime = 0;

                this.xMot = 0;
                this.yMot = 0;
                this.zMot = 0;
            }
            this.verticalCollision = false;
        }

        if (this.getNavigation().getTargetPos() != null && this.jumpWaitTime < 1 && this.targetedJump == null) {
            double chargex = this.getX() - this.getNavigation().getTargetPos().getX();
            double chargey = this.getY() - this.getNavigation().getTargetPos().getY();
            double chargez = this.getZ() - this.getNavigation().getTargetPos().getZ();
            double charged = Math.sqrt(chargex * chargex + chargey * chargey + chargez * chargez);
            float power = 0.3F;
            double motionX = -(chargex / charged * (double) power * 0.2D);
            double motionY = -(chargey / charged * (double) power * 0.2D);
            double motionZ = -(chargez / charged * (double) power * 0.2D);
            if (this.getDeltaMovement().lengthSqr() < 3.5D) {
                double yGravity = this.getDeltaMovement().y;
                this.setDeltaMovement(this.getDeltaMovement().add(
                        motionX * Math.max(0.0, this.speedBackUp),
                        0.0D,
                        motionZ * Math.max(0.0, this.speedBackUp)).normalize().scale(0.5D * this.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                double xMove = this.getDeltaMovement().x;
                double zMove = this.getDeltaMovement().z;
                this.setDeltaMovement(xMove, yGravity, zMove);
            }

            if (this.horizontalCollision && this.distanceToSqr(this.getNavigation().getTargetPos().getX(), this.getNavigation().getTargetPos().getY(), this.getNavigation().getTargetPos().getZ()) > 9.0D) {
                this.stuckTicks++;
                if (this.stuckTicks > 40) {
                    double x = (int)(this.getX() + random.nextInt(6) - 3);
                    double z = (int)(this.getZ() + random.nextInt(6) - 3);
                    int worldHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int)x, (int)z);
                    if (worldHeight > level.getMinBuildHeight() + 1) {
                        this.setPos(x, worldHeight, z);
                        this.stuckTicks = 0;
                    }
                }
            } else {
                this.stuckTicks = 0;
            }
        }

        if (this.isChallenge()) {
            if (this.level.dimensionType().piglinSafe()) {
                final ObjectArrayList<BlockPos> toBlow = new ObjectArrayList<>();
                Set<BlockPos> set = Sets.newHashSet();
                int k;
                int l;
                for(int j = 0; j < 16; ++j) {
                    for(k = 0; k < 16; ++k) {
                        for(l = 0; l < 16; ++l) {
                            if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                                double d0 = (double)((float)j / 15.0F * 2.0F - 1.0F);
                                double d1 = (double)((float)k / 15.0F * 2.0F - 1.0F);
                                double d2 = (double)((float)l / 15.0F * 2.0F - 1.0F);
                                double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                                d0 /= d3;
                                d1 /= d3;
                                d2 /= d3;
                                float f = (float) (2.5F * (0.7F + this.level.random.nextFloat() * 0.6F));
                                double d4 = this.getX();
                                double d6 = this.getY();
                                double d8 = this.getZ();

                                for(float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                                    BlockPos blockpos = new BlockPos(d4, d6, d8);
                                    BlockState blockstate = this.level.getBlockState(blockpos);
                                    FluidState fluidstate = this.level.getFluidState(blockpos);
                                    if (!this.level.isInWorldBounds(blockpos)) {
                                        break;
                                    }

                                    if (!blockstate.isAir()) {
                                        set.add(blockpos);
                                    }

                                    d4 += d0 * 0.30000001192092896;
                                    d6 += d1 * 0.30000001192092896;
                                    d8 += d2 * 0.30000001192092896;
                                }
                            }
                        }
                    }
                }

                toBlow.addAll(set);
                ObjectListIterator var5 = toBlow.iterator();
                while(var5.hasNext()) {
                    BlockPos blockpos = (BlockPos)var5.next();
                    BlockState blockstate = this.level.getBlockState(blockpos);
                    net.minecraft.world.level.block.Block block = blockstate.getBlock();
                    if (!blockstate.isAir() && blockstate.is(Blocks.LAVA)) {
                        BlockPos blockpos1 = blockpos.immutable();
                        this.level.getProfiler().push("explosion_blocks");

                        this.level.setBlockAndUpdate(blockpos1, RegistryHandler.FROZEN_LAVA.get().defaultBlockState());
                        this.level.getProfiler().pop();
                    }
                }
            }
        }
    }

    @Override
    public boolean startRiding(Entity p_20330_) {
        return false;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
            if (this.canDestroyBlocks && !(this.isOnGround() || this.targetJumpTime < 20)) {
                int j1 = Mth.floor(this.getY());
                int i2 = Mth.floor(this.getX());
                int j2 = Mth.floor(this.getZ());
                boolean flag = false;

                for(int j = -2; j <= 2; ++j) {
                    for(int k2 = -2; k2 <= 2; ++k2) {
                        for(int k = 0; k <= 6; ++k) {
                            int l2 = i2 + j;
                            int l = j1 + k;
                            int i1 = j2 + k2;
                            BlockPos blockpos = new BlockPos(l2, l, i1);
                            BlockState blockstate = this.level.getBlockState(blockpos);
                            if (!YellowbrossExtrasConfig.vilvgaverChallenge_smashBlacklist.get().contains(Registry.BLOCK.getKey(blockstate.getBlock()).toString())) {
                                if (blockstate.canEntityDestroy(this.level, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                    flag = this.level.destroyBlock(blockpos, false, this) || flag;
                                }
                            }
                        }
                    }
                }

                if (flag) {
                    this.level.levelEvent((Player)null, 1022, this.blockPosition(), 0);
                }
            } else {
                if (this.horizontalCollision) {
                    if (this.random.nextInt(2) == 0) {
                        int j1 = Mth.floor(this.getY());
                        int i2 = Mth.floor(this.getX());
                        int j2 = Mth.floor(this.getZ());
                        boolean flag = false;

                        for(int j = -1; j <= 1; ++j) {
                            for(int k2 = -1; k2 <= 1; ++k2) {
                                for(int k = 0; k <= 3; ++k) {
                                    int l2 = i2 + j;
                                    int l = j1 + k;
                                    int i1 = j2 + k2;
                                    BlockPos blockpos = new BlockPos(l2, l, i1);
                                    BlockState blockstate = this.level.getBlockState(blockpos);
                                    if (!YellowbrossExtrasConfig.vilvgaverChallenge_smashBlacklist.get().contains(Registry.BLOCK.getKey(blockstate.getBlock()).toString())) {
                                        if (blockstate.canEntityDestroy(this.level, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                            flag = this.level.destroyBlock(blockpos, false, this) || flag;
                                        }
                                    }
                                }
                            }
                        }

                        if (flag) {
                            this.slowdownTime = 20;
                            if (this.isChallenge()) {
                                if (this.getTarget() != null) {
                                    if (this.random.nextInt(3) == 0 && !this.isInAttackSight(getTarget())) {
                                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_CRASH_CLOSE.get(), 3.2F, this.getVoicePitch());
                                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_CRASH_MEDIUM.get(), 4.0F, this.getVoicePitch());
                                        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_CRASH_DISTANT.get(), 10.0F, this.getVoicePitch());
                                        CameraShakeEntity.cameraShake(this.level, position(), 60, 0.1f, 0, 20);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 p_21075_, float p_21076_) {
        this.moveRelative(this.getFrictionInfluencedSpeed(1.5F), p_21075_);

        return super.handleRelativeFrictionAndCalculateMovement(p_21075_, p_21076_);
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    private float getFrictionInfluencedSpeed(float p_21331_) {
        return this.onGround ? this.getSpeed() * (0.21600002F / (p_21331_ * p_21331_ * p_21331_)) : this.flyingSpeed;
    }

    @Override
    public boolean canBeLeashed(Player p_21418_) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_ == DamageSource.OUT_OF_WORLD) {
            p_21017_ = Float.MAX_VALUE;
        }
        if (p_21016_.isProjectile() && this.isChallenge()) {
            if (p_21016_.getEntity() != null) {
                Entity entity = p_21016_.getEntity();
                double chargex = this.getX() - entity.getX();
                double chargey = this.getY() - entity.getY();
                double chargez = this.getZ() - entity.getZ();
                double charged = Math.sqrt(chargex * chargex + chargey * chargey + chargez * chargez);
                float power = 6.0F;
                double motionX = (chargex / charged * (double) power * 0.2D);
                double motionY = (chargey / charged * (double) power * 0.2D);
                double motionZ = (chargez / charged * (double) power * 0.2D);
                if (this.getDeltaMovement().lengthSqr() < 3.5D) {
                    this.setDeltaMovement(this.getDeltaMovement().add(motionX, motionY, motionZ));
                }
                double speedCheck = this.speedBackUp - YellowbrossExtrasConfig.vilvgaverChallenge_snowballPush.get();
                this.speedBackUp = Math.max(speedCheck, -YellowbrossExtrasConfig.vilvgaverChallenge_pushLimit.get());
            }
        }
        if (p_21017_ < 1000000000000.0F) {
            return false;
        }
        return super.hurt(p_21016_, p_21017_);
    }

    @Override
    public void die(DamageSource p_21014_) {
        this.deathTime = 19;
        super.die(p_21014_);
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return this.getTarget() == null && this.tickCount > 300;
    }

    @Override
    public boolean isPushedByFluid(FluidType type) {
        return false;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readSpawnData(FriendlyByteBuf additionalData) {
        Minecraft.getInstance().getSoundManager().play(new LoopingSound(this, YellowbrossExtrasSoundEvents.ENTITY_VILVGAVER_LOOP.get(), (float) (YellowbrossExtrasConfig.vilvgaver_ambienceVolume.get() * 1.0D)));
    }
}
