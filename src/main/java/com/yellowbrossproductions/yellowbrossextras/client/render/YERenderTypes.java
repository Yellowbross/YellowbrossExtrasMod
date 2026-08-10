package com.yellowbrossproductions.yellowbrossextras.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.BiFunction;

@OnlyIn(Dist.CLIENT)
public abstract class YERenderTypes extends RenderType {

    public YERenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    private static final BiFunction<ResourceLocation, Boolean, RenderType> NO_SHADING_ALLOWED = Util.memoize((resourceLocation, compositeState) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.RENDERTYPE_BEACON_BEAM_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(resourceLocation, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(RenderStateShard.CULL)
                .setLightmapState(RenderStateShard.LIGHTMAP)
                .setOverlayState(RenderStateShard.OVERLAY)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .createCompositeState(compositeState);
        return create("noShadingAllowed", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, true, rendertype$compositestate);
    });

    public static RenderType noShadingAllowed(ResourceLocation resourceLocation, boolean compositeState) {
        return NO_SHADING_ALLOWED.apply(resourceLocation, compositeState);
    }

    public static RenderType getMask(ResourceLocation location) {
        RenderType.CompositeState rendertype = RenderType.CompositeState.builder().setShaderState(RENDERTYPE_ENTITY_GLINT_DIRECT_SHADER).setTextureState(new RenderStateShard.TextureStateShard(location, true, false)).setWriteMaskState(COLOR_WRITE).setCullState(NO_CULL).setDepthTestState(EQUAL_DEPTH_TEST).setTransparencyState(GLINT_TRANSPARENCY).setTexturingState(ENTITY_GLINT_TEXTURING).createCompositeState(false);

        return create("mask",
                DefaultVertexFormat.POSITION_TEX,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                rendertype);
    }
}
