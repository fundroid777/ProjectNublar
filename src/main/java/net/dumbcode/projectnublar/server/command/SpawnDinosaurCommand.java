package net.dumbcode.projectnublar.server.command;

import com.google.common.collect.Lists;
import net.dumbcode.projectnublar.server.ProjectNublar;
import net.dumbcode.projectnublar.server.dinosaur.Dinosaur;
import net.dumbcode.projectnublar.server.entity.DinosaurEntity;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnDinosaurCommand extends CommandBase {
    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "this needs to be done";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        DinosaurEntity entity = new DinosaurEntity(sender.getEntityWorld(), ProjectNublar.DINOSAUR_REGISTRY.getValue(new ResourceLocation(args[0])));
        entity.setPosition(sender.getPositionVector().x, sender.getPositionVector().y, sender.getPositionVector().z);
        sender.getEntityWorld().spawnEntity(entity);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, ProjectNublar.DINOSAUR_REGISTRY.getValuesCollection().stream().map(Dinosaur::getRegName).map(Object::toString).collect(Collectors.toList())) : Lists.newArrayList();
    }
}
