package com.yellowbrossproductions.yellowbrossextras.entities.defender.projectile;

import com.yellowbrossproductions.yellowbrossextras.entities.projectile.CustomAbstractHurtingProjectile;
import com.yellowbrossproductions.yellowbrossextras.init.YEEntityTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class OkayMisterImSorry extends CustomAbstractHurtingProjectile {

    public OkayMisterImSorry(EntityType<? extends CustomAbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    public OkayMisterImSorry(Level level, LivingEntity livingEntity, Vec3 targetPos) {
        super(YEEntityTypes.OkayMisterImSorry.get(), livingEntity, targetPos.x, targetPos.y, targetPos.z, level);
        this.xPower *= 0.25f;
        this.yPower *= 0.25f;
        this.zPower *= 0.25f;
        this.setYRot(livingEntity.getYRot());
    }

    @Override
    protected boolean canHitEntity(Entity pEntity) {
        return false;
    }

    @Override
    protected void onHit(HitResult pResult) {

    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean ignoreExplosion() {
        return true;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) this.hurtMarked = true;

        ProjectileUtil.rotateTowardsMovement(this, 0.7F);

        if (this.tickCount > 140) this.discard();

        super.tick();
    }
}
