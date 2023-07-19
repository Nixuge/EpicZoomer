package me.nixuge.epiczoomer;

import java.io.File;

import lombok.Getter;
import lombok.Setter;
import me.nixuge.epiczoomer.config.ConfigCache;
import me.nixuge.epiczoomer.keybinds.KeyInputHandler;
import me.nixuge.epiczoomer.keybinds.Keybinds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = McMod.MOD_ID,
        name = McMod.NAME,
        version = McMod.VERSION,
        guiFactory = "me.nixuge.epiczoomer.gui.GuiFactory",
        clientSideOnly = true
)

@Setter
public class McMod {
    public static final String MOD_ID = "epiczoomer";
    public static final String NAME = "Epic Zoomer";
    public static final String VERSION = "1.2.4";


    @Getter
    @Mod.Instance(value = McMod.MOD_ID)
    private static McMod instance;
    @Getter
    private ConfigCache configCache;
    @Getter
    private Configuration configuration;

    @Mod.EventHandler
    public void init(FMLPreInitializationEvent event) {
        Keybinds.register();
        String configDirectory = event.getModConfigurationDirectory().toString();
        final File path = new File(configDirectory + File.separator + McMod.MOD_ID + ".cfg");
        this.configuration = new Configuration(path);
        this.configCache = new ConfigCache(this.configuration);
    }

    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());
        MinecraftForge.EVENT_BUS.register(configCache);
    }
}
