package com.yellowbrossproductions.yellowbrossextras.entities.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.IsOryctolinAligned;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.RegistryHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
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

import java.util.List;

public class ConverslinBulletEntity extends CustomAbstractHurtingProjectile implements ItemSupplier {

    public ConverslinBulletEntity(EntityType<? extends CustomAbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public ConverslinBulletEntity(EntityType<? extends CustomAbstractHurtingProjectile> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        super(ModEntityTypes.ConverslinBullet.get(), p_36818_, p_36819_, p_36820_, p_36821_, p_36822_, p_36823_, p_36824_);
    }

    public ConverslinBulletEntity(Level p_36831_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_) {
        super(ModEntityTypes.ConverslinBullet.get(), p_36827_, p_36828_, p_36829_, p_36830_, p_36831_);
    }

    protected void onHitEntity(EntityHitResult p_37404_) {
        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        boolean team = true;
        if (shouldCareAboutTeams) {
            team = EntityUtil.canHurtThisMob(p_37404_.getEntity(), (Mob) this.getOwner()) && !(p_37404_.getEntity() instanceof AbstractOryctolin);
        }
        if (team && p_37404_.getEntity() != this.getOwner() && !(p_37404_.getEntity() instanceof Projectile)) {
            super.onHitEntity(p_37404_);
            if (!this.level.isClientSide) {
                this.explode(3.0D);
            }
        }
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            this.explode(3.0D);
            this.discard();
        }
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level, size, this, Entity::isAlive);

        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        this.makeExplodeParticles();
        this.playSound(SoundEvents.GENERIC_EXPLODE, 2.0F, 0.8F);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = true;
                if (shouldCareAboutTeams) {
                    team = EntityUtil.canHurtThisMob(living, (Mob) this.getOwner()) && entity != this.getOwner() && !(living instanceof IsOryctolinAligned);
                }
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.hurt(DamageSource.indirectMagic(this, this.getOwner()), 15.0F);
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
                this.explode(3.0D);
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
                    for(int i = 0; i < 3; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 1; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian());
                        double d1 = (-0.5 + this.random.nextGaussian());
                        double d2 = (-0.5 + this.random.nextGaussian());
                        packet.queueParticle(ParticleTypes.EXPLOSION_EMITTER, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    public ItemStack getItem() {
        return RegistryHandler.CONVERSLIN_BULLET.get().getDefaultInstance();
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    @Override
    public boolean hurt(DamageSource p_36839_, float p_36840_) {
        if (p_36839_ == DamageSource.OUT_OF_WORLD) {
            return super.hurt(p_36839_, p_36840_);
        }
        return false;
    }
}
