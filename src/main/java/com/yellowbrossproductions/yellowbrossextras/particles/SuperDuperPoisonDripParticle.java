package com.yellowbrossproductions.yellowbrossextras.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class SuperDuperPoisonDripParticle extends TextureSheetParticle {
    protected boolean hasTouchedGround = false;
    private final SpriteSet sprites;
    private int frame;
    private final int maxFrames = 7;

    protected SuperDuperPoisonDripParticle(ClientLevel pLevel, SpriteSet spriteSet, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, float pScale) {
        super(pLevel, pX, pY, pZ);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.quadSize *= pScale;
        this.lifetime = 100;
        this.xd += pXSpeed;
        this.yd += pYSpeed;
        this.zd += pZSpeed;
        this.frame = this.random.nextInt(7);
        this.sprites = spriteSet;
        this.pickSprite(sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        } else {
            this.yd -= (double)this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= (double)0.98F;
            this.yd *= (double)0.98F;
            this.zd *= (double)0.98F;

            if (this.onGround) {
                if (!this.hasTouchedGround) {
                    this.level.playLocalSound(this.x, this.y, this.z, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER, SoundSource.BLOCKS, Mth.randomBetween(this.random, 0.3F, 1), 1, false);
                    this.hasTouchedGround = true;
                }

                if (Math.random() < 0.05) {
                    this.remove();
                }

                this.xd *= (double)0.7F;
                this.zd *= (double)0.7F;
            } else {
                if (this.frame++ >= 7) this.frame = 0;
                if (!this.removed) this.setSprite(this.sprites.get(this.frame, this.maxFrames));
            }

            BlockPos blockpos = new BlockPos(this.x, this.y, this.z);
            double d0 = Math.max(this.level.getBlockState(blockpos).getCollisionShape(this.level, blockpos).max(Direction.Axis.Y, this.x - (double)blockpos.getX(), this.z - (double)blockpos.getZ()), (double)this.level.getFluidState(blockpos).getHeight(this.level, blockpos));
            if (d0 > 0.0D && this.y < (double)blockpos.getY() + d0) {
                this.remove();
            }

        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SuperDuperPoisonDripParticle(pLevel, this.spriteSet, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, 1.0f);
        }
    }
}
