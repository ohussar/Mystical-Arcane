package com.ohussar.mysticalarcane.Content.Blocks.Holder;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;

public class HolderEntityRenderer implements BlockEntityRenderer<HolderEntity>{
    public HolderEntityRenderer(BlockEntityRendererProvider.Context context){
        
    }

    @Override
    public void render(HolderEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(blockEntity.holder.getStackInSlot(0) != ItemStack.EMPTY){

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            ItemStack stack = blockEntity.holder.getStackInSlot(0);
            poseStack.pushPose();
            poseStack.translate(0.5f, 0.05f, 0.5f);
            poseStack.scale(0.3f, 0.3f, 0.3f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(90));
            itemRenderer.renderStatic(stack, ItemTransforms.TransformType.GUI, 
            getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), 
            OverlayTexture.NO_OVERLAY, poseStack, bufferSource, 1);
            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
