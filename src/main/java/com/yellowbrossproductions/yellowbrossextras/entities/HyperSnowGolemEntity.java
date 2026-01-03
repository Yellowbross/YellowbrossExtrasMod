package com.yellowbrossproductions.yellowbrossextras.entities;

import com.yellowbrossproductions.yellowbrossextras.entities.projectile.HyperSnowballEntity;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

public class HyperSnowGolemEntity extends SnowGolem implements YextrasEntity {
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D, 1.0000001E-5F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 1, true, false, (p_29932_) -> {
            return p_29932_ instanceof Enemy;
        }));
    }

    public HyperSnowGolemEntity(EntityType<? extends SnowGolem> p_29902_, Level p_29903_) {
        super(p_29902_, p_29903_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 400.0D)
                .add(Attributes.MOVEMENT_SPEED, (double)0.3F)
                .add(Attributes.ATTACK_DAMAGE, 0.0000001F)
                .add(Attributes.FOLLOW_RANGE, 96.0d);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getTarget() != null && this.isAggressive()) {
            for (int i = 0; i < 10; ++i) {
                this.performRangedAttack(this.getTarget(), 0.0f);
            }

            double x = (int)(getTarget().getX() + random.nextInt(80) - 40);
            double z = (int)(getTarget().getZ() + random.nextInt(80) - 40);
            int worldHeight = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int)x, (int)z);
            if (worldHeight > level.getMinBuildHeight() + 1) {
                EntityUtil.makeSimpleTrail(this, ParticleTypes.SONIC_BOOM, 50,
                        this.getX(), this.getY(), this.getZ(),
                        x, worldHeight, z);
                this.playSound(SoundEvents.ENDERMAN_TELEPORT, 3.0F, 1.0F);
                this.setPos(x, worldHeight, z);
            }
        }
    }

    @Override
    public void performRangedAttack(LivingEntity p_29912_, float p_29913_) {
        HyperSnowballEntity snowball = new HyperSnowballEntity(this.level, this);
        double d0 = p_29912_.getEyeY() - (double)1.1F;
        double d1 = p_29912_.getX() - this.getX();
        double d2 = d0 - snowball.getY();
        double d3 = p_29912_.getZ() - this.getZ();
        double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.2F;
        snowball.shoot(d1, d2 + d4, d3, 3.0F, 30.0F);
        this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 3.0F, (0.5F + this.getRandom().nextFloat() + (this.getRandom().nextFloat() / 2.0F)));
        this.level.addFreshEntity(snowball);
    }

    @Override
    public boolean hurt(DamageSource p_21016_, float p_21017_) {
        if (p_21016_.getEntity() == this) {
            return false;
        }
        return super.hurt(p_21016_, p_21017_);
    }

    @Override
    public boolean isSensitiveToWater() {
        return false;
    }
}
