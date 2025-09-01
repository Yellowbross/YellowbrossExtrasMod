package com.yellowbrossproductions.yellowbrossextras.world.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.yellowbrossproductions.yellowbrossextras.world.bunnyblitz.BlitzManager;
import com.yellowbrossproductions.yellowbrossextras.world.bunnyblitz.BunnyBlitz;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.entity.raid.Raids;

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
}
