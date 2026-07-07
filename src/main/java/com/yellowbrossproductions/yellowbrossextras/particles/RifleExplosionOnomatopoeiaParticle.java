package com.yellowbrossproductions.yellowbrossextras.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class RifleExplosionOnomatopoeiaParticle extends TextureSheetParticle {
    final double originalX;
    final double originalY;
    final double originalZ;

    protected RifleExplosionOnomatopoeiaParticle(ClientLevel pLevel, SpriteSet spriteSet, double pX, double pY, double pZ, float pScale) {
        super(pLevel, pX, pY, pZ, 0.0D, 0.0D, 0.0D);
        this.lifetime = 40;
        this.quadSize = 2.5F;
        this.originalX = pX;
        this.originalY = pY;
        this.originalZ = pZ;
        this.setSpriteFromAge(spriteSet);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public int getLightColor(float pPartialTick) {
        return 15728880;
    }

    @Override
    public void tick() {
        this.x = this.originalX + (this.age > 7 ? 0 : (this.random.nextDouble() - 0.5) / 2);
        this.y = this.originalY + (this.age > 7 ? 0 : (this.random.nextDouble() - 0.5) / 2);
        this.z = this.originalZ + (this.age > 7 ? 0 : (this.random.nextDouble() - 0.5) / 2);
        if (this.age++ >= this.lifetime) {
            this.remove();
        }
    }

    @Override
    public void render(VertexConsumer pBuffer, Camera pRenderInfo, float pPartialTicks) {
        super.render(pBuffer, pRenderInfo, pPartialTicks);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new RifleExplosionOnomatopoeiaParticle(pLevel, this.spriteSet, pX, pY, pZ, 1.0f);
        }
    }
}
