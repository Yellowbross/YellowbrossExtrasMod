package com.yellowbrossproductions.yellowbrossextras.entities.defender;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShakeEntity;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.entities.creepers.AbstractCreeperEntity;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.util.YellowbrossExtrasSoundEvents;
import com.yellowbrossproductions.yellowbrossextras.world.CustomExplosion;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

// A lot of the code here was adapted and mashed together from Mutant Monsters and Mowzie's Mobs
public class CreeperBulletEntity extends AbstractCreeperEntity implements IsDefenderAligned {
    private static final EntityDataAccessor<BlockPos> COLLISION_POS = SynchedEntityData.defineId(CreeperBulletEntity.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Float> SHOOT_Y = SynchedEntityData.defineId(CreeperBulletEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> SHOOT_X = SynchedEntityData.defineId(CreeperBulletEntity.class, EntityDataSerializers.FLOAT);
    LivingEntity shooter = null;
    public boolean wasShotFromDefender;
    int getUpTicks;

    public AnimationState anim_fly = new AnimationState();
    public AnimationState anim_getup = new AnimationState();
    public AnimationState anim_falling = new AnimationState();

    public CreeperBulletEntity(EntityType<? extends YExtrasMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new DoNothingUntilGotUpGoal());
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new DontExplodeUntilGotUpGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, IsDefenderAligned.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Enemy;
        }));
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                .add(Attributes.MAX_HEALTH, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COLLISION_POS, BlockPos.ZERO);
        this.entityData.define(SHOOT_Y, 0.0F);
        this.entityData.define(SHOOT_X, 0.0F);
    }

    @Override
    public boolean canAttack(LivingEntity entity) {
        return !this.wasShotFromDefender && super.canAttack(entity);
    }

    public BlockPos getCollisionPos() {
        return this.entityData.get(COLLISION_POS);
    }

    public void setCollisionPos(BlockPos pos) {
        this.entityData.set(COLLISION_POS, pos);
    }

    public void setCollisionPos(int x, int y, int z) {
        this.entityData.set(COLLISION_POS, new BlockPos(x, y, z));
    }

    public void randomize(float scale) {
        this.setCollisionPos(
                (int)(this.getCollisionPos().getX() + (this.random.nextFloat() - 0.5F) * scale),
                (int)(this.getCollisionPos().getY() + (this.random.nextFloat() - 0.5F) * scale),
                (int)(this.getCollisionPos().getZ() + (this.random.nextFloat() - 0.5F) * scale)
        );
    }

    @Override
    public void updateAnimations() {
        EntityUtil.animateWhen(this.anim_fly, this.getAnimationState().equals("fly"), this.tickCount);
        EntityUtil.animateWhen(this.anim_getup, this.getAnimationState().equals("getup"), this.tickCount);
        EntityUtil.animateWhen(this.anim_falling, this.getAnimationState().equals("falling"), this.tickCount);
    }

    @Override
    public void explodeCreeper() {
        if (!this.level.isClientSide) {
            this.dead = true;
            CustomExplosion.create(this, this.getX(), this.getY() + 0.3, this.getZ(), (float)this.explosionRadius * (this.isPowered() ? 2 : 1), true);
            CameraShakeEntity.cameraShake(this.level, position(), 30, 0.1f, 0, 15);
            this.discard();
            this.spawnLingeringCloud();
        }
    }

    @Override
    public boolean causeFallDamage(float p_149687_, float p_149688_, DamageSource p_149689_) {
        if (this.wasShotFromDefender) {
            this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CREEPERBULLET_LAND.get());
            this.setAnimationState("getup");
        }
        return super.causeFallDamage(p_149687_, p_149688_, p_149689_);
    }

    public void setShooter(LivingEntity shooter) {
        this.shooter = shooter;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof CreeperBulletEntity creeper) {
            if (creeper.isPowered()) {
                this.setPowered();
                this.ignite();
                this.maxSwell = this.random.nextInt(15) + 5;
            }
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isAlive()) {
            if (this.wasShotFromDefender) {
                if (this.tickCount == 1) {
                    if (!this.level.isClientSide) this.spawnHittingSomething(this.level, this.getPosition(0.0F), new Vec3(this.getCollisionPos().getX(), this.getCollisionPos().getY(), this.getCollisionPos().getZ()));
                }
                if (this.tickCount == 5 && !this.getAnimationState().equals("getup")) {
                    this.setAnimationState("falling");
                }
            }
            if (this.tickCount > 200 && this.random.nextInt(16) == 0 && !this.level.isClientSide) this.ignite();
        }
    }

    public void beTheChosenOne() {
        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.level);
        assert lightning != null;
        lightning.setPos(this.getX(), this.getY(), this.getZ());
        lightning.setVisualOnly(true);
        this.playSound(SoundEvents.LIGHTNING_BOLT_IMPACT, 3.0F, 1.0F);
        this.playSound(SoundEvents.LIGHTNING_BOLT_THUNDER, 10000.0F, 1.0F);
        this.level.addFreshEntity(lightning);
        this.setPowered();
        this.ignite();
    }

    public float getShootY() {
        return this.entityData.get(SHOOT_Y);
    }

    public void setShootY(float input) {
        if (!Float.isFinite(input)) Util.logAndPauseIfInIde("Invalid entity rotation: " + input + ", discarding.");
        else this.entityData.set(SHOOT_Y, input);
    }

    public float getShootX() {
        return this.entityData.get(SHOOT_X);
    }

    public void setShootX(float input) {
        if (!Float.isFinite(input)) Util.logAndPauseIfInIde("Invalid entity rotation: " + input + ", discarding.");
        else this.entityData.set(SHOOT_X, input);
    }

    @Override
    public void die(DamageSource p_21014_) {
        super.die(p_21014_);
        this.wasShotFromDefender = false;
        this.setAnimationState("none");
    }

    public static class CreeperBulletHitResult {
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

    public void spawnHittingSomething(Level world, Vec3 from, Vec3 to) {
        this.wasShotFromDefender = true;

        double x = (to.x) - this.getX();
        double y = (to.y) - this.getY();
        double z = (to.z) - this.getZ();
        double d = Math.sqrt(x * x + z * z);
        // this.setShootY(180.0F + (float)Math.toDegrees(Math.atan2(x, z)));
        // if (this.getShootY() > 360.0F) this.setShootY(this.getShootY() - 360.0F);

        // this.setShootX((float)Math.toDegrees((Math.atan2(y, d))));

        CreeperBulletHitResult hitResult = new CreeperBulletHitResult();
        hitResult.setBlockHit(world.clip(new ClipContext(from, to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this)));
        if (hitResult.blockHit != null) {
            Vec3 hitVec = hitResult.blockHit.getLocation();
            this.setCollisionPos(hitResult.getBlockHit().getBlockPos());
        } else {
            this.setCollisionPos((int)to.x, (int)to.y, (int)to.z);
        }
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, new AABB(
                Math.min(getX(), this.getCollisionPos().getX()),
                Math.min(getY(), this.getCollisionPos().getY()),
                Math.min(getZ(), this.getCollisionPos().getZ()),
                Math.max(getX(), this.getCollisionPos().getX()),
                Math.max(getY(), this.getCollisionPos().getY()),
                Math.max(getZ(), this.getCollisionPos().getZ())).inflate(1, 1, 1));
        for (LivingEntity entity : entities) {
            if (entity == this.shooter) {
                continue;
            }
            float pad = entity.getPickRadius() + 0.5f;
            AABB aabb = entity.getBoundingBox().inflate(pad, pad, pad);
            Optional<Vec3> hit = aabb.clip(from, to);
            if (!(entity instanceof CreeperBulletEntity)) {
                if (aabb.contains(from)) {
                    hitResult.addEntityHit(entity);
                } else if (hit.isPresent()) {
                    hitResult.addEntityHit(entity);
                }
            }
        }

        double closestDistanceSq = Double.MAX_VALUE;
        LivingEntity closestDamaged = null;
        for (LivingEntity hit : hitResult.entities) {
            if (hit != this && hit.isAlive() && !hit.isRemoved()) {
                double distanceSq = hit.distanceToSqr(this);
                if (distanceSq < closestDistanceSq) {
                    closestDistanceSq = distanceSq;
                    closestDamaged = hit;
                }
            }
        }

        if (closestDamaged != null && hitResult.blockHit == null) {
            if (closestDamaged.hurt(new IndirectEntityDamageSource("arrow", this, this.shooter).setProjectile(), 0.5F)) {
                this.setCollisionPos(closestDamaged.getBlockX(), (int)(closestDamaged.getBlockY() + closestDamaged.getBbHeight() / 2), closestDamaged.getBlockZ());
                closestDamaged.invulnerableTime = 0;
            }
        }
        this.setPos(this.getCollisionPos().getX(), this.getCollisionPos().getY(), this.getCollisionPos().getZ());
        double mult = 0.75;
        this.setDeltaMovement(
                (this.random.nextDouble() - 0.5d) * mult,
                        0.75d,
                        (this.random.nextDouble() - 0.5d) * mult);
        this.playSound(YellowbrossExtrasSoundEvents.ENTITY_DEFENDER_CREEPERGUN_HIT.get(), 2.0F, this.getVoicePitch());
    }

    class DontExplodeUntilGotUpGoal extends CreeperExplodeGoal {

        public DontExplodeUntilGotUpGoal(AbstractCreeperEntity creeper) {
            super(creeper);
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !CreeperBulletEntity.this.wasShotFromDefender;
        }
    }

    class DoNothingUntilGotUpGoal extends Goal {

        protected DoNothingUntilGotUpGoal() {
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return CreeperBulletEntity.this.wasShotFromDefender && CreeperBulletEntity.this.getAnimationState().equals("getup") && CreeperBulletEntity.this.getUpTicks <= 20;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            CreeperBulletEntity.this.getUpTicks++;
        }

        @Override
        public void stop() {
            CreeperBulletEntity.this.wasShotFromDefender = false;
        }
    }
}
