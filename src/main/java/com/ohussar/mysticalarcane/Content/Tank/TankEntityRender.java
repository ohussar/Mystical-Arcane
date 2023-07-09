package com.ohussar.mysticalarcane.Content.Tank;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;

public class TankEntityRender implements BlockEntityRenderer<TankEntity> {
    public TankEntityRender(BlockEntityRendererProvider.Context context){

    }
    @Override
    public void render(TankEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(blockEntity.fuelCount > 0){
            BlockRenderDispatcher block = Minecraft.getInstance().getBlockRenderer();
            poseStack.pushPose();
            poseStack.translate(0.0625f, 0.07f, 0.0625f);
            poseStack.scale(0.875f, 0.02f * blockEntity.fuelCount, 0.875f);
            block.renderSingleBlock(Blocks.REDSTONE_BLOCK.defaultBlockState(), poseStack, 
            bufferSource, packedLight, packedOverlay, null, null);
            poseStack.popPose();
        }
    }
}
