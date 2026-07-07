package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class AbstractSnipingProjectile extends Entity {
    private static final EntityDataAccessor<BlockPos> COLLISION_POS = SynchedEntityData.defineId(AbstractSnipingProjectile.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Integer> MAX_TIME = SynchedEntityData.defineId(AbstractSnipingProjectile.class, EntityDataSerializers.INT);
    LivingEntity shooter = null;

    public AbstractSnipingProjectile(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(COLLISION_POS, BlockPos.ZERO);
        this.entityData.define(MAX_TIME, 2);
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

    public void setCollisionPos(BlockPos pos) {
        this.entityData.set(COLLISION_POS, pos);
    }

    public void setCollisionPos(int x, int y, int z) {
        this.entityData.set(COLLISION_POS, new BlockPos(x, y, z));
    }

    public int getMaxTime() {
        return this.entityData.get(MAX_TIME);
    }

    public void setMaxTime(int input) {
        this.entityData.set(MAX_TIME, input);
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

    public static class SnipingProjectileHitResult {
        public BlockHitResult blockHit;

        public final List<LivingEntity> entities = new ArrayList<>();

        public BlockHitResult getBlockHit() {
            return blockHit;
        }

        public void setBlockHit(HitResult rayTraceResult) {
            if (rayTraceResult.getType() == HitResult.Type.BLOCK)
                this.blockHit = (BlockHitResult) rayTraceResult;
        }

        public void addEntityHit(LivingEntity entity) {
            entities.add(entity);
        }
    }
}
