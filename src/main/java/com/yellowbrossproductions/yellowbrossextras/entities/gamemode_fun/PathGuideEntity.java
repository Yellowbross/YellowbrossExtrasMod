package com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun;

import com.google.common.collect.Lists;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.gamemode_fun.CapTheIntelGoal;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class PathGuideEntity extends Entity {
    private static final EntityDataAccessor<Boolean> WAITING_FOR_SIGNAL = SynchedEntityData.defineId(PathGuideEntity.class, EntityDataSerializers.BOOLEAN);
    private List<BlockPos> savedLocations = Lists.newArrayList();

    public PathGuideEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(WAITING_FOR_SIGNAL, false);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {
        if (p_20052_.contains("SavedLocations")) {
            ListTag listTag = p_20052_.getList("SavedLocations", 10);

            for(int i = 0; i < listTag.size(); ++i) {
                BlockPos pos = NbtUtils.readBlockPos(listTag.getCompound(i));
                this.savedLocations.add(pos);
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {
        ListTag listtag = new ListTag();

        for (BlockPos savedLocation : this.savedLocations) {
            listtag.add(NbtUtils.writeBlockPos(savedLocation));
        }

        p_20139_.put("SavedLocations", listtag);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount % 20 == 0) {
            List<PathfinderMob> playing = this.level.getEntitiesOfClass(PathfinderMob.class, this.getBoundingBox().inflate(150.0D), p -> {
                return !p.isRemoved() && p.isAlive() && p.getTeam() != null && p.getTeam() != this.getTeam() && p.getTags().contains("playing");
            });

            if (!this.level.isClientSide) {
                // this.setInvisible(!playing.isEmpty());
            }

            if (!this.isInvisible()) {
                if (!this.savedLocations.isEmpty()) {
                    for (BlockPos neighbor : this.savedLocations) {
                        this.makeHelpingTrail(this.blockPosition().above(), neighbor.above());
                    }
                }
            }
        }
    }

    public void makeHelpingTrail(BlockPos origin, BlockPos destination) {
        double srcX = origin.getX() + 0.5;
        double srcY = origin.getY();
        double srcZ = origin.getZ() + 0.5;
        double destX = destination.getX() + 0.5;
        double destY = destination.getY();
        double destZ = destination.getZ() + 0.5;

        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    int particles = 30;
                    for (int i = 0; i < particles; i++) {
                        double trailFactor = i / (particles - 1.0D);
                        float f = (this.random.nextFloat() - 0.5F) * 0.2F;
                        float f1 = (this.random.nextFloat() - 0.5F) * 0.2F;
                        float f2 = (this.random.nextFloat() - 0.5F) * 0.2F;
                        double tx = srcX + (destX - srcX) * trailFactor;
                        double ty = srcY + (destY - srcY) * trailFactor;
                        double tz = srcZ + (destZ - srcZ) * trailFactor;
                        packet.queueParticle(ParticleTypes.HAPPY_VILLAGER, false, new Vec3(tx, ty, tz), new Vec3(f, f1, f2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    public InteractionResult interact(Player p_19978_, InteractionHand p_19979_) {
        if (!this.isWaitingForSignal()) {
            this.setWaitingForSignal(true);
        }
        List<PathGuideEntity> neighbors = this.level.getEntitiesOfClass(PathGuideEntity.class, this.getBoundingBox().inflate(150.0D), p -> {
            return p.isWaitingForSignal() && p != this;
        });
        if (!neighbors.isEmpty()) {
            PathGuideEntity neighbor = neighbors.get(0);
            BlockPos pos = neighbor.blockPosition();
            BlockPos pos2 = this.blockPosition();
            if (!this.savedLocations.contains(pos)) {
                this.savedLocations.add(pos);
                neighbor.getNeighbors().add(pos2);
            } else {
                this.savedLocations.remove(pos);
                neighbor.getNeighbors().remove(pos2);
            }
            this.setWaitingForSignal(false);
            neighbors.get(0).setWaitingForSignal(false);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    public boolean isWaitingForSignal() {
        return this.entityData.get(WAITING_FOR_SIGNAL);
    }

    public void setWaitingForSignal(boolean i) {
        if (!this.level.isClientSide) {
            this.entityData.set(WAITING_FOR_SIGNAL, i);
            this.setGlowingTag(i);
        }
    }

    public boolean isPickable() {
        return true;
    }

    public List<BlockPos> getNeighbors() {
        return this.savedLocations;
    }
}
