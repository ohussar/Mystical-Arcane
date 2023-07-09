package com.ohussar.mysticalarcane.Content.ManaReceptor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ManaReceptorBlock extends Block {
    public static final DirectionProperty FACING = DirectionalBlock.FACING; 
    public static final BooleanProperty isAble = BooleanProperty.create("able");
    public ManaReceptorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
            this.stateDefinition.any()
            .setValue(FACING, Direction.NORTH)
         );
    }

    private static final VoxelShape SHAPE = 
    Block.box(3, 0, 3, 13, 16, 13);
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {

        Direction[] dir = Direction.orderedByNearest(context.getPlayer());
        Direction desiredDir = null;
        for(int k = 0; k < dir.length; k++){
            if(dir[k] == Direction.UP || dir[k] == Direction.DOWN){
                continue;
            }else if(desiredDir == null){
                desiredDir = dir[k];
                break;
            }
        }
        return this.defaultBlockState().setValue(FACING, desiredDir.getOpposite());
    } 
    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext context){
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }

}
