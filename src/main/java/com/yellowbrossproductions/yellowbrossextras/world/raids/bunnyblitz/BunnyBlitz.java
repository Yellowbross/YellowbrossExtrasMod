package com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz;

import com.google.common.collect.Sets;
import com.yellowbrossproductions.yellowbrossextras.YellowbrossExtras;
import com.yellowbrossproductions.yellowbrossextras.config.YellowbrossExtrasConfig;
import com.yellowbrossproductions.yellowbrossextras.entities.goal.RunTowardBlitzGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

// A lot of the code here was borrowed from Custom Raid.
public class BunnyBlitz {
    private static final Component RAID_NAME_COMPONENT = Component.translatable("event.yellowbrossextras.bunny_blitz");
    private static final Component VICTORY = Component.translatable("event.yellowbrossextras.bunny_blitz.victory");
    private static final Component DEFEAT = Component.translatable("event.yellowbrossextras.bunny_blitz.defeat");
    private static final String RAIDERS_REMAINING = "event.yellowbrossextras.bunny_blitz.raiders_remaining";
    private static final String BOSSES_REMAINING = "event.yellowbrossextras.bunny_blitz.bosses_remaining";
    private static final Component RAID_BAR_VICTORY_COMPONENT = RAID_NAME_COMPONENT.copy().append(" - ").append(VICTORY);
    private static final Component RAID_BAR_DEFEAT_COMPONENT = RAID_NAME_COMPONENT.copy().append(" - ").append(DEFEAT);
    private final ServerBossEvent raidEvent = new ServerBossEvent(RAID_NAME_COMPONENT, BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6);
    private Optional<BlockPos> waveSpawnPos = Optional.empty();
    public final ServerLevel level;
    private final int id;
    protected Status status = Status.ONGOING;
    private float totalHealth;
    private final RandomSource random = RandomSource.create();
    private int raidCooldownTicks;
    protected int tick = 0;
    protected int stopTick = 0;
    protected int currentWave = 0;
    protected int currentSpawn = 0;
    protected Set<LivingEntity> raiders = Sets.newHashSet();
    private final Set<UUID> heroes = Sets.newHashSet();
    private boolean active;
    private boolean started;
    private int postRaidTicks;
    public int celebrationTicks;
    public static float raidRange = 120.0F;
    public boolean absurdity = false;

    protected BlockPos center;

    public BunnyBlitz(int id, ServerLevel world, BlockPos pos) {
        this.id = id;
        this.level = world;
        this.center = pos;
    }

    public BunnyBlitz(ServerLevel world, CompoundTag nbt) {
        this.level = world;
        this.id = nbt.getInt("blitz_id");
        this.status = Status.values()[nbt.getInt("blitz_status")];
        this.tick = nbt.getInt("blitz_tick");
        this.stopTick = nbt.getInt("stop_tick");
        this.currentWave = nbt.getInt("current_wave");
        this.started = nbt.getBoolean("started");
        this.totalHealth = nbt.getFloat("TotalHealth");
        this.absurdity = nbt.getBoolean("absurdity");
        {// for raid center position.
            CompoundTag tmp = nbt.getCompound("center_pos");
            this.center = new BlockPos(tmp.getInt("pos_x"), tmp.getInt("pos_y"), tmp.getInt("pos_z"));
        }
        {// for raiders entity id.
            ListTag list = nbt.getList("raiders", 10);
            for(int i = 0; i < list.size(); ++ i) {
                final int id = list.getInt(i);
                final Entity entity = world.getEntity(id);
                if (entity instanceof LivingEntity livingEntity) {
                    this.raiders.add(livingEntity);
                }
            }
        }
        {// for heroes uuid.
            ListTag list = nbt.getList("heroes", 10);
            for(int i = 0; i < list.size(); ++ i) {
                final UUID uuid = NbtUtils.loadUUID(list.getCompound(i));
                if(uuid != null) {
                    this.heroes.add(uuid);
                }
            }
        }
    }

    public void save(CompoundTag nbt) {
        nbt.putInt("blitz_id", this.id);
        nbt.putInt("blitz_status", this.status.ordinal());
        nbt.putInt("blitz_tick", this.tick);
        nbt.putInt("stop_tick", this.stopTick);
        nbt.putInt("current_wave", this.currentWave);
        nbt.putBoolean("started", this.started);
        nbt.putBoolean("absurdity", this.absurdity);
        nbt.putFloat("TotalHealth", this.totalHealth);
        {// for raid center position.
            CompoundTag tmp = new CompoundTag();
            tmp.putInt("pos_x", this.center.getX());
            tmp.putInt("pos_y", this.center.getY());
            tmp.putInt("pos_z", this.center.getZ());
            nbt.put("center_pos", tmp);
        }
        {// for raiders entity id.
            ListTag list = new ListTag();
            for(Entity entity : this.raiders) {
                list.add(IntTag.valueOf(entity.getId()));
            }
            nbt.put("raiders", list);
        }
        {// for heroes uuid.
            ListTag list = new ListTag();
            for(UUID uuid : this.heroes) {
                list.add(NbtUtils.createUUID(uuid));
            }
            nbt.put("heroes", list);
        }
    }

    public boolean isOver() {
        return this.isVictory() || this.isLoss();
    }

    public boolean isBetweenWaves() {
        return this.hasFirstWaveSpawned() && this.getTotalRaidersAlive() == 0 && this.raidCooldownTicks > 0;
    }

    public boolean hasFirstWaveSpawned() {
        return this.currentSpawn > 0;
    }

    public boolean isStopped() {
        return this.status == BunnyBlitz.Status.STOPPED;
    }

    public boolean isVictory() {
        return this.status == BunnyBlitz.Status.VICTORY;
    }

    public boolean isLoss() {
        return this.status == BunnyBlitz.Status.LOSS;
    }

    public float getTotalHealth() {
        return this.totalHealth;
    }

    public int getTotalRaidersAlive() {
        int count = 0;
        for (Entity entity : this.raiders) {
            if (entity.isAlive() && !entity.isRemoved()) {
                count++;
            }
        }
        return count;
    }

    public void tick() {
        if (!this.isStopped()) {
            if (this.status == BunnyBlitz.Status.ONGOING) {
                boolean flag = this.active;
                this.active = this.level.hasChunkAt(this.center);
                if (this.level.getDifficulty() == Difficulty.PEACEFUL) {
                    this.stop();
                    return;
                }

                if (flag != this.active) {
                    this.raidEvent.setVisible(this.active);
                }

                if (!this.active) {
                    return;
                }

                ++this.tick;
                if (this.tick >= 48000L) {
                    this.stop();
                    return;
                }

                int i = this.getTotalRaidersAlive();
                if (i == 0 && this.hasMoreWaves()) {
                    if (this.raidCooldownTicks <= 0) {
                        if (this.raidCooldownTicks == 0 && this.getGroupsSpawned() > 0) {
                            this.raidCooldownTicks = 300;
                            this.raidEvent.setName(RAID_NAME_COMPONENT);
                            return;
                        }
                    } else {
                        boolean flag1 = this.waveSpawnPos.isPresent();
                        boolean flag2 = !flag1 && this.raidCooldownTicks % 5 == 0;
                        if (flag1 && !this.level.isPositionEntityTicking(this.waveSpawnPos.get())) {
                            flag2 = true;
                        }

                        if (flag2) {
                            int j = 0;
                            if (this.raidCooldownTicks < 100) {
                                j = 1;
                            } else if (this.raidCooldownTicks < 40) {
                                j = 2;
                            }

                            this.waveSpawnPos = this.getValidSpawnPos(j);
                        }

                        if (this.raidCooldownTicks == 300 || this.raidCooldownTicks % 20 == 0) {
                            this.updatePlayers();
                        }

                        --this.raidCooldownTicks;
                        this.raidEvent.setProgress(Mth.clamp((float)(300 - this.raidCooldownTicks) / 300.0F, 0.0F, 1.0F));
                    }
                }
                if (i > 0) {
                    this.updateBossbar();
                }

                if (this.tick % 20L == 0L) {
                    this.updatePlayers();
                    this.updateRaiders();
                    if (i > 0) {
                        if (i <= 10) {
                            this.raidEvent.setName(RAID_NAME_COMPONENT.copy().append(" - ").append(Component.translatable("event.yellowbrossextras.bunny_blitz.raiders_remaining", i)));
                        } else {
                            this.raidEvent.setName(RAID_NAME_COMPONENT);
                        }
                    } else {
                        this.raidEvent.setName(RAID_NAME_COMPONENT);
                    }
                }

                boolean flag3 = false;
                int k = 0;

                while(this.shouldSpawnGroup()) {
                    BlockPos blockpos = this.waveSpawnPos.isPresent() ? this.waveSpawnPos.get() : this.findRandomSpawnPos(k, 20);
                    if (blockpos != null) {
                        this.started = true;
                        this.spawnGroup(blockpos);
                        if (!flag3) {
                            this.playSound(blockpos);
                            flag3 = true;
                        }
                    } else {
                        ++k;
                    }

                    if (k > 3) {
                        this.stop();
                        break;
                    }
                }

                if (this.isStarted() && !this.hasMoreWaves() && i == 0) {
                    if (this.postRaidTicks < 40) {
                        ++this.postRaidTicks;
                    } else {
                        this.status = BunnyBlitz.Status.VICTORY;

                        for(UUID uuid : this.heroes) {

                        }
                    }
                }
            } else if (this.isOver()) {
                ++this.celebrationTicks;
                if (this.celebrationTicks >= 600) {
                    this.stop();
                    return;
                }

                if (this.celebrationTicks % 20 == 0) {
                    this.updatePlayers();
                    this.raidEvent.setVisible(true);
                    if (this.isVictory()) {
                        this.raidEvent.setProgress(0.0F);
                        this.raidEvent.setName(RAID_BAR_VICTORY_COMPONENT);
                    } else {
                        this.raidEvent.setName(RAID_BAR_DEFEAT_COMPONENT);
                    }
                }
            }

        }
    }

    private Predicate<ServerPlayer> validPlayer() {
        return (p_37723_) -> {
            BlockPos blockpos = p_37723_.blockPosition();
            return p_37723_.isAlive();
        };
    }

    private void updatePlayers() {
        Set<ServerPlayer> set = Sets.newHashSet(this.raidEvent.getPlayers());
        List<ServerPlayer> list = this.level.getPlayers(this.validPlayer());

        for(ServerPlayer serverplayer : list) {
            if (!set.contains(serverplayer)) {
                this.raidEvent.addPlayer(serverplayer);
            }
        }

        for(ServerPlayer serverplayer1 : set) {
            if (!list.contains(serverplayer1)) {
                this.raidEvent.removePlayer(serverplayer1);
            }
        }

    }

    private void updateRaiders() {

    }

    private void playSound(BlockPos p_37744_) {
        float f = 13.0F;
        int i = 64;
        Collection<ServerPlayer> collection = this.raidEvent.getPlayers();
        long j = this.random.nextLong();

        for(ServerPlayer serverplayer : this.level.players()) {
            Vec3 vec3 = serverplayer.position();
            Vec3 vec31 = Vec3.atCenterOf(p_37744_);
            double d0 = Math.sqrt((vec31.x - vec3.x) * (vec31.x - vec3.x) + (vec31.z - vec3.z) * (vec31.z - vec3.z));
            double d1 = vec3.x + 13.0D / d0 * (vec31.x - vec3.x);
            double d2 = vec3.z + 13.0D / d0 * (vec31.z - vec3.z);
            if (d0 <= 128.0D || collection.contains(serverplayer)) {
                serverplayer.connection.send(new ClientboundSoundPacket(SoundEvents.RAID_HORN, SoundSource.NEUTRAL, d1, serverplayer.getY(), d2, 128.0F, 1.0F, j));
            }
        }

    }

    private boolean shouldSpawnGroup() {
        return this.raidCooldownTicks == 0 && (this.currentSpawn < this.getNumGroups()) && this.getTotalRaidersAlive() == 0;
    }

    private void spawnGroup(BlockPos pos) {
        boolean flag = false;
        int i = this.currentSpawn + 1;
        this.totalHealth = 0.0F;
        for(String string : YellowbrossExtrasConfig.bunnyBlitz_raiders.get()) {
            int j = this.getDefaultNumSpawns(string, i);
            int k = 0;
            k += 1;

            for(int l = 0; l < j; ++l) {
                boolean summonedMobFromConfig = summonMobFromConfig(string, pos);
                if (!summonedMobFromConfig) {
                    YellowbrossExtras.LOGGER.warn("Yellowbross's Extras couldn't spawn a mob! Check the config file for invalid registry names!");
                }
            }
        }

        this.waveSpawnPos = Optional.empty();
        this.currentSpawn += 1;
        this.updateBossbar();
    }

    // code borrowed from Dungeons Mobs
    private boolean summonMobFromConfig(String string, BlockPos blockPos) {
        List<? extends String> mobSpawns = YellowbrossExtrasConfig.bunnyBlitz_raiders.get();
        if (mobSpawns.isEmpty()) return false;

        List<String> splitArray = List.of(string.split(","));
        String trueMobID = splitArray.get(0);
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(trueMobID));
        if (entityType == null) return false;

        Entity entity = entityType.create(this.level);
        if (!(entity instanceof Mob)) return false;

        Mob mobEntity = (Mob) entity;
        DifficultyInstance difficultyForLocation = this.level.getCurrentDifficultyAt(blockPos.above());
        mobEntity.moveTo(blockPos.above(), 0.0F, 0.0F);
        if (!this.level.isClientSide) {
            mobEntity.finalizeSpawn((ServerLevelAccessor)this.level, difficultyForLocation, MobSpawnType.EVENT, (SpawnGroupData) null, (CompoundTag) null);
        }
        if (mobEntity instanceof Raider) {
            ((Raider) mobEntity).setCanJoinRaid(false);
        }
        this.joinRaid(0, mobEntity, blockPos, false);
        if (mobEntity instanceof PathfinderMob mob) {
            mob.goalSelector.addGoal(3, new RunTowardBlitzGoal(mob));
        }
        this.totalHealth += mobEntity.getHealth();
        mobEntity.setPersistenceRequired();
        return this.level.addFreshEntity(mobEntity);
    }

    private int getDefaultNumSpawns(String string, int wave) {
        List<? extends String> mobSpawns = YellowbrossExtrasConfig.bunnyBlitz_raiders.get();
        if (mobSpawns.isEmpty()) return 0;
        List<String> splitArray = List.of(string.split(","));
        if (splitArray.get(wave) == null) {
            return 0;
        }
        int i = Integer.parseInt(splitArray.get(wave));
        if (this.absurdity) {
            i*=3;
        }
        return i;
    }

    public void addPlayerToRaid(Player player) {
        this.heroes.add(player.getUUID());
    }

    public void joinRaid(int p_37714_, LivingEntity p_37715_, @Nullable BlockPos p_37716_, boolean p_37717_) {
        this.raiders.add(p_37715_);
    }

    public void updateBossbar() {
        this.raidEvent.setProgress(Mth.clamp(this.getHealthOfLivingRaiders() / this.totalHealth, 0.0F, 1.0F));
    }

    public float getHealthOfLivingRaiders() {
        float f = 0.0F;

        for(LivingEntity raider : this.raiders) {
            f += raider.getHealth();
        }

        return f;
    }

    public void moveRaidCenterToNearbyVillageSection() {
        Stream<SectionPos> stream = SectionPos.cube(SectionPos.of(this.center), 2);
        stream.filter(this.level::isVillage).map(SectionPos::center).min(Comparator.comparingDouble((p_37766_) -> {
            return p_37766_.distSqr(this.center);
        })).ifPresent(this::setCenter);
    }

    private Optional<BlockPos> getValidSpawnPos(int p_37764_) {
        for(int i = 0; i < 3; ++i) {
            BlockPos blockpos = this.findRandomSpawnPos(p_37764_, 1);
            if (blockpos != null) {
                return Optional.of(blockpos);
            }
        }

        return Optional.empty();
    }

    private boolean hasMoreWaves() {
        return !this.isFinalWave();
    }

    public boolean isStarted() {
        return this.started;
    }

    public int getGroupsSpawned() {
        return this.currentSpawn;
    }

    private boolean isFinalWave() {
        return this.getGroupsSpawned() == this.getNumGroups();
    }

    public int getId() {
        return id;
    }

    public static float getRaidRange() {
        return raidRange;
    }

    public boolean isRaider(LivingEntity entity) {
        return this.raiders.contains(entity);
    }

    public void setAbsurdity(boolean absurdity) {
        this.absurdity = absurdity;
    }

    @Nullable
    private BlockPos findRandomSpawnPos(int p_37708_, int p_37709_) {
        int i = p_37708_ == 0 ? 2 : 2 - p_37708_;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i1 = 0; i1 < p_37709_; ++i1) {
            float f = this.level.random.nextFloat() * ((float)Math.PI * 2F);
            int j = this.center.getX() + Mth.floor(Mth.cos(f) * 32.0F * (float)i) + this.level.random.nextInt(5);
            int l = this.center.getZ() + Mth.floor(Mth.sin(f) * 32.0F * (float)i) + this.level.random.nextInt(5);
            int k = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, j, l);
            blockpos$mutableblockpos.set(j, k, l);
            if (!this.level.isVillage(blockpos$mutableblockpos) || p_37708_ >= 2) {
                int j1 = 10;
                if (this.level.hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10) && this.level.isPositionEntityTicking(blockpos$mutableblockpos) && (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.level, blockpos$mutableblockpos, EntityType.RAVAGER) || this.level.getBlockState(blockpos$mutableblockpos.below()).is(Blocks.SNOW) && this.level.getBlockState(blockpos$mutableblockpos).isAir())) {
                    return blockpos$mutableblockpos;
                }
            }
        }

        return null;
    }

    public BlockPos getCenter() {
        return this.center;
    }

    private void setCenter(BlockPos p_37761_) {
        this.center = p_37761_;
    }

    public void stop() {
        this.status = Status.STOPPED;
        this.raidEvent.removeAllPlayers();
        this.raiders.forEach(e -> e.remove(Entity.RemovalReason.KILLED));
    }

    public int getNumGroups() {
        return 10;
    }

    public Set<LivingEntity> getRaiders() {
        return this.raiders;
    }

    public void skipToWave(int wave) {
        int i = Math.min(wave, this.getNumGroups());
        this.currentWave = i;
        this.currentSpawn = i - 1;
    }

    static enum Status {
        ONGOING,
        VICTORY,
        LOSS,
        STOPPED;

        private static final Status[] VALUES = values();

        static Status getByName(String p_37804_) {
            for(Status raid$raidstatus : VALUES) {
                if (p_37804_.equalsIgnoreCase(raid$raidstatus.name())) {
                    return raid$raidstatus;
                }
            }

            return ONGOING;
        }

        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
