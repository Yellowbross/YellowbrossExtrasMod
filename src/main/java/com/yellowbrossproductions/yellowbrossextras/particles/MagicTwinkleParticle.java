package com.yellowbrossproductions.yellowbrossextras.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class MagicTwinkleParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private int frame;
    private final static int maxFrames = 5;
    private final static float gravityLimit = 0.007f;

    protected MagicTwinkleParticle(ClientLevel pLevel, SpriteSet spriteSet, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed, float pScale) {
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
            this.xd *= 0.93F;
            this.yd *= 0.93F;
            this.zd *= 0.93F;
            if (this.gravity > gravityLimit) {
                this.gravity *= 0.8F;
            }

            if (this.onGround) {
                this.remove();
            } else {
                if (this.lifetime % 2 == 0) {
                    if (this.frame++ >= maxFrames) this.frame = 0;
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
            return new MagicTwinkleParticle(pLevel, this.spriteSet, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed, 1.0f);
        }
    }

    @Override
    protected int getLightColor(float pPartialTick) {
        return 15728880;
    }
}
