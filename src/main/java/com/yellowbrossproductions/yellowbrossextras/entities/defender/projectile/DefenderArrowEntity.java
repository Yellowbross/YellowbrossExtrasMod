package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.DefenderEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.IsDefenderAligned;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;

import java.util.List;

public class DefenderArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ARROW_TYPE = SynchedEntityData.defineId(DefenderArrowEntity.class, EntityDataSerializers.INT);

    public DefenderArrowEntity(EntityType<? extends AbstractArrow> p_36858_, Level p_36859_) {
        super(p_36858_, p_36859_);
    }

    public DefenderArrowEntity(Level p_36861_, double x, double y, double z) {
        super(ModEntityTypes.DefenderArrow.get(), x, y, z, p_36861_);
        this.setPos(x, y, z);
    }

    public DefenderArrowEntity(Level p_36866_, LivingEntity p_36867_) {
        super(ModEntityTypes.DefenderArrow.get(), p_36867_, p_36866_);
        setOwner(p_36867_);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ARROW_TYPE, 0);
    }

    public int getArrowType() {
        return this.entityData.get(ARROW_TYPE);
    }

    public void setArrowType(int input) {
        this.entityData.set(ARROW_TYPE, input);
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
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHitEntity(EntityHitResult hitRes) {
        this.setPierceLevel((byte) (this.getPierceLevel() + 2));
        super.onHitEntity(hitRes);
        switch (this.getArrowType()) {
            case 1 : {

            }
            default : {
                this.explode(5.0F);
                if (hitRes.getEntity().invulnerableTime < 1) this.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 0.6F);
                break;
            }
        }
    }

    private void explode(double size) {
        List<Entity> list = EntityUtil.getEntitiesFromAABB(this.level, size, this, Entity::isAlive);

        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        this.makeExplodeParticles();
        for (Entity entity : list) {
            if (entity instanceof LivingEntity living) {
                boolean team = true;
                if (shouldCareAboutTeams) {
                    team = EntityUtil.canHurtThisMob(living, (Mob) this.getOwner()) && entity != this.getOwner() && !(living instanceof IsDefenderAligned);
                }
                if (team && entity.isAlive() && !entity.isInvulnerable() && !entity.isSpectator()) {
                    living.hurt(DamageSource.arrow(this, this.getOwner()), (float) this.getBaseDamage() * EntityUtil.multiplyToScrewArmor(living, 1.0f));
                }
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
        for(int i = 0; i < 3; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
        for(int i = 0; i < 1; ++i) {
            double d0 = (-0.5 + this.random.nextGaussian());
            double d1 = (-0.5 + this.random.nextGaussian());
            double d2 = (-0.5 + this.random.nextGaussian());
            EntityUtil.makeAParticle(this.level, ParticleTypes.EXPLOSION_EMITTER, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
        }
    }
}
