package com.ohussar.mysticalarcane.Content.Tank;

import com.ohussar.mysticalarcane.Base.AbstractManaBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
public class Tank extends AbstractManaBlock {

    public Tank(Properties p_49224_) {
        super(p_49224_);
    }
    
    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
            InteractionHand interactionHand, BlockHitResult blockHit) {
        BlockEntity b = level.getBlockEntity(blockPos);
        if(b instanceof TankEntity && interactionHand == InteractionHand.MAIN_HAND && !level.isClientSide()){
            if(((TankEntity)b).onItemClick(player.getMainHandItem(), player)){
                return InteractionResult.CONSUME;
            }else{
                return InteractionResult.PASS;
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    public void actionOnClick(BlockPos pos, Level level) {
        
    }
    private static final VoxelShape SHAPE = 
    Block.box(0, 0, 0, 16, 6, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext context){
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new TankEntity(p_153215_, p_153216_);
    }
    
}
