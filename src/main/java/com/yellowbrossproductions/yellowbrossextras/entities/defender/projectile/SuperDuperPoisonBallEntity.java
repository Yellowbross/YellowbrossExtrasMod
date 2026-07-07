package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.IsDefenderAligned;
import com.yellowbrossproductions.yellowbrossextras.entities.projectile.CustomAbstractHurtingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.*;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class SuperDuperPoisonBallEntity extends CustomAbstractHurtingProjectile implements ItemSupplier {

    public SuperDuperPoisonBallEntity(EntityType<? extends CustomAbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    public SuperDuperPoisonBallEntity(EntityType<? extends CustomAbstractHurtingProjectile> p_36817_, double p_36818_, double p_36819_, double p_36820_, double p_36821_, double p_36822_, double p_36823_, Level p_36824_) {
        super(YEEntityTypes.SuperDuperPoisonBall.get(), p_36818_, p_36819_, p_36820_, p_36821_, p_36822_, p_36823_, p_36824_);
    }

    public SuperDuperPoisonBallEntity(Level p_36831_, LivingEntity p_36827_, double p_36828_, double p_36829_, double p_36830_) {
        super(YEEntityTypes.SuperDuperPoisonBall.get(), p_36827_, p_36828_, p_36829_, p_36830_, p_36831_);
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
        return YEParticleTypes.SUPERDUPERPOISON_DRIP.get();
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level, size, this, Entity::isAlive);

        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        this.playSound(YESoundEvents.SUPERDUPERPOISON_NOSCREAM.get(), 1.0F, 1.0F);
        EntityUtil.makeAParticle(this.level, YEParticleTypes.SUPERDUPERPOISON_EXPLOSION.get(), false, this.position(), Vec3.ZERO);
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = true;
                if (shouldCareAboutTeams) {
                    team = EntityUtil.canHurtThisMob(living, (Mob) this.getOwner()) && entity != this.getOwner() && !(living instanceof IsDefenderAligned);
                }
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.addEffect(new MobEffectInstance(YEEffects.SUPER_DUPER_POISON.get(), 25 * 20, 0));
                }
            }
        }
        if (!this.level.isClientSide) {
            this.discard();
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

    @Override
    public ItemStack getItem() {
        return YEItemsAndBlocks.SUPERDUPERPOISON_BALL.get().getDefaultInstance();
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
