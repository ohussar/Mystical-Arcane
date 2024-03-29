package com.ohussar.mysticalarcane.Content.Blocks.ItemAltar;

import com.ohussar.mysticalarcane.Base.AbstractManaBlock;
import com.ohussar.mysticalarcane.Base.ModBlockEntities;
import com.ohussar.mysticalarcane.Content.Items.ArcaneWand.ArcaneWand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ItemAltarBlock extends AbstractManaBlock {

    public ItemAltarBlock(Properties properties) {
        super(properties);
    }


    private static final VoxelShape SHAPE = 
    Block.box(0, 0, 0, 16, 9, 16);

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player,
            InteractionHand interactionHand, BlockHitResult blockHit) {
        ItemStack stack = player.getMainHandItem();
        if(!level.isClientSide() && interactionHand == InteractionHand.MAIN_HAND){
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof ItemAltarBlockEntity &&
                !(stack.getItem() instanceof ArcaneWand) ){
                ItemAltarBlockEntity AltarEntity = ((ItemAltarBlockEntity)blockEntity);
                if(!AltarEntity.isCrafting){
                    AltarEntity.exchangeItem(stack, player);
                }
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
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext context){
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }
    
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
        return new ItemAltarBlockEntity(pos, state);
    }
    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState,
            boolean isMoving) {
        if(blockState.getBlock() != newBlockState.getBlock()){
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if(blockEntity instanceof ItemAltarBlockEntity){
                ((ItemAltarBlockEntity) blockEntity).drops();
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState,
            BlockEntityType<T> blockEntityType) {
        
        return createTickerHelper(blockEntityType, ModBlockEntities.ITEM_ALTAR_ENTITY.get(), ItemAltarBlockEntity::tick);
    }

    @Override
    public void actionOnClick(BlockPos pos, Level level) {
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof ItemAltarBlockEntity){
            ItemAltarBlockEntity altar = (ItemAltarBlockEntity) entity;
            altar.startCrafting(altar);
        }
    }
}
