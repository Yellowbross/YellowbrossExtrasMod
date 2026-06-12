package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.IsDefenderAligned;
import com.yellowbrossproductions.yellowbrossextras.init.YEEffects;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
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

import java.util.List;

public class DefenderArrowEntity extends AbstractArrow {
    private static final EntityDataAccessor<Integer> ARROW_TYPE = SynchedEntityData.defineId(DefenderArrowEntity.class, EntityDataSerializers.INT);

    public DefenderArrowEntity(EntityType<? extends AbstractArrow> arrow, Level level) {
        super(arrow, level);
    }

    public DefenderArrowEntity(Level level, double x, double y, double z) {
        super(YEEntityTypes.DefenderArrow.get(), x, y, z, level);
        this.setPos(x, y, z);
    }

    public DefenderArrowEntity(Level level, LivingEntity entity) {
        super(YEEntityTypes.DefenderArrow.get(), entity, level);
        setOwner(entity);
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
    protected boolean canHitEntity(Entity entity) {
        boolean shouldCareAboutTeams = this.getOwner() instanceof Mob;
        boolean team = true;
        if (shouldCareAboutTeams) {
            team = EntityUtil.canHurtThisMob(entity, (Mob) this.getOwner()) && !(entity instanceof IsDefenderAligned);
        }
        return team && entity != this.getOwner() && !(entity instanceof Projectile) && super.canHitEntity(entity);
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
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("ArrowType", this.getArrowType());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setArrowType(pCompound.getInt("ArrowType"));
    }

    @Override
    protected void onHitEntity(EntityHitResult hitRes) {
        switch (this.getArrowType()) {
            case 0 : {
                this.setPierceLevel((byte) (this.getPierceLevel() + 2));
                this.explode(5.0F);
                if (hitRes.getEntity().invulnerableTime < 1) this.playSound(SoundEvents.GENERIC_EXPLODE, 1.5F, 0.6F);
                break;
            }
            case 1 : {
                if (hitRes.getEntity() instanceof LivingEntity living && !living.isBlocking()) {
                    MobEffectInstance effect = living.getEffect(YEEffects.SUPER_DUPER_POISON.get());
                    int i = 1;
                    if (effect != null) {
                        i += effect.getAmplifier();
                        living.removeEffectNoUpdate(YEEffects.SUPER_DUPER_POISON.get());
                    } else {
                        --i;
                    }

                    i = Mth.clamp(i, 0, 4);
                    living.addEffect(new MobEffectInstance(YEEffects.SUPER_DUPER_POISON.get(), 25 * 20, i));
                }
                break;
            }
        }
        super.onHitEntity(hitRes);
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
