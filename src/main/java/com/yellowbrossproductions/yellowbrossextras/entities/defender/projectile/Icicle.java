package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.List;

public class Icicle extends Entity {
    private static final EntityDataAccessor<BlockPos> COLLISION_POS = SynchedEntityData.defineId(Icicle.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> TIMER = SynchedEntityData.defineId(Icicle.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DELAY = SynchedEntityData.defineId(Icicle.class, EntityDataSerializers.INT);
    LivingEntity shooter = null;
    public boolean shotByDefender = false;
    LivingEntity target = null;

    public Icicle(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public Icicle(Level level, LivingEntity shooter, Vec3 spawnPos, BlockPos collisionPos) {
        super(YEEntityTypes.Icicle.get(), level);
        this.setOwner(shooter);
        this.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        this.setCollisionPos(collisionPos.getX(), collisionPos.getZ());
        this.shotByDefender = true;
    }

    public Icicle(PlayMessages.SpawnEntity packet, Level level) {
        this(YEEntityTypes.Icicle.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLLISION_POS, BlockPos.ZERO);
        this.entityData.define(TIMER, 100);
        this.entityData.define(DELAY, 0);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public BlockPos getCollisionPos() {
        return this.entityData.get(COLLISION_POS);
    }

    public void setCollisionPos(int x, int z) {
        this.entityData.set(COLLISION_POS, new BlockPos(x, this.level.getMaxBuildHeight(), z));
    }

    public int getTrueTimer() {
        return this.entityData.get(TIMER) + this.getDelay();
    }

    public void setTimer(int input) {
        this.entityData.set(TIMER, input);
    }

    public int getDelay() {
        return this.entityData.get(DELAY);
    }

    public void setDelayAndTarget(int input, LivingEntity target) {
        this.entityData.set(DELAY, input * 5);
        this.target = target;
    }

    @Nullable
    public LivingEntity getOwner() {
        return this.shooter;
    }

    public void setOwner(LivingEntity owner) {
        this.shooter = owner;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.shotByDefender) {
            if (this.tickCount < 5 + this.getDelay() && this.target != null && this.target.isAlive() && !this.target.isRemoved()) {
                this.setCollisionPos(this.target.getBlockX(), this.target.getBlockZ());
            }
            if (this.tickCount >= 5 + this.getDelay()) {
                BlockPos.MutableBlockPos yPosition = this.getCollisionPos().mutable();
                while (yPosition.getY() > this.level.getMinBuildHeight() && !this.level.getBlockState(yPosition).getMaterial().blocksMotion()) {
                    yPosition.move(Direction.DOWN);
                }

                this.setPos(yPosition.getX() + 0.5, yPosition.getY() + 1, yPosition.getZ() + 0.5);
                if (this.level instanceof ServerLevel server) server.getChunkSource().broadcast(this, new ClientboundTeleportEntityPacket(this));
            }
        }

        if (this.tickCount == this.getTrueTimer()) {
            this.playSound(YESoundEvents.ENTITY_DEFENDER_ICETHROWER_HIT.get(), 2.0F, 1.0F);
            EntityUtil.makeCircleParticles(this.level, this.getPosition(0).add(0, 0.4, 0), ParticleTypes.POOF, 30, 1.0F, Vec3.ZERO, 0.0F);
            for(int i = 0; i < 10; ++i) {
                EntityUtil.makeAParticle(this.level, ParticleTypes.POOF, false, new Vec3(this.getX(), this.getY(), this.getZ()), new Vec3(0, this.random.nextGaussian() * 2, 0));
            }
            CameraShake.cameraShake(this.level, position(), 35, 0.05f, 0, 10);

            List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3),
                    p -> !p.isOnFire() && p.getType().isBlockDangerous(Blocks.POWDER_SNOW.defaultBlockState()));
            for (LivingEntity hit : entities) {
                if (hit.hurt(DamageSource.thrown(this, this.getOwner()), 8.0F)) {
                    hit.invulnerableTime = 0;
                    hit.hurt(DamageSource.FREEZE, 4.0F);
                    hit.addEffect(new MobEffectInstance(YEEffects.FROZEN.get(), 300, 0,  false, false, true));
                }
            }
        }

        if (this.tickCount > 140) this.discard();
    }
}
