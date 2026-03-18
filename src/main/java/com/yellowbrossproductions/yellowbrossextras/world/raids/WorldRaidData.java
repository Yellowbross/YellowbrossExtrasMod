package com.yellowbrossproductions.yellowbrossextras.world.raids;

import com.google.common.collect.Maps;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BunnyBlitz;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// Code borrowed from Custom Raid
public class WorldRaidData extends SavedData {

    private static final String DATA_NAME = "YExtrasRaidData";
    private final Map<Integer, BunnyBlitz> blitzMap = Maps.newHashMap();
    private int currentRaidId = 1;
    private int tick = 0;

    public WorldRaidData() {
        super();
        this.setDirty();
    }

    public WorldRaidData(ServerLevel world, CompoundTag nbt) {
        if (nbt.contains("current_id")) {
            this.currentRaidId = nbt.getInt("current_id");
        }
        final ListTag blitzList = nbt.getList("blitzes", 10);
        for (int i = 0; i < blitzList.size(); ++i) {
            final CompoundTag tmp = blitzList.getCompound(i);
            final BunnyBlitz raid = new BunnyBlitz(world, tmp);
            this.blitzMap.put(raid.getId(), raid);
        }
    }

    public void tick(Level world) {
        Iterator<BunnyBlitz> iterator1 = this.blitzMap.values().iterator();
        while (iterator1.hasNext()) {
            BunnyBlitz raid = iterator1.next();
            if (raid.isStopped()) {
                iterator1.remove();
                this.setDirty();
            } else {
                world.getProfiler().push("Bunny Blitz Tick");
                raid.tick();
                world.getProfiler().pop();
            }
        }

        if (++ this.tick % 200 == 0) {
            this.setDirty();
        }
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        nbt.putInt("current_id", this.currentRaidId);

        final ListTag raidList = new ListTag();
        for (BunnyBlitz raid : this.blitzMap.values()) {
            final CompoundTag tmp = new CompoundTag();
            raid.save(tmp);
            raidList.add(tmp);
        }
        nbt.put("blitzes", raidList);

        return nbt;
    }

    public BunnyBlitz createBunnyBlitz(ServerLevel world, BlockPos pos) {
        final int id = this.getUniqueId();
        final BunnyBlitz raid = new BunnyBlitz(id, world, pos);
        this.addBunnyBlitz(id, raid);
        return raid;
    }

    public void addBunnyBlitz(int id, BunnyBlitz raid) {
        this.blitzMap.put(id, raid);
        this.setDirty();
    }

    public int getUniqueId() {
        this.setDirty();
        return this.currentRaidId ++;
    }

    public List<BunnyBlitz> getBunnyBlitzes() {
        return new ArrayList<>(this.blitzMap.values());
    }

    public static WorldRaidData getInvasionData(Level worldIn) {
        if (!(worldIn instanceof ServerLevel)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        return ((ServerLevel) worldIn).getDataStorage().computeIfAbsent((tag) -> new WorldRaidData((ServerLevel) worldIn, tag), WorldRaidData::new,
                DATA_NAME);
    }
}
