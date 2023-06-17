package codyy.toacrisp.registry;

import codyy.toacrisp.ToACrisp;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SandBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TACBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ToACrisp.MOD_ID);

    public static final RegistryObject<Block> ASH = BLOCKS.register("ash", () -> new SandBlock(0x3f3b3f, BlockBehaviour.Properties.copy(Blocks.SAND)));
}
