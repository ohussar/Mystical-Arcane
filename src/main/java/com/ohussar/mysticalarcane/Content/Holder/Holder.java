package com.ohussar.mysticalarcane.Content.Holder;

import com.ohussar.mysticalarcane.Base.AbstractManaBlock;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Content.ModBlocks;
import com.ohussar.mysticalarcane.Content.ArcaneWand.ArcaneWand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Holder extends AbstractManaBlock {
    public static final BooleanProperty isAbove = BooleanProperty.create("isabove");
    public Holder(Properties properties) {
        super(properties);
        isAbove.value(false);

        this.registerDefaultState(
            this.stateDefinition.any()
            .setValue(isAbove, false)
            );
    }
    private static final VoxelShape SHAPE = 
    Block.box(0, 0, 0, 16, 2, 16);

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext context){
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }
    @Override
    public void actionOnClick(BlockPos pos, Level level) {
        if(level.getBlockEntity(pos) instanceof HolderEntity block){
            block.tryCraft(level, block);
        }
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(isAbove);
    }
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HolderEntity(pos, state);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
            BlockEntityType<T> blockEntityType) {
        
        return createTickerHelper(blockEntityType, ModBlockEntities.HOLDER_ENTITY.get(), HolderEntity::tick);
    }
    @Override
    public void onPlace(BlockState nowstate, Level level, BlockPos pos, BlockState prevstate, boolean bool) {
        if(pos != null){
            if(level.getBlockState(pos.below()).getBlock() == ModBlocks.TANK.get()){
                level.setBlock(pos, nowstate.setValue(Holder.isAbove, true), 3);
            }
        }
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor){
        if(pos.below() == neighbor){
            if(level.getBlockState(neighbor).getBlock() == ModBlocks.TANK.get()){
                ((HolderEntity) level.getBlockEntity(pos)).changed = true;
            }
        }
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
            InteractionHand interactionHand, BlockHitResult blockHit) {
        ItemStack stack = player.getMainHandItem();
        if(!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND){
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof HolderEntity &&
                !(stack.getItem() instanceof ArcaneWand) ){

                 ((HolderEntity) blockEntity).onClick(level, player);


                return InteractionResult.SUCCESS;
            }else if(stack.getItem() instanceof ArcaneWand){
                return InteractionResult.PASS;
            }
        }
        if(stack.getItem() instanceof ArcaneWand){
            return InteractionResult.PASS;
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState,
            boolean isMoving) {
        if(blockState.getBlock() != newBlockState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof HolderEntity){
                ((HolderEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }
}
