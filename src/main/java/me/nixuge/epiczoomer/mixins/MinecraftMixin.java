package me.nixuge.epiczoomer.mixins;

import me.nixuge.epiczoomer.zoom.properties.ZoomProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Redirect(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/InventoryPlayer;changeCurrentItem(I)V"))
    private void changeCurrentItem(InventoryPlayer instance, int direction) {
        if (!ZoomProperties.isZooming())
            instance.changeCurrentItem(direction);
    }
}
