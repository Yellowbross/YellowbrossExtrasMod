package com.yellowbrossproductions.yellowbrossextras.entities.goal.defender;

import com.yellowbrossproductions.yellowbrossextras.entities.CameraShake;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.Defender;
import com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile.OkayMisterImSorry;
import com.yellowbrossproductions.yellowbrossextras.init.YESoundEvents;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WatchYoToneGoal extends Goal {
    private final Defender defender;

    public WatchYoToneGoal(Defender defender) {
        this.defender = defender;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.defender.villagerSoul != null && !this.defender.villagerSoul.isRemoved() && this.defender.jumpscareTicks < 1 && this.defender.getTarget() == null && this.defender.attackType == 0 && this.defender.getPhase() == 1;
    }

    @Override
    public boolean canContinueToUse() {
        return this.defender.villagerSoul != null && !this.defender.villagerSoul.isRemoved() && this.defender.jumpscareTicks < 110 && this.defender.getTarget() == null;
    }

    @Override
    public void tick() {
        if (this.defender.villagerSoul != null && !this.defender.villagerSoul.isRemoved()) {
            Entity soul = this.defender.villagerSoul;

            this.defender.getNavigation().stop();

            boolean shouldJump = this.defender.distanceTo(soul) < 10.0d && this.defender.jumpscareTicks < 1 && soul.tickCount >= 48;
            if (shouldJump) {
                if (!defender.level.isClientSide) {
                    CameraShake.cameraShake(this.defender.level, this.defender.position(), 100.0f, 0.25f, 110, 40);
                    this.defender.setCustomRender(3);
                    this.defender.setImmediateTurn(true);
                    stopScreechingInMyEarYouUngratefulPestThatsWhyFreakagerMutatedYou(this.defender.level);
                    this.defender.playSound(YESoundEvents.ENTITY_DEFENDER_WATCHYOTONEBUDDYBOY.get(), 5.0F, 1.0F);
                    this.defender.jumpscareTicks = 1;
                    Vec3 position = soul.position().subtract(this.defender.position()).normalize().scale(1.3f);
                    OkayMisterImSorry okayMisterImSorry = new OkayMisterImSorry(this.defender.level, this.defender, position);
                    okayMisterImSorry.setPos(soul.position());
                    this.defender.level.addFreshEntity(okayMisterImSorry);
                    soul.discard();
                    this.defender.setVillagerSoul(okayMisterImSorry);
                }
            } else {
                this.defender.getLookControl().setLookAt(soul, 100.0F, 100.0F);
            }
        }
    }

    private void stopScreechingInMyEarYouUngratefulPestThatsWhyFreakagerMutatedYou(Level world) {
        MinecraftServer server = world.getServer();
        if (server == null) {
            return;
        }

        ClientboundStopSoundPacket sstopsoundpacket = new ClientboundStopSoundPacket(new ResourceLocation("illageandspillage", "entity.ragno.screech"), null);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            player.connection.send(sstopsoundpacket);
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        this.defender.setCustomRender(0);
        this.defender.setImmediateTurn(false);
        this.defender.villagerSoul = null;
        this.defender.jumpscareTicks = 0;
    }
}
