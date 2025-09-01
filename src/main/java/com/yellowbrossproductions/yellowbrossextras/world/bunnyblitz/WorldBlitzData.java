package com.yellowbrossproductions.yellowbrossextras.world.bunnyblitz;

import com.google.common.collect.Maps;
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
public class WorldBlitzData extends SavedData {

    private static final String DATA_NAME = "CustomRaidData";
    private final Map<Integer, BunnyBlitz> raidMap = Maps.newHashMap();
    private int currentRaidId = 1;
    private int tick = 0;

    public WorldBlitzData() {
        super();
        this.setDirty();
    }

    public WorldBlitzData(ServerLevel world, CompoundTag nbt) {
        if (nbt.contains("current_id")) {
            this.currentRaidId = nbt.getInt("current_id");
        }
        final ListTag raidList = nbt.getList("blitzes", 10);
        for (int i = 0; i < raidList.size(); ++i) {
            final CompoundTag tmp = raidList.getCompound(i);
            final BunnyBlitz raid = new BunnyBlitz(world, tmp);
            this.raidMap.put(raid.getId(), raid);
        }
    }

    public void tick(Level world) {
        Iterator<BunnyBlitz> iterator = this.raidMap.values().iterator();
        while (iterator.hasNext()) {
            BunnyBlitz raid = iterator.next();
            if (raid.isStopped()) {
                iterator.remove();
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
        for (BunnyBlitz raid : this.raidMap.values()) {
            final CompoundTag tmp = new CompoundTag();
            raid.save(tmp);
            raidList.add(tmp);
        }
        nbt.put("blitzes", raidList);

        return nbt;
    }

    public BunnyBlitz createRaid(ServerLevel world, BlockPos pos) {
        final int id = this.getUniqueId();
        final BunnyBlitz raid = new BunnyBlitz(id, world, pos);
        this.addRaid(id, raid);
        return raid;
    }

    public void addRaid(int id, BunnyBlitz raid) {
        this.raidMap.put(id, raid);
        this.setDirty();
    }

    public int getUniqueId() {
        this.setDirty();
        return this.currentRaidId ++;
    }

    public List<BunnyBlitz> getRaids() {
        return new ArrayList<>(this.raidMap.values());
    }

    public static WorldBlitzData getInvasionData(Level worldIn) {
        if (!(worldIn instanceof ServerLevel)) {
            throw new RuntimeException("Attempted to get the data from a client world. This is wrong.");
        }
        return ((ServerLevel) worldIn).getDataStorage().computeIfAbsent((tag) -> new WorldBlitzData((ServerLevel) worldIn, tag), WorldBlitzData::new,
                DATA_NAME);
    }
}
