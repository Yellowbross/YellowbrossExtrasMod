package com.yellowbrossproductions.yellowbrossextras.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

public class OryctolinShardParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private int frame;
    private final static int maxFrames = 7;
    private final int randomFallDelay;
    private final static float gravityLimit = 0.007f;

    protected OryctolinShardParticle(ClientLevel pLevel, SpriteSet spriteSet, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, float pScale) {
        super(pLevel, pX, pY, pZ);
        this.setSize(0.02F, 0.02F);
        this.gravity = 0.06F;
        this.quadSize *= pScale;
        this.lifetime = 200;
        this.xd += pXSpeed;
        this.yd += pYSpeed;
        this.zd += pZSpeed;
        this.frame = this.random.nextInt(maxFrames);
        this.sprites = spriteSet;
        this.pickSprite(sprites);
        this.randomFallDelay = this.random.nextInt(19);
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
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.95F;
            this.yd *= 0.95F;
            this.zd *= 0.95F;
            if (this.gravity > gravityLimit) {
                this.gravity *= 0.8F;
            }

            if (this.onGround) {
                if (Math.random() < 0.05) {
                    this.remove();
                }

                this.xd *= 0.7F;
                this.zd *= 0.7F;
            } else {
                if (this.gravity > gravityLimit) {
                    if (this.frame++ >= maxFrames) this.frame = 0;
                } else {
                    int gentleFallFrames = (this.lifetime + this.randomFallDelay) % 20;
                    switch (gentleFallFrames) {
                        case 0, 1, 16, 17, 18, 19 -> this.frame = 0;
                        case 2, 14, 15 -> this.frame = 1;
                        case 3, 13 -> this.frame = 2;
                        case 4, 5, 11, 12 -> this.frame = 3;
                        case 6, 7, 8, 9, 10 -> this.frame = 4;
                    }
                }
                if (!this.removed) this.setSprite(this.sprites.get(this.frame, maxFrames));
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
            return new OryctolinShardParticle(pLevel, this.spriteSet, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, 1.0f);
        }
    }
}
