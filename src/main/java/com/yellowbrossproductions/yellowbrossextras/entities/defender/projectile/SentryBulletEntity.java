package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.IsDefenderAligned;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.CustomAbstractHurtingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.init.YEItemsAndBlocks;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.List;

public class SentryBulletEntity extends CustomAbstractHurtingProjectile implements ItemSupplier {

    public SentryBulletEntity(EntityType<? extends CustomAbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public SentryBulletEntity(EntityType<? extends CustomAbstractHurtingProjectile> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        super(YEEntityTypes.SentryBullet.get(), p_36818_, p_36819_, p_36820_, p_36821_, p_36822_, p_36823_, p_36824_);
    }

    public SentryBulletEntity(Level p_36831_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_) {
        super(YEEntityTypes.SentryBullet.get(), p_36827_, p_36828_, p_36829_, p_36830_, p_36831_);
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        boolean team = true;
        if (shouldCareAboutTeams) {
            team = EntityUtil.canHurtThisMob(entity, (Mob) this.getOwner()) && !(entity instanceof IsDefenderAligned);
        }
        return team && entity != this.getOwner() && !(entity instanceof Projectile) && super.canHitEntity(entity);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.explode(null, 2.0D);
            this.discard();
        }
    }

    @Override
    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.ELECTRIC_SPARK;
    }

    private void explode(@Nullable Entity hitEntity, double size) {
        if (this.level.isClientSide) return;

        List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, new AABB(this.position().subtract(size, size, size), this.position().add(size, size, size)), p -> p.isAlive() && p != this.getOwner() && p.hasLineOfSight(this));
        if (hitEntity instanceof LivingEntity living) list.add(living);

        this.makeExplodeParticles();
        this.stopShootingSound(this.level);
        this.playSound(YESoundEvents.ENTITY_DEFENDER_SENTRY_HIT.get(), 2.0F, 1.0F);

        for (Entity entity : list) {
            boolean canHurt = (!(this.getOwner() instanceof Mob owner) || EntityUtil.canHurtThisMob(entity, owner)) && entity != this.getOwner() && !(entity instanceof IsDefenderAligned);
            if (canHurt) {
                DamageSource damageSource = new IndirectEntityDamageSource("thrown", this, this.getOwner()).setProjectile();
                entity.invulnerableTime -= 9;
                entity.hurt(damageSource, 5.0F);
            }
        }
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > 80) {
            if (!this.level.isClientSide) {
                this.level.broadcastEntityEvent(this, (byte)3);
                this.explode(null, 2.0D);
                this.discard();
            }
        }
    }

    public void makeExplodeParticles() {
        for(int i = 0; i < 3; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 1; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.EXPLOSION, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
    }

    @Override
    public ItemStack getItem() {
        return YEItemsAndBlocks.CONVERSLIN_BULLET.get().getDefaultInstance();
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public void stopShootingSound(Level world) {
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        ClientboundStopSoundPacket sstopsoundpacket = new ClientboundStopSoundPacket(YESoundEvents.ENTITY_DEFENDER_SENTRY_HIT.get().getLocation(), SoundSource.NEUTRAL);
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

    @Override
    public boolean isPickable() {
        return false;
    }
}
