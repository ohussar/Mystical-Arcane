package com.ohussar.mysticalarcane.Content.Blocks.Tank;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ohussar.mysticalarcane.Base.ModFluids;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;

public class TankEntityRender implements BlockEntityRenderer<TankEntity> {
    public TankEntityRender(BlockEntityRendererProvider.Context context){

    }
    @Override
    public void render(TankEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(blockEntity.getFluidCount() > 0){
            poseStack.pushPose();
            poseStack.translate(0.0625f, 0.07f, 0.0625f);
            poseStack.scale(0.875f, 0.000125f * blockEntity.getFluidCount(), 0.875f);
            FluidRenderer.renderFluidBox(new FluidStack(ModFluids.SOURCE_MANA_WATER.get(), 100), 
            0, 0, 0, 
            1, 1, 1, bufferSource, poseStack, packedLight, false);
            poseStack.popPose();
        }
    }
}
