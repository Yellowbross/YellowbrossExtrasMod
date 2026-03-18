package com.yellowbrossproductions.yellowbrossextras.world.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BlitzManager;
import com.yellowbrossproductions.yellowbrossextras.world.raids.bunnyblitz.BunnyBlitz;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;
import java.util.Set;

public class BunnyBlitzCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("bunnyblitz").requires((p_180498_) -> {
            return p_180498_.hasPermission(2);
        });
        builder.then(Commands.literal("start").then(Commands.argument("pos", BlockPosArgument.blockPos()).executes((command) -> {
            return start(command.getSource(), BlockPosArgument.getLoadedBlockPos(command, "pos"), false, false);
        })));
        builder.then(Commands.literal("start").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("centerOnVillage", BoolArgumentType.bool()).executes((command) -> {
            return start(command.getSource(), BlockPosArgument.getLoadedBlockPos(command, "pos"), BoolArgumentType.getBool(command, "centerOnVillage"), false);
        }))));
        builder.then(Commands.literal("start").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("centerOnVillage", BoolArgumentType.bool()).then(Commands.argument("absurdity", BoolArgumentType.bool()).executes((command) -> {
            return start(command.getSource(), BlockPosArgument.getLoadedBlockPos(command, "pos"), BoolArgumentType.getBool(command, "centerOnVillage"), BoolArgumentType.getBool(command, "absurdity"));
        })))));
        builder.then(Commands.literal("highlightBunnies").executes((command) -> {
            return highlightBunnies(command.getSource());
        }));
        builder.then(Commands.literal("skipToWave").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("wave", IntegerArgumentType.integer()).then(Commands.argument("range", IntegerArgumentType.integer()).executes((command) -> {
            return skipToWave(command.getSource(), BlockPosArgument.getLoadedBlockPos(command, "pos"), IntegerArgumentType.getInteger(command, "wave"), IntegerArgumentType.getInteger(command, "range"));
        })))));
        dispatcher.register(builder);
    }

    private static int start(CommandSourceStack p_180485_, BlockPos pos, boolean centerOnVillage, boolean absurdity) throws CommandSyntaxException {
        ServerPlayer serverplayer = p_180485_.getPlayerOrException();
        BunnyBlitz blitz = BlitzManager.createRaid(serverplayer.getLevel(), pos);
        blitz.addPlayerToRaid(serverplayer);
        if (centerOnVillage) {
            blitz.moveRaidCenterToNearbyVillageSection();
        }
        if (absurdity) {
            blitz.setAbsurdity(true);
        }
        if (blitz != null) {
            p_180485_.sendSuccess(Component.translatable("command.yellowbrossextras.bunny_blitz.start_success", (blitz.getCenter().getX() + ", " + blitz.getCenter().getY() + ", " + blitz.getCenter().getZ())), false);
        } else {
            p_180485_.sendFailure(Component.literal("Failed to create a raid in your local village"));
        }

        return 1;
    }

    private static int highlightBunnies(CommandSourceStack p_180485_) throws CommandSyntaxException {
        ServerPlayer serverplayer = p_180485_.getPlayerOrException();
        List<BunnyBlitz> list = BlitzManager.getRaids(serverplayer.getLevel());
        if (!list.isEmpty()) {
            int i = 0;
            for (BunnyBlitz blitz : list) {
                Set<LivingEntity> entities = blitz.getRaiders();
                for (LivingEntity entity : entities) {
                    if (entity.isAlive() && !entity.isRemoved()) {
                        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 300));
                        i += 1;
                    }
                }
            }
            p_180485_.sendSuccess(Component.translatable("command.yellowbrossextras.bunny_blitz.highlight_success", i), false);
        } else {
            p_180485_.sendFailure(Component.translatable("command.yellowbrossextras.bunny_blitz.overall_fail"));
        }
        return 1;
    }

    private static int skipToWave(CommandSourceStack p_180485_, BlockPos pos, int i, int range) throws CommandSyntaxException {
        ServerPlayer serverplayer = p_180485_.getPlayerOrException();
        List<BunnyBlitz> list = BlitzManager.getRaids(serverplayer.getLevel());

        if (!list.isEmpty()) {
            int i1 = 0;
            for (BunnyBlitz blitz : list) {
                if (blitz.getCenter().equals(pos) || blitz.getCenter().distSqr(pos) < range * 30) {
                    Set<LivingEntity> entities = blitz.getRaiders();
                    for (LivingEntity entity : entities) {
                        entity.kill();
                    }
                    blitz.skipToWave(i);
                    i1 += 1;
                }
            }
            if (i1 == 0) {
                p_180485_.sendFailure(Component.translatable("command.yellowbrossextras.bunny_blitz.skip_fail"));
            } else {
                p_180485_.sendSuccess(Component.translatable("command.yellowbrossextras.bunny_blitz.skip_success", i), false);
            }
        } else {
            p_180485_.sendFailure(Component.translatable("command.yellowbrossextras.bunny_blitz.overall_fail"));
        }

        return 1;
    }
}
