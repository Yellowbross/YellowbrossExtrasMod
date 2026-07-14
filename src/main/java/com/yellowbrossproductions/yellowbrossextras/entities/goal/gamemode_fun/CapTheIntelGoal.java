package com.yellowbrossproductions.yellowbrossextras.entities.goal.gamemode_fun;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.Intelligence;
import com.yellowbrossproductions.yellowbrossextras.entities.gamemode_fun.PathGuide;
import com.yellowbrossproductions.yellowbrossextras.packet.PacketHandler;
import com.yellowbrossproductions.yellowbrossextras.packet.ParticlePacket;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PacketDistributor;

import java.util.EnumSet;
import java.util.List;

public class CapTheIntelGoal extends Goal {
    Level level;
    private final PathfinderMob mob;
    BlockPos pos1;
    BlockPos pos2;
    BlockPos pos3;
    BlockPos pos4;
    Vec3 goal;
    BlockPos previous_pos1;
    BlockPos previous_pos2;
    int updateCooldown;

    public CapTheIntelGoal(PathfinderMob affected) {
        this.mob = affected;
        this.level = mob.getLevel();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return isItOkToFlee() &&
                mob.getTeam() != null &&
                !this.level.getEntitiesOfClass(Intelligence.class, mob.getBoundingBox().inflate(150.0D), p -> {
            return p.getTeam() != mob.getTeam();
        }).isEmpty() &&
                !this.level.getEntitiesOfClass(Intelligence.class, mob.getBoundingBox().inflate(150.0D), p -> {
                    return p.getTeam() == mob.getTeam();
                }).isEmpty();
    }

    public boolean isItOkToFlee() {
        if (this.mob.getTarget() != null) {
            LivingEntity target = this.mob.getTarget();
            if (EntityUtil.isEnemyCapping(target, this.mob)) return false;
            if (this.mob.distanceToSqr(target) < 50.0D) {
                return this.mob.getHealth() < this.mob.getMaxHealth() * 0.25F;
            }
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        this.updateCooldown -= 1;

        List<Intelligence> enemylist = this.level.getEntitiesOfClass(Intelligence.class, mob.getBoundingBox().inflate(150.0D), p -> {
            return p.getTeam() != mob.getTeam();
        });
        Intelligence enemy_intel = EntityUtil.getNearestIntel(enemylist, this.mob);

        List<Intelligence> caplist = this.level.getEntitiesOfClass(Intelligence.class, mob.getBoundingBox().inflate(150.0D), p -> {
            return p.getTeam() == mob.getTeam();
        });
        Intelligence cap_intel = EntityUtil.getNearestIntel(caplist, this.mob);

        if (enemy_intel != null && cap_intel != null) {
            List<PathGuide> guides = this.level.getEntitiesOfClass(PathGuide.class, mob.getBoundingBox().inflate(150.0D));
            PathGuide guide = EntityUtil.getNearestGuide(guides, this.mob);

            Vec3 home = new Vec3(cap_intel.getHomeX(), cap_intel.getHomeY(), cap_intel.getHomeZ());
            Vec3 enemy = new Vec3(enemy_intel.getX(), enemy_intel.getY(), enemy_intel.getZ());

            if (!this.mob.isPathFinding()) {
                boolean taken = !this.mob.hasPassenger(enemy_intel);

                if (taken) {
                    this.goal = enemy;
                } else {
                    this.goal = home;
                }

                if (guide != null) {
                    Vec3 guide_pos = Vec3.atCenterOf(guide.blockPosition());

                    this.reroute(guide);

                    if (this.mob.distanceToSqr(guide) > 9.0D && this.goal == null) {
                        this.goal = DefaultRandomPos.getPosTowards(this.mob, 7, 0, guide_pos, (double)((float)Math.PI / 2F));
                    }
                }

                if (taken) {
                    if (EntityUtil.hasLineOfSightToLocation(this.mob, enemy)) {
                        this.mob.getNavigation().moveTo(enemy_intel, 1.25D);
                    } else {
                        if (this.goal != null) this.mob.getNavigation().moveTo(this.goal.x, this.goal.y, this.goal.z, 1.25D);
                    }
                } else {
                    if (EntityUtil.hasLineOfSightToLocation(this.mob, home)) {
                        this.mob.getNavigation().moveTo(home.x, home.y, home.z, 1.25D);
                    } else {
                        if (this.goal != null) this.mob.getNavigation().moveTo(this.goal.x, this.goal.y, this.goal.z, 1.25D);
                    }
                }
            }
        }
        if (this.mob.getNavigation().getTargetPos() != null) {
            BlockPos debug = this.mob.getNavigation().getTargetPos();
            this.makeDebugParticles(debug.getX(), debug.getY(), debug.getZ());
        }

        // if (this.previous_pos1 != null) YellowbrossExtras.LOGGER.debug("Previous pos 1: " + this.previous_pos1);
        // if (this.previous_pos2 != null) YellowbrossExtras.LOGGER.debug("Previous pos 2: " + this.previous_pos2);
        if (this.pos1 != null) YellowbrossExtras.LOGGER.debug("pos 1: " + this.pos1);
        if (this.pos2 != null) YellowbrossExtras.LOGGER.debug("pos 2: " + this.pos2);
        if (this.pos3 != null) YellowbrossExtras.LOGGER.debug("pos 3: " + this.pos3);
        if (this.pos4 != null) YellowbrossExtras.LOGGER.debug("pos 4: " + this.pos4);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void reroute(PathGuide guide) {
        BlockPos go = EntityUtil.getNextGuide(guide, this.mob, this.goal);
        if (go != null) {
            if (!EntityUtil.checkGuide(go, this.mob, this.goal)) {
                PathGuide furtherGuide = EntityUtil.getGuideAt(go, this.mob.level);
                if (furtherGuide != null) {
                    boolean goodPath1 = false;
                    for (BlockPos further1 : furtherGuide.getNeighbors()) {
                        if (EntityUtil.checkGuide(further1, this.mob, this.goal) && checkPos(further1)) {
                            goodPath1 = true;
                            this.pos1 = further1;
                            YellowbrossExtras.LOGGER.debug("Found a path at " + this.pos1);
                        } else {
                            goodPath1 = false;
                        }
                    }
                    if (!goodPath1 && this.pos1 != null) {
                        boolean goodPath2 = false;
                        PathGuide furtherGuide2 = EntityUtil.getGuideAt(this.pos1, this.mob.level);
                        if (furtherGuide2 != null) {
                            for (BlockPos further1 : furtherGuide2.getNeighbors()) {
                                if (EntityUtil.checkGuide(further1, this.mob, this.goal) && checkPos(further1)) {
                                    goodPath2 = true;
                                    this.pos2 = further1;
                                    YellowbrossExtras.LOGGER.debug("Found another path at " + this.pos2);
                                } else {
                                    goodPath2 = false;
                                }
                            }
                            if (!goodPath2 && this.pos2 != null) {
                                boolean goodPath3 = false;
                                PathGuide furtherGuide3 = EntityUtil.getGuideAt(this.pos1, this.mob.level);
                                if (furtherGuide3 != null) {
                                    for (BlockPos further1 : furtherGuide3.getNeighbors()) {
                                        if (EntityUtil.checkGuide(further1, this.mob, goal) && checkPos(further1)) {
                                            goodPath3 = true;
                                            this.pos3 = further1;
                                            YellowbrossExtras.LOGGER.debug("Found yet another path at " + this.pos3);
                                        } else {
                                            goodPath3 = false;
                                        }
                                    }
                                    if (!goodPath3 && this.pos3 != null) {
                                        PathGuide furtherGuide4 = EntityUtil.getGuideAt(this.pos1, this.mob.level);
                                        if (furtherGuide4 != null) {
                                            for (BlockPos further1 : furtherGuide4.getNeighbors()) {
                                                if (EntityUtil.checkGuide(further1, this.mob, goal) && checkPos(further1)) {
                                                    this.pos4 = further1;
                                                    YellowbrossExtras.LOGGER.debug("Found final path at " + this.pos4);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.goal = Vec3.atCenterOf(go);
        }
        if (this.mob.distanceToSqr(this.goal) < 12.0D) {
            if (this.pos1 != null) {
                this.goal = Vec3.atCenterOf(this.pos1);
                if (this.mob.distanceToSqr(Vec3.atCenterOf(this.pos1)) < 12.0D) {
                    this.pos1 = null;
                    if (this.pos2 != null) {
                        this.goal = Vec3.atCenterOf(this.pos2);
                        if (this.mob.distanceToSqr(Vec3.atCenterOf(this.pos2)) < 12.0D) {
                            this.pos2 = null;
                            if (this.pos3 != null) {
                                this.goal = Vec3.atCenterOf(this.pos3);
                                if (this.mob.distanceToSqr(Vec3.atCenterOf(this.pos3)) < 12.0D) {
                                    this.pos3 = null;
                                    if (this.pos4 != null) {
                                        this.goal = Vec3.atCenterOf(this.pos4);
                                        if (this.mob.distanceToSqr(Vec3.atCenterOf(this.pos4)) < 12.0D) {
                                            this.pos4 = null;
                                            this.goal = null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                this.goal = null;
            }
        }
        if (this.updateCooldown < 1) {
            BlockPos pos = this.mob.blockPosition();
            this.previous_pos2 = this.previous_pos1;
            this.previous_pos1 = pos;
            this.updateCooldown = 40;
        }
    }

    private void makeDebugParticles(double x, double y, double z) {
        if (!this.level.isClientSide) {
            for (ServerPlayer serverPlayer : ((ServerLevel)this.level).players()) {
                if (serverPlayer.distanceToSqr(this.mob) < 4096.0D) {
                    ParticlePacket packet = new ParticlePacket();

                    for(int i = 0; i < 10; ++i) {
                        packet.queueParticle(ParticleTypes.CRIT, false, new Vec3(x + 0.5, y + 1, z + 0.5), new Vec3(0, 0, 0));
                    }

                    PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
                }
            }
        }
    }

    private boolean checkPos(BlockPos pos) {
        double radiusLimit = 9.0D;
        if (this.previous_pos1 != null) {
            if (pos.distSqr(this.previous_pos1) <= radiusLimit) return false;
        }
        if (this.previous_pos2 != null) {
            if (pos.distSqr(this.previous_pos2) <= radiusLimit) return false;
        }
        if (this.pos1 != null) {
            if (pos.distSqr(this.pos1) <= radiusLimit) return false;
        }
        if (this.pos2 != null) {
            if (pos.distSqr(this.pos2) <= radiusLimit) return false;
        }
        if (this.pos3 != null) {
            if (pos.distSqr(this.pos3) <= radiusLimit) return false;
        }
        if (this.pos4 != null) {
            if (pos.distSqr(this.pos4) <= radiusLimit) return false;
        }
        return true;
    }
}
