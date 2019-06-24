package net.dumbcode.projectnublar.server.recipes;

import net.dumbcode.projectnublar.server.ProjectNublar;
import net.dumbcode.projectnublar.server.block.entity.CoalGeneratorBlockEntity;
import net.dumbcode.projectnublar.server.block.entity.MachineModuleBlockEntity;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public enum CoalGeneratorRecipe implements MachineRecipe<CoalGeneratorBlockEntity> {
    INSTANCE;

    @Override
    public boolean accepts(CoalGeneratorBlockEntity blockEntity, MachineModuleBlockEntity.MachineProcess process) {
        Item item = blockEntity.getHandler().getStackInSlot(process.getInputSlots()[0]).getItem();
        return item == Items.COAL;
    }

    @Override
    public int getRecipeTime(CoalGeneratorBlockEntity blockEntity, MachineModuleBlockEntity.MachineProcess process) {
        return 30; // 1.5s
    }

    @Override
    public void onRecipeFinished(CoalGeneratorBlockEntity blockEntity, MachineModuleBlockEntity.MachineProcess process) {

    }

    @Override
    public void onRecipeStarted(CoalGeneratorBlockEntity blockEntity, MachineModuleBlockEntity.MachineProcess process) {
        ItemStack inSlot = blockEntity.getHandler().getStackInSlot(process.getInputSlots()[0]);
        inSlot.shrink(1);
    }


    @Override
    public boolean acceptsInputSlot(CoalGeneratorBlockEntity blockEntity, int slotIndex, ItemStack testStack, MachineModuleBlockEntity.MachineProcess process) {
        if(slotIndex != 0)
            return false;
        return testStack.getItem() == Items.COAL;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return new ResourceLocation(ProjectNublar.MODID, "coal_generator");
    }

    @Override
    public int getCurrentConsumptionPerTick(CoalGeneratorBlockEntity blockEntity, MachineModuleBlockEntity.MachineProcess process) {
        return 0;
    }

    @Override
    public int getCurrentProductionPerTick(CoalGeneratorBlockEntity blockEntity, MachineModuleBlockEntity.MachineProcess process) {
        return 2000;
    }
}
