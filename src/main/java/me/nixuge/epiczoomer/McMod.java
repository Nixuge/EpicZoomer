package me.nixuge.epiczoomer;

import lombok.Getter;
import lombok.Setter;
import me.nixuge.epiczoomer.keybinds.KeyInputHandler;
import me.nixuge.epiczoomer.keybinds.Keybinds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = McMod.MOD_ID,
        name = McMod.NAME,
        version = McMod.VERSION,
        clientSideOnly = true
)

@Setter
public class McMod {
    public static final String MOD_ID = "epiczoomer";
    public static final String NAME = "Epic Zoomer";
    public static final String VERSION = "1.2.0";


    @Getter
    @Mod.Instance(value = McMod.MOD_ID)
    private static McMod instance;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        Keybinds.register();
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
    }
}
