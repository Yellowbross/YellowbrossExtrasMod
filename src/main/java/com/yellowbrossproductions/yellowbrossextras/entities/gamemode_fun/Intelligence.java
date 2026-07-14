package com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.gamemode_fun.CapTheIntelGoal;
import com.yellowbrossproductions.yellowbrossextras.util.DelayedActionHandler;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class Intelligence extends Entity {
    private static final EntityDataAccessor<Integer> CAPTURES = SynchedEntityData.defineId(Intelligence.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TIME_UNTIL_RETURN = SynchedEntityData.defineId(Intelligence.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RESET_TIME = SynchedEntityData.defineId(Intelligence.class, EntityDataSerializers.INT);

    private double homeX;
    private double homeY;
    private double homeZ;
    private boolean captured = false;

    public Intelligence(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
        noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(CAPTURES, 0);
        this.entityData.define(TIME_UNTIL_RETURN, 1200);
        this.entityData.define(RESET_TIME, 1200);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        if (pCompound.contains("Team", 8)) {
            String s = pCompound.getString("Team");
            PlayerTeam playerteam = this.level.getScoreboard().getPlayerTeam(s);
            boolean flag = playerteam != null && this.level.getScoreboard().addPlayerToTeam(this.getStringUUID(), playerteam);
            if (!flag) {
                YellowbrossExtras.LOGGER.warn("Unable to add the Intelligence to team \"{}\" (that team probably doesn't exist)", (Object)s);
            }
        }
        this.setCaptures(pCompound.getInt("Captures"));
        this.setTimeUntilReturn(pCompound.getInt("TimeUntilReturn"));
        this.entityData.set(RESET_TIME, pCompound.getInt("ResetTime"));
        if (pCompound.contains("HomeCoords", 9)) {
            ListTag listtag = pCompound.getList("HomeCoords", 6);
            if (listtag.size() == 3) {
                this.setHomeCoords(listtag.getDouble(0), listtag.getDouble(1), listtag.getDouble(2));
            }
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.getTeam() != null) {
            pCompound.putString("Team", this.getTeam().getName());
        }
        pCompound.putInt("Captures", this.getCaptures());
        pCompound.putInt("TimeUntilReturn", this.getTimeUntilReturn());
        pCompound.putInt("ResetTime", this.entityData.get(RESET_TIME));
        pCompound.put("HomeCoords", this.newDoubleList(this.getHomeX(), this.getHomeY(), this.getHomeZ()));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public double getMyRidingOffset() {
        return super.getMyRidingOffset() + 0.5F;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount % 20 == 0) {
            List<PathfinderMob> playing = this.level.getEntitiesOfClass(PathfinderMob.class, this.getBoundingBox().inflate(150.0D), p -> {
                return !p.isRemoved() && p.isAlive() && p.getTeam() != null && p.getTeam() != this.getTeam() && p.getTags().contains("playing");
            });

            for (PathfinderMob mob : playing) {
                if (!doesThisMobHaveCapGoal(mob)) {
                    DelayedActionHandler.queueAction(() -> {
                        mob.goalSelector.addGoal(1, new CapTheIntelGoal(mob));
                        mob.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(mob, PathfinderMob.class, 10, true, false, p -> {
                            return EntityUtil.isEnemyCapping(p, mob);}));
                    });
                }
            }
        }

        this.setGlowingTag(this.hasMovedFromHome());
        if (this.hasMovedFromHome()) {
            if (!this.isPassenger()) {
                this.setTimeUntilReturn(this.getTimeUntilReturn() - 1);
                if (this.getTimeUntilReturn() <= 0) {
                    this.goHome();
                }
            } else {
                this.resetTimeUntilReturn();
                if (this.getVehicle() != null) {
                    this.getVehicle().setGlowingTag(true);
                }
            }
        }

        this.boardingCooldown = 0;

        if (this.getTeam() != null) {
            Team team = this.getTeam();
            List<Mob> cappers = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(2.0D), p -> {
                return !p.isRemoved() && p.isAlive() && p.getTeam() != null && p.getTeam() != team && p.getTags().contains("playing");
            });
            if (!cappers.isEmpty() && !this.isPassenger() && !this.captured) {
                Mob cap = this.getNearestCapper(cappers);
                this.startRiding(cap);
                this.playSound(YESoundEvents.CTF_FRIENDLY_TAKE.get(), 10000.0F, 1.0F);
            }

            if (this.getVehicle() instanceof LivingEntity vehicle) {
                if (!vehicle.isAlive() || vehicle.isRemoved()) {
                    this.stopRiding();
                }

                List<Intelligence> caplist = this.level.getEntitiesOfClass(Intelligence.class, this.getBoundingBox().inflate(60.0D), p -> {
                    return p.getTeam() == vehicle.getTeam();
                });
                Intelligence cap_intel = EntityUtil.getNearestIntel(caplist, vehicle);

                if (cap_intel != null) {
                    Vec3 vec3 = new Vec3(cap_intel.getHomeX(), cap_intel.getHomeY(), cap_intel.getHomeZ());
                    if (vehicle.distanceToSqr(vec3) <= 9.0D) {
                        this.capture(cap_intel.getTeam());
                    }
                }
            }
        }
    }

    public boolean doesThisMobHaveCapGoal(PathfinderMob mob) {
        Set<WrappedGoal> goals = mob.goalSelector.getAvailableGoals();
        for (WrappedGoal wrappedGoal : goals) {
            Goal goal = wrappedGoal.getGoal();
            if (goal instanceof CapTheIntelGoal) {
                return true;
            }
        }
        return false;
    }

    public void setCaptures(int i) {
        this.entityData.set(CAPTURES, i);
    }

    public int getCaptures() {
        return this.entityData.get(CAPTURES);
    }

    public int getTimeUntilReturn() {
        return this.entityData.get(TIME_UNTIL_RETURN);
    }

    public void setTimeUntilReturn(int i) {
        this.entityData.set(TIME_UNTIL_RETURN, i);
    }

    public void resetTimeUntilReturn() {
        this.entityData.set(TIME_UNTIL_RETURN, this.entityData.get(RESET_TIME));
    }

    public double getHomeX() {
        return this.homeX;
    }

    public double getHomeY() {
        return this.homeY;
    }

    public double getHomeZ() {
        return this.homeZ;
    }

    public void setHomeCoords(double x, double y, double z) {
        this.homeX = x;
        this.homeY = y;
        this.homeZ = z;
    }

    public boolean hasMovedFromHome() {
        BlockPos blockPos = new BlockPos(new Vec3(this.getHomeX(), this.getHomeY(), this.getHomeZ()));
        return this.distanceToSqr(Vec3.atCenterOf(blockPos)) > 9.0D;
    }

    public void capture(@Nullable Team team) {
        this.captured = true;
        this.setCaptures(this.getCaptures() + 1);
        this.playSound(YESoundEvents.CTF_FRIENDLY_CAP.get(), 10000.0F, 1.0F);

        List<Mob> cappers = this.level.getEntitiesOfClass(Mob.class, this.getBoundingBox().inflate(150.0D), p -> {
            return !p.isRemoved() && p.isAlive() && p.getTeam() == team;
        });
        for (Mob cap : cappers) {
            cap.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 3, true, true, true));
        }

        this.goHome();
    }

    public void goHome() {
        this.stopRiding();
        if (!this.level.isClientSide) {
            this.setPos(this.getHomeX() + 0.5D, this.getHomeY(), this.getHomeZ() + 0.5D);
        }
        this.resetTimeUntilReturn();
        if (!this.captured) this.playSound(YESoundEvents.CTF_ENEMY_RETURN.get(), 10000.0F, 1.0F);
        this.captured = false;
    }

    @Override
    public void stopRiding() {
        if (this.getVehicle() != null) {
            this.setPos(this.getX(), this.getVehicle().getY(), this.getZ());
            this.getVehicle().setGlowingTag(false);
        }
        super.stopRiding();
    }

    public Mob getNearestCapper(List<Mob> intels) {
        double d0 = -1.0D;
        Mob t = null;

        for(Mob t1 : intels) {
            double d1 = t1.distanceToSqr(this.getX(), this.getY(), this.getZ());
            if (d0 == -1.0D || d1 < d0) {
                d0 = d1;
                t = t1;
            }
        }

        return t;
    }
}
