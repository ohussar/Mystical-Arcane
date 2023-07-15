package com.ohussar.mysticalarcane.Content.Blocks.Holder;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.ohussar.mysticalarcane.Main;
import com.ohussar.mysticalarcane.Networking.ButtonCall;
import com.ohussar.mysticalarcane.Networking.ModMessages;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class HolderScreen extends AbstractContainerScreen<HolderMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID,"textures/gui/holder.png");
    private Button button = null;
    public HolderScreen(HolderMenu menu, Inventory inv, Component component) {
        super(menu, inv, component);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - imageWidth)/2;
        int y = (height - imageHeight)/2;
        this.blit(stack, x, y, 0, 0, imageWidth, imageHeight);
        int buttonX = 67;
        int buttonY = 53;
        int width = 42;
        int height = 11;

        if(inBounds(mouseX, mouseY, x+buttonX, y+buttonY, width, height)){
            this.blit(stack, x+buttonX-1, y+buttonY-1, 176, 0, 44, 16);
            if(button == null){
                button = new Button(x+buttonX-1, y+buttonY-1, 44, 16, null, (btn) -> {
                    ModMessages.sendToServer(new ButtonCall(menu.getBlockEntity().getBlockPos()));
                });
                this.addWidget(button);
            }
        }

        //font.draw(stack, Component.translatable("menu.holder.button"), x+67, y+54, 0xffffffff);
        drawCenteredString(stack, font, Component.translatable("menu.holder.button"), x+87, y+56, 0xffffffff);
        
    }
 
    
    private boolean inBounds(int x, int y, int xs, int ys, int width, int height){
        if(x >= xs && x <= xs+width && y >= ys && y <= ys+height){
            return true;
        }
        return false;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta){
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, delta);
        renderTooltip(stack, mouseX, mouseY);
    }

}
