package com.yellowbrossproductions.yellowbrossextras.entities.defender;

import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.WitherExplosionFlashPacket;
import com.yellowbrossproductions.yellowbrossextras.packet.WitherExplosionOverlayPacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.LoopingSound;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;

public class WitherExplosion extends Entity {
    public Mob owner;

    public WitherExplosion(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.noPhysics = true;
    }

    public WitherExplosion(Level level, Vec3 pos, @Nullable Mob owner) {
        super(YEEntityTypes.WitherExplosion.get(), level);
        this.owner = owner;
        this.setPos(pos);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= 60) this.discard();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();

        CameraShake.cameraShake(this.level, this.position(), 200, 0.04f, 40, 20);

        if (!this.level.isClientSide && this.level instanceof ServerLevel serverLevel) {
            double radius = 150.0;

            for (ServerPlayer player : serverLevel.players()) {
                if (this.distanceToSqr(player) <= radius * radius) {
                    PacketHandler.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> player),
                            new WitherExplosionOverlayPacket()
                    );
                    if (player.hasLineOfSight(this)) {
                        PacketHandler.CHANNEL.send(
                                PacketDistributor.PLAYER.with(() -> player),
                                new WitherExplosionFlashPacket()
                        );
                    }
                }
            }
        }

        for (LivingEntity entity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(50.0d))) {
            boolean allowedToDamage = true;
            if (this.owner != null) allowedToDamage = EntityUtil.canHurtThisMob(entity, this.owner) && entity != this.owner;
            if (allowedToDamage && entity.hasLineOfSight(this)) {
                if (entity.hurt(DamageSource.explosion(this.owner), 50 * EntityUtil.multiplyToScrewArmor(entity, 0.3f))) {
                    double x = this.getX() - entity.getX();
                    double y = this.getY() - entity.getY();
                    double z = this.getZ() - entity.getZ();
                    double d = Math.sqrt(x * x + y * y + z * z);
                    entity.hurtMarked = true;
                    entity.setDeltaMovement(entity.getDeltaMovement().add(-x / d * 4.5D, (-y / d * 1.2D) + 2.0D, -z / d * 4.5D));
                    entity.addEffect(new MobEffectInstance(MobEffects.WITHER, 300, 2, true, true));
                }
            }
        }
    }

    @Override
    public boolean isPickable() {
        return false;
    }
}
