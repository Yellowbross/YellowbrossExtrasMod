package com.yellowbrossproductions.yellowbrossextras.entities.creepers;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.init.ModEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class SneakerEntity extends AbstractCreeperEntity implements CreeperEnemy {
    private static final EntityDataAccessor<Integer> CREEPER_TYPE = SynchedEntityData.defineId(SneakerEntity.class, EntityDataSerializers.INT);
    private int explosionPull;

    public SneakerEntity(EntityType<? extends YExtrasMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CreeperExplodeGoal(this));
        this.goalSelector.addGoal(3, new MergeWithMyLoveGoal(this, SneakerEntity.class));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, CreeperEnemy.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Mob && !(p_29932_ instanceof CreeperEnemy);
        }));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CREEPER_TYPE, 0);
    }

    public void addAdditionalSaveData(CompoundTag p_32304_) {
        super.addAdditionalSaveData(p_32304_);

        p_32304_.putInt("CreeperType", this.getCreeperType());
    }

    public void readAdditionalSaveData(CompoundTag p_32296_) {
        super.readAdditionalSaveData(p_32296_);

        this.setCreeperType(p_32296_.getInt("CreeperType"));
    }

    @Override
    protected int getMaxAbsorbs() {
        return 4;
    }

    @Override
    public void tick() {
        if (this.getSwellDir() > 0 && this.getCreeperType() == 2) {
            this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
        }
        super.tick();

        if (this.getCreeperType() == 2) {
            this.heal(2.0F);
            this.makeLifestealParticles3();

            if (this.swell >= this.maxSwell) {
                this.explosionPull += 1;
                if (this.explosionPull >= 70) {
                    this.explodeCreeper();
                }
            }

            for (Entity caught : level.getEntities(this, getBoundingBox().inflate(15.0F))) {
                if (EntityUtil.canHurtThisMob(caught, this) && caught.isAlive() && EntityUtil.isMobNotInCreativeMode(caught) && !(caught instanceof CreeperEnemy)) {
                    double x = getX() - caught.getX();
                    double y = getY() - caught.getY();
                    double z = getZ() - caught.getZ();
                    double d = Math.sqrt(x * x + y * y + z * z);
                    float power = (float) 1.0F + (this.explosionPull * 0.1F);
                    double motionX = caught.getDeltaMovement().x + (x / d * (double) power * 0.2D);
                    double motionY = caught.getDeltaMovement().y + (y / d * (double) power * 0.2D);
                    double motionZ = caught.getDeltaMovement().z + (z / d * (double) power * 0.2D);
                    caught.hurtMarked = true;
                    caught.setDeltaMovement(motionX, motionY, motionZ);
                    caught.lerpMotion(motionX, motionY, motionZ);
                    this.makeLifestealParticles2(caught);
                }
            }
        }
    }

    @Override
    public void calculateSwell() {
        if (this.getCreeperType() == 2) {
            if (this.shouldCalculateSwell) {
                this.oldSwell = this.swell;
                if (this.isIgnited()) {
                    this.setSwellDir(1);
                }

                int i = this.getSwellDir();
                if (i > 0 && this.swell == 0) {
                    this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                    this.gameEvent(GameEvent.PRIME_FUSE);
                }

                this.swell += i;
                if (this.swell < 0) {
                    this.swell = 0;
                }

                if (this.swell >= this.maxSwell) {
                    this.swell = this.maxSwell;
                }
            }
        } else {
            super.calculateSwell();
        }
    }

    public void makeLifestealParticles2(Entity caught) {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 2; ++i) {
                        packet.queueParticle(ParticleTypes.CRIT, false, new Vec3(caught.getRandomX(0.5D), caught.getRandomY(), caught.getRandomZ(0.5D)), new Vec3(0.0D, 0.0D, 0.0D));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public void makeLifestealParticles3() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 6; ++i) {
                        double randomX = this.getX() - 5 + this.random.nextInt(10);
                        double randomY = this.getY() - 5 + this.random.nextInt(10);
                        double randomZ = this.getZ() - 5 + this.random.nextInt(10);

                        double d0 = (this.getX() - randomX) / 4;
                        double d1 = (this.getY() - randomY) / 4;
                        double d2 = (this.getZ() - randomZ) / 4;
                        packet.queueParticle(ParticleTypes.ELECTRIC_SPARK, false, new Vec3(randomX, randomY, randomZ), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    @Override
    public void explodeCreeper() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            this.dead = true;
            CameraShakeEntity.cameraShake(this.level, position(), 30, 0.1f, 0, 15);
            if (this.getAbsorbedCreepers() < this.getMaxAbsorbs()) {
                if (this.getCreeperType() == 0) {
                    if (this.random.nextBoolean()) {
                        ParacreeperEntity creeper = ModEntityTypes.Paracreeper.get().create(this.level);
                        assert creeper != null;
                        creeper.copyPosition(this);
                        creeper.setPos(creeper.getX(), creeper.getY() + 1, creeper.getZ());
                        if (this.getTeam() != null) {
                            level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                                    level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                        }
                        this.level.addFreshEntity(creeper);
                    }
                    this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)(this.explosionRadius * f), explosion$blockinteraction);
                } else if (this.getCreeperType() == 1) {
                    for (int i = 0; i < 5; ++i) {
                        ParacreeperEntity creeper = ModEntityTypes.Paracreeper.get().create(this.level);
                        assert creeper != null;
                        creeper.copyPosition(this);
                        creeper.setPos(creeper.getX(), creeper.getY() + 1, creeper.getZ());
                        creeper.setDeltaMovement(this.random.nextDouble() - 0.5D,
                                this.random.nextDouble() - 0.5D,
                                this.random.nextDouble() - 0.5D);
                        if (this.getTeam() != null) {
                            level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                                    level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                        }
                        this.level.addFreshEntity(creeper);
                    }
                    this.makeExplodeParticles();
                    this.playSound(YellowbrossExtrasSoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.2F);
                    CameraShakeEntity.cameraShake(this.level, position(), 40, 0.2f, 0, 30);
                    this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)(this.explosionRadius * 2.5F * f), explosion$blockinteraction);
                } else if (this.getCreeperType() == 2) {
                    SneakerEntity creeper = ModEntityTypes.Sneaker.get().create(this.level);
                    assert creeper != null;
                    creeper.copyPosition(this);
                    if (this.getTeam() != null) {
                        level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                                level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                    }
                    creeper.setCreeperType(1);
                    this.level.addFreshEntity(creeper);

                    this.makeExplodeParticles();
                    this.playSound(YellowbrossExtrasSoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.2F);
                    CameraShakeEntity.cameraShake(this.level, position(), 40, 0.2f, 0, 30);
                    this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)(this.explosionRadius * 2.5F * f), explosion$blockinteraction);
                }
            } else {
                CrawlerEntity creeper = ModEntityTypes.Crawler.get().create(this.level);
                assert creeper != null;
                creeper.copyPosition(this);
                if (this.getTeam() != null) {
                    level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                            level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                }
                this.level.addFreshEntity(creeper);

                this.makeExplodeParticles();
                this.playSound(YellowbrossExtrasSoundEvents.HUGE_EXPLOSION.get(), 4.0F, 1.2F);
                CameraShakeEntity.cameraShake(this.level, position(), 40, 0.2f, 0, 30);
                this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)(this.explosionRadius * 2.5F * f), explosion$blockinteraction);
            }
            this.discard();
            this.spawnLingeringCloud();
        }

    }

    public void makeExplodeParticles() {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 250; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d1 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d2 = (-0.5 + this.random.nextGaussian()) / 2;
                        packet.queueParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 200; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d1 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d2 = (-0.5 + this.random.nextGaussian()) / 2;
                        packet.queueParticle(ParticleTypes.POOF, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }
                    for(int i = 0; i < 150; ++i) {
                        double d0 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d1 = (-0.5 + this.random.nextGaussian()) / 2;
                        double d2 = (-0.5 + this.random.nextGaussian()) / 2;
                        packet.queueParticle(ParticleTypes.LARGE_SMOKE, false, new Vec3(this.getRandomX(1.0D), this.getRandomY(), this.getRandomZ(1.0D)), new Vec3(d0, d1, d2));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    public int getCreeperType() {
        return this.entityData.get(CREEPER_TYPE);
    }

    public void setCreeperType(int type) {
        this.entityData.set(CREEPER_TYPE, type);
    }

    @Override
    public void die(DamageSource p_21014_) {
        this.ignite();
    }

    @Override
    protected void tickDeath() {
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
    }
}
