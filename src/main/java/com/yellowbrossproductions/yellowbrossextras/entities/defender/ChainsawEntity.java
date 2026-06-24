package com.yellowbrossproductions.yellowbrossextras.entities.defender;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// code borrowed + adapted from Mowzie's Mobs
public class ChainsawEntity extends Entity {
    public LivingEntity caster;
    public double endPosX, endPosY, endPosZ;
    public double collidePosX, collidePosY, collidePosZ;
    public double prevCollidePosX, prevCollidePosY, prevCollidePosZ;
    public float renderYaw, renderPitch;
    public boolean on = true;
    public Direction blockSide = null;

    private static final EntityDataAccessor<Float> YAW = SynchedEntityData.defineId(ChainsawEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Float> PITCH = SynchedEntityData.defineId(ChainsawEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> DURATION = SynchedEntityData.defineId(ChainsawEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> CASTER = SynchedEntityData.defineId(ChainsawEntity.class, EntityDataSerializers.INT);

    public float prevYaw;
    public float prevPitch;

    @OnlyIn(Dist.CLIENT)
    private Vec3[] attractorPos;

    public ChainsawEntity(EntityType<? extends ChainsawEntity> entityType, Level level) {
        super(entityType, level);
        noCulling = true;
        if (level.isClientSide) {
            attractorPos = new Vec3[] {new Vec3(0,0,0)};
        }
    }

    public ChainsawEntity(EntityType<? extends ChainsawEntity> type, Level world, LivingEntity caster, double x, double y, double z, float yaw, float pitch, int duration) {
        this(type, world);
        this.caster = caster;
        this.setYaw(yaw);
        this.setPitch(pitch);
        this.setDuration(duration);
        this.setPos(x, y, z);
        this.calculateEndPos();
        if (!world.isClientSide) {
            this.setCasterID(caster.getId());
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(YAW, 0F);
        this.entityData.define(PITCH, 0F);
        this.entityData.define(DURATION, 0);
        this.entityData.define(CASTER, -1);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public PushReaction getPistonPushReaction() {
        return PushReaction.IGNORE;
    }

    public float getYaw() {
        return getEntityData().get(YAW);
    }

    public void setYaw(float yaw) {
        getEntityData().set(YAW, yaw);
    }

    public float getPitch() {
        return getEntityData().get(PITCH);
    }

    public void setPitch(float pitch) {
        getEntityData().set(PITCH, pitch);
    }

    public int getDuration() {
        return getEntityData().get(DURATION);
    }

    public void setDuration(int duration) {
        getEntityData().set(DURATION, duration);
    }

    public int getCasterID() {
        return getEntityData().get(CASTER);
    }

    public void setCasterID(int id) {
        getEntityData().set(CASTER, id);
    }

    private void updateWithDefender() {
        if (this.caster != null) {
            this.setYaw((float) ((caster.yHeadRot + 90) * Math.PI / 180.0d));
            if (caster instanceof DefenderEntity) {
                this.setPitch((float) ((-((DefenderEntity) caster).getChainsawLookX()) * Math.PI / 180.0d));
            }
            Vec3 vecOffset1 = new Vec3(0, 0, 0.8).yRot((float) Math.toRadians(-caster.getYRot()));
            Vec3 vecOffset2 = new Vec3(0.8, 0, 0).yRot(-getYaw()).xRot(getPitch());
            this.xOld = caster.xOld;
            this.yOld = caster.yOld;
            this.zOld = caster.zOld;
            this.setPos(caster.getX(), caster.getY() + 1.125, caster.getZ());
        }
    }

    private void calculateEndPos() {
        double radius = 30.0D;
        if (level.isClientSide()) {
            endPosX = getX() + radius * Math.cos(renderYaw) * Math.cos(renderPitch);
            endPosZ = getZ() + radius * Math.sin(renderYaw) * Math.cos(renderPitch);
            endPosY = getY() + radius * Math.sin(renderPitch);
        }
        else {
            endPosX = getX() + radius * Math.cos(getYaw()) * Math.cos(getPitch());
            endPosZ = getZ() + radius * Math.sin(getYaw()) * Math.cos(getPitch());
            endPosY = getY() + radius * Math.sin(getPitch());
        }
    }

    public ChainsawHitResult raytraceEntities(Level world, Vec3 from, Vec3 to, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
        ChainsawHitResult result = new ChainsawHitResult();
        result.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (result.blockHit != null) {
            Vec3 hitVec = result.blockHit.getLocation();
            collidePosX = hitVec.x;
            collidePosY = hitVec.y;
            collidePosZ = hitVec.z;
            blockSide = result.blockHit.getDirection();
        } else {
            collidePosX = endPosX;
            collidePosY = endPosY;
            collidePosZ = endPosZ;
            blockSide = null;
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(Math.min(getX(), collidePosX), Math.min(getY(), collidePosY), Math.min(getZ(), collidePosZ), Math.max(getX(), collidePosX), Math.max(getY(), collidePosY), Math.max(getZ(), collidePosZ)).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == caster) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (aabb.contains(from)) {
                result.addEntityHit(entity);
            } else if (hit.isPresent()) {
                result.addEntityHit(entity);
            }
        }
        return result;
    }

    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024;
    }

    public static class ChainsawHitResult {
        private BlockHitResult blockHit;

        private final List<LivingEntity> entities = new ArrayList<>();

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

    @Override
    public void tick() {
        super.tick();
        prevCollidePosX = collidePosX;
        prevCollidePosY = collidePosY;
        prevCollidePosZ = collidePosZ;
        prevYaw = renderYaw;
        prevPitch = renderPitch;
        xo = getX();
        yo = getY();
        zo = getZ();
        if (tickCount == 1 && level.isClientSide) {
            caster = (LivingEntity) level.getEntity(getCasterID());
        }
        if (!level.isClientSide) {
            this.updateWithDefender();
        }
        if (caster instanceof DefenderEntity) {
            renderYaw = (float) ((caster.yHeadRot + 90.0d) * Math.PI / 180.0d);
            renderPitch = (float) ((-((DefenderEntity) caster).getChainsawLookX()) * Math.PI / 180.0d);
        }

        if (!on) {
            this.discard();
        }

        if (caster != null && !caster.isAlive()) discard() ;

        if (level.isClientSide && tickCount <= 10 && caster != null) {
            int particleCount = 8;
            while (--particleCount != 0) {
                double radius = 2f * caster.getBbWidth();
                double yaw = random.nextFloat() * 2 * Math.PI;
                double pitch = random.nextFloat() * 2 * Math.PI;
                double ox = radius * Math.sin(yaw) * Math.sin(pitch);
                double oy = radius * Math.cos(pitch);
                double oz = radius * Math.cos(yaw) * Math.sin(pitch);
                double rootX = caster.getX();
                double rootY = caster.getY() + caster.getBbHeight() / 2f + 0.3f;
                double rootZ = caster.getZ();
                attractorPos[0] = new Vec3(rootX, rootY, rootZ);
            }
        }
        this.calculateEndPos();
        List<LivingEntity> hit = raytraceEntities(level, new Vec3(getX(), getY(), getZ()), new Vec3(endPosX, endPosY, endPosZ), false, true, true).entities;
        if (!level.isClientSide && this.caster != null) {
            float healing = 0.0f;
            boolean canHeal = false;

            for (LivingEntity target : hit) {
                if (target.hurt(DamageSource.indirectMagic(this, this.caster), 3.0F)) {
                    healing += 0.5f;
                    canHeal = true;
                }
                if (this.tickCount % 2 == 0) target.invulnerableTime -= 2;
                target.hurtMarked = true;
                target.setDeltaMovement(0.0D, 0.0D, 0.0D);
            }

            if (canHeal) this.caster.heal(Math.min(healing, 15.0f));
        }
        if (tickCount > getDuration()) {
            on = false;
        }
    }
}
