package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.IsDefenderAligned;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.IsOryctolinAligned;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.ItemRegisterer;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class SentryBulletEntity extends AbstractHurtingProjectile implements ItemSupplier {

    public SentryBulletEntity(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public SentryBulletEntity(EntityType<? extends AbstractHurtingProjectile> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        super(ModEntityTypes.SentryBullet.get(), p_36818_, p_36819_, p_36820_, p_36821_, p_36822_, p_36823_, p_36824_);
    }

    public SentryBulletEntity(Level p_36831_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_) {
        super(ModEntityTypes.SentryBullet.get(), p_36827_, p_36828_, p_36829_, p_36830_, p_36831_);
    }

    @Override
    protected boolean canHitEntity(Entity p_36842_) {
        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        boolean team = true;
        if (shouldCareAboutTeams) {
            team = EntityUtil.canHurtThisMob(p_36842_, (Mob) this.getOwner()) && !(p_36842_ instanceof IsDefenderAligned);
        }
        return team && p_36842_ != this.getOwner() && !(p_36842_ instanceof Projectile) && super.canHitEntity(p_36842_);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.explode(2.0D);
            this.discard();
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.ELECTRIC_SPARK;
    }

    private void explode(double size) {
        List<Entity> list = this.level.getEntities(this, new AABB(this.getX() - size, this.getY() - size, this.getZ() - size, this.getX() + size, this.getY() + size, this.getZ() + size), Entity::isAlive);

        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        this.makeExplodeParticles();
        this.stopShootingSound(this.level);
        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SENTRY_HIT.get(), 2.0F, 1.0F);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = true;
                if (shouldCareAboutTeams) {
                    team = EntityUtil.canHurtThisMob(living, (Mob) this.getOwner()) && entity != this.getOwner() && !(living instanceof IsDefenderAligned);
                }
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.hurt(DamageSource.thrown(this, this.getOwner()), 1.0F);
                    living.invulnerableTime -= 9;
                    if (!this.level.isClientSide) {
                        this.discard();
                    }
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > 80) {
            if (!this.level.isClientSide) {
                this.level.broadcastEntityEvent(this, (byte)3);
                this.explode(2.0D);
                this.discard();
            }
        }
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
                        packet.queueParticle(ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 1; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.EXPLOSION, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return ItemRegisterer.CONVERSLIN_BULLET.get().getDefaultInstance();
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public void stopShootingSound(Level world) {
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        ClientboundStopSoundPacket sstopsoundpacket = new ClientboundStopSoundPacket(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_SENTRY_HIT.get().getLocation(), SoundSource.NEUTRAL);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(sstopsoundpacket);
        }
    }

    @Override
    public boolean hurt(DamageSource p_36839_, float p_36840_) {
        if (p_36839_ == DamageSource.OUT_OF_WORLD) {
            return super.hurt(p_36839_, p_36840_);
        }
        return false;
    }
}
