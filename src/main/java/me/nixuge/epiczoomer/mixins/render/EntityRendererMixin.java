package me.nixuge.epiczoomer.mixins.render;

import me.nixuge.epiczoomer.manager.ForgeHookClientProperties;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    // Welcome to my Q&A
    // Q: Why does this set a boolean to get processed in ForgeHookClientMixin
    //    instead of just doing the whole thing here ?
    // A: Optifine overwrites the only Redirect I could use and I can't redirect
    //    anything else useful here lmfao

    @Inject(method = "getFOVModifier", at = @At("HEAD"))
    public void owo(float partialTicks, boolean useFOVSetting, CallbackInfoReturnable<Float> cir) {
        ForgeHookClientProperties.setShouldChangeFov(true);
    }
}
