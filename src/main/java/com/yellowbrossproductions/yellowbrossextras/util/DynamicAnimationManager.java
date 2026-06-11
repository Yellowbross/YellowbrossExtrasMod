package com.yellowbrossproductions.yellowbrossextras.util;

import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.HashMap;
import java.util.Map;

// Base code in 1.19.2 provided by Google's Gemini, then I adapted and built off of it. This is how Vilvgaver's animated texture works dynamically.
public class DynamicAnimationManager extends SimplePreparableReloadListener<Map<String, Integer>> {

    public static final DynamicAnimationManager INSTANCE = new DynamicAnimationManager();

    private final Map<String, Integer> folderFrameCounts = new HashMap<>();

    @Override
    protected Map<String, Integer> prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        Map<String, Integer> newlyScannedFrames = new HashMap<>();

        String folderPath = "textures/entity/vilvgaver";

        Map<ResourceLocation, Resource> matches = resourceManager.listResources(
                folderPath,
                location -> location.getNamespace().equals(YellowbrossExtras.MOD_ID) && location.getPath().endsWith(".png")
        );

        newlyScannedFrames.put(folderPath, matches.size());
        return newlyScannedFrames;
    }

    @Override
    protected void apply(Map<String, Integer> object, ResourceManager resourceManager, ProfilerFiller profiler) {
        folderFrameCounts.clear();
        folderFrameCounts.putAll(object);
    }

    public int getFrameCount(String folderPath) {
        return folderFrameCounts.getOrDefault(folderPath, 1);
    }
}
