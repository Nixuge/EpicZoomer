package me.nixuge.epiczoomer.keybinds;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;

import static net.minecraftforge.fml.client.registry.ClientRegistry.registerKeyBinding;

public class Keybinds {
    public static KeyBinding zoom = new KeyBinding("Zoomifier", Keyboard.KEY_H, "ZoomerMod");
    public static void register() {
        registerKeyBinding(zoom);
    }
}
