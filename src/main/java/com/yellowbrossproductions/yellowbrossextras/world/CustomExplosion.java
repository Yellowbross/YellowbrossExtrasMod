package com.yellowbrossproductions.yellowbrossextras.world;

import com.yellowbrossproductions.yellowbrossextras.entities.defender.CreeperBullet;
import com.yellowbrossproductions.yellowbrossextras.util.EntityUtil;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;

public class CustomExplosion extends Explosion {
    private final Level level;
    private final float size;
    private final Entity source;
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    private boolean multipliesToScrewArmor;
    private boolean healExploder;

    public CustomExplosion(Level worldIn, @Nullable Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, Explosion.BlockInteraction modeIn, boolean multToScrewArmor, boolean healExploder) {
        super(worldIn, exploderIn, null, null, xIn, yIn, zIn, sizeIn, causesFireIn, modeIn);
        this.level = worldIn;
        this.size = sizeIn;
        this.source = exploderIn;
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
        this.radius = sizeIn;
        this.multipliesToScrewArmor = multToScrewArmor;
        this.healExploder = healExploder;
    }

    public void explode() {
        this.level.gameEvent(this.source, GameEvent.EXPLODE, new Vec3(this.x, this.y, this.z));

        float f2 = this.radius * 2.0F;
        int k1 = Mth.floor(this.x - (double)f2 - 1.0D);
        int l1 = Mth.floor(this.x + (double)f2 + 1.0D);
        int i2 = Mth.floor(this.y - (double)f2 - 1.0D);
        int i1 = Mth.floor(this.y + (double)f2 + 1.0D);
        int j2 = Mth.floor(this.z - (double)f2 - 1.0D);
        int j1 = Mth.floor(this.z + (double)f2 + 1.0D);
        List<Entity> list = this.level.getEntities(this.source, new AABB((double)k1, (double)i2, (double)j2, (double)l1, (double)i1, (double)j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.level, this, list, f2);
        Vec3 vec3 = new Vec3(this.x, this.y, this.z);

        for(int k2 = 0; k2 < list.size(); ++k2) {
            Entity entity = list.get(k2);
            if (!entity.ignoreExplosion() &&
                    (entity instanceof CreeperBullet || (!(this.getExploder() instanceof Mob mob) || EntityUtil.canHurtThisMob(entity, mob)))) {
                double d12 = Math.sqrt(entity.distanceToSqr(vec3)) / (double)f2;
                if (d12 <= 1.0D) {
                    double d5 = entity.getX() - this.x;
                    double d7 = (entity instanceof PrimedTnt ? entity.getY() : entity.getEyeY()) - this.y;
                    double d9 = entity.getZ() - this.z;
                    double d13 = Math.sqrt(d5 * d5 + d7 * d7 + d9 * d9);
                    if (d13 != 0.0D) {
                        d5 /= d13;
                        d7 /= d13;
                        d9 /= d13;
                        double d14 = (double)getSeenPercent(vec3, entity);
                        double d10 = (1.0D - d12) * d14;
                        if (entity.hurt(this.getDamageSource(), ((float)((int)((d10 * d10 + d10) / 2.0D * 7.0D * (double)f2 + 1.0D))) * (entity instanceof LivingEntity && this.multipliesToScrewArmor ? (EntityUtil.multiplyToScrewArmor((LivingEntity) entity, 0.25f)) : 1.0F))) {
                            if (this.healExploder && this.source instanceof LivingEntity living) {
                                living.heal(0.5F);
                            }
                        }
                        double d11 = d10;
                        if (entity instanceof LivingEntity) {
                            d11 = ProtectionEnchantment.getExplosionKnockbackAfterDampener((LivingEntity)entity, d10);
                        }

                        entity.setDeltaMovement(entity.getDeltaMovement().add(d5 * d11, d7 * d11, d9 * d11));
                    }
                }
            }
        }

    }

    public static CustomExplosion create(@Nonnull Entity exploderIn, double x, double y, double z, float sizeIn, boolean multipliesToScrewArmor, boolean healExploder) {
        return create(exploderIn.level, exploderIn, x, y, z, sizeIn, false, multipliesToScrewArmor, healExploder);
    }

    public static CustomExplosion create(@Nonnull Entity exploderIn, Vec3 position, float sizeIn, boolean multipliesToScrewArmor, boolean healExploder) {
        return create(exploderIn.level, exploderIn, position.x, position.y, position.z, sizeIn, false, multipliesToScrewArmor, healExploder);
    }

    public static CustomExplosion create(Level worldIn, @Nullable Entity exploderIn, double xIn, double yIn, double zIn, float sizeIn, boolean causesFireIn, boolean multipliesToScrewArmor, boolean healExploder) {
        Explosion.BlockInteraction mode = BlockInteraction.NONE;

        CustomExplosion explosion = new CustomExplosion(worldIn, exploderIn, xIn, yIn, zIn, sizeIn, causesFireIn, mode, multipliesToScrewArmor, healExploder);
        if (ForgeEventFactory.onExplosionStart(worldIn, explosion)) {
            return explosion;
        } else {
            if (worldIn instanceof ServerLevel) {
                explosion.explode();
                explosion.finalizeExplosion(false);
                explosion.clearToBlow();

                Iterator var12 = ((ServerLevel)worldIn).players().iterator();

                while(var12.hasNext()) {
                    ServerPlayer serverplayerentity = (ServerPlayer)var12.next();
                    if (serverplayerentity.distanceToSqr(xIn, yIn, zIn) < 4096.0) {
                        serverplayerentity.connection.send(new ClientboundExplodePacket(xIn, yIn, zIn, sizeIn, explosion.getToBlow(), explosion.getHitPlayers().get(serverplayerentity)));
                    }
                }
            }

            return explosion;
        }
    }
}
