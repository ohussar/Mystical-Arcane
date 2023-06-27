package com.ohussar.mysticalarcane.Content;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ItemAltarBlock extends Block {

    public ItemAltarBlock(Properties properties) {
        super(properties);
    }
    private static final VoxelShape SHAPE = 
    Block.box(0, 0, 0, 16, 9, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext context){
        return SHAPE;
    }
}
