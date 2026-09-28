package com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.bosses;

import com.yellowbrossproductions.yellowbrossextras.entities.Vilvgaver;
import com.yellowbrossproductions.yellowbrossextras.entities.YExtrasMob;
import com.yellowbrossproductions.yellowbrossextras.entities.oryctolins.AbstractOryctolin;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public abstract class BossOryctolin extends AbstractOryctolin {
    private static final EntityDataAccessor<Boolean> FORCEFIELD = SynchedEntityData.defineId(BossOryctolin.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ACTIVE = SynchedEntityData.defineId(BossOryctolin.class, EntityDataSerializers.BOOLEAN);

    public BossOryctolin(EntityType<? extends YExtrasMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FORCEFIELD, false);
        this.entityData.define(ACTIVE, false);
    }
}
