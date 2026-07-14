package com.yellowbrossproductions.yellowbrossextras.entities.creepers;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.EnumSet;
import java.util.List;

public class Paracreeper extends AbstractCreeperEntity implements CreeperInfection, Enemy {

    public Paracreeper(EntityType<? extends YExtrasMob> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CreeperExplodeGoal(this));
        this.goalSelector.addGoal(3, new MergeWithMyLoveGoal(this, Paracreeper.class));
        this.goalSelector.addGoal(3, new AlwaysWatchTargetGoal());
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, CreeperInfection.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, (p_29932_) -> {
            return p_29932_ instanceof Mob && !(p_29932_ instanceof CreeperInfection);
        }));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, (double)0.35F)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    public boolean causeFallDamage(float p_149687_, float p_149688_, DamageSource p_149689_) {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        this.setNoGravity(true);
        this.setYRot(this.getYHeadRot());
        this.yBodyRot = this.getYRot();

        EntityUtil.basicMobFlight(this, this.getTarget(), 9.0D, 1, 1);
    }

    @Override
    public void explodeCreeper() {
        if (!this.level.isClientSide) {
            Explosion.BlockInteraction explosion$blockinteraction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
            this.dead = true;
            this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float)(this.explosionRadius * 0.75F * f), explosion$blockinteraction);

            if (this.getAbsorbedCreepers() >= this.getMaxAbsorbs()) {
                Sneaker creeper = new Sneaker(YEEntityTypes.Sneaker.get(), this.level);
                creeper.copyPosition(this);
                if (this.getTeam() != null) {
                    level.getScoreboard().addPlayerToTeam(creeper.getStringUUID(),
                            level.getScoreboard().getPlayerTeam(this.getTeam().getName()));
                }
                if (random.nextInt(4) == 0) {
                    creeper.setCreeperType(1);
                    List<Sneaker> list = level.getEntitiesOfClass(Sneaker.class, creeper.getBoundingBox().inflate(30.0F), predicate -> {
                        return predicate.getCreeperType() == 2 && predicate != creeper;
                    });
                    if (list.isEmpty()) {
                        if (random.nextInt(3) == 0) {
                            creeper.setCreeperType(2);
                        }
                    }
                }

                this.level.addFreshEntity(creeper);

                CameraShake.cameraShake(this.level, position(), 40, 0.2f, 0, 30);
            }

            CameraShake.cameraShake(this.level, position(), 20, 0.05f, 0, 15);
            this.discard();
            this.spawnLingeringCloud();
        }

    }

    @Override
    public void die(DamageSource p_21014_) {
        this.ignite();
    }

    @Override
    protected void tickDeath() {
        this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
    }

    @Override
    protected int getMaxAbsorbs() {
        return 2;
    }

    class AlwaysWatchTargetGoal extends Goal {

        public AlwaysWatchTargetGoal() {
            this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            return getTarget() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return getTarget() != null;
        }

        @Override
        public void tick() {
            getNavigation().stop();

            if (getTarget() != null) {
                getLookControl().setLookAt(getTarget(), 100.0F, 100.0F);
            }

            navigation.stop();
        }
    }
}
