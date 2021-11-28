package dev.rebel.hotlayer;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.util.IdentityHashMap;
import java.util.Map;

@Mod(modid = "hotlayer", name = "HotLayer", version = "1.0")
public class HotLayer {
    public Map<EnumPlayerModelParts, KeyBinding> layerPartsKeyBinding;
    public KeyBinding enableAllLayersKeyBinding;
    public KeyBinding disableAllLayersKeyBinding;

    @Mod.EventHandler
    public void onFMLInitialization(FMLInitializationEvent event) {
        // https://emxtutorials.wordpress.com/adding-custom-keybinds/
        String category = "Skin Layers";

        this.enableAllLayersKeyBinding = new KeyBinding("Enable All Skin Layers", Keyboard.KEY_RETURN, category); // numpad enter
        this.disableAllLayersKeyBinding = new KeyBinding("Disable All Skin Layers", Keyboard.KEY_NUMPAD0, category);

        this.layerPartsKeyBinding = new IdentityHashMap<>();
        this.layerPartsKeyBinding.put(EnumPlayerModelParts.CAPE, new KeyBinding("Toggle Cape", Keyboard.KEY_DECIMAL, category)); // numpad decimal
        this.layerPartsKeyBinding.put(EnumPlayerModelParts.HAT, new KeyBinding("Toggle Hat", Keyboard.KEY_NUMPAD8, category));
        this.layerPartsKeyBinding.put(EnumPlayerModelParts.JACKET, new KeyBinding("Toggle Jacket", Keyboard.KEY_NUMPAD5, category));
        this.layerPartsKeyBinding.put(EnumPlayerModelParts.LEFT_PANTS_LEG, new KeyBinding("Toggle Left Pants Leg", Keyboard.KEY_NUMPAD1, category));
        this.layerPartsKeyBinding.put(EnumPlayerModelParts.LEFT_SLEEVE, new KeyBinding("Toggle Left Sleeve", Keyboard.KEY_NUMPAD4, category));
        this.layerPartsKeyBinding.put(EnumPlayerModelParts.RIGHT_PANTS_LEG, new KeyBinding("Toggle Right Pants Leg", Keyboard.KEY_NUMPAD3, category));
        this.layerPartsKeyBinding.put(EnumPlayerModelParts.RIGHT_SLEEVE, new KeyBinding("Toggle Right Sleeve", Keyboard.KEY_NUMPAD6, category));

        ClientRegistry.registerKeyBinding(this.enableAllLayersKeyBinding);
        ClientRegistry.registerKeyBinding(this.disableAllLayersKeyBinding);
        for (KeyBinding keyBinding: this.layerPartsKeyBinding.values()) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }

        MinecraftForge.EVENT_BUS.register(new EventHandler(this));
    }
}
