package dev.rebel.hotlayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventHandler {
  private final HotLayer hotLayer;

  Set<String> prevKeys;

  EventHandler(HotLayer hotLayer) {
    this.hotLayer = hotLayer;
    this.prevKeys = new HashSet<>();
  }

  // https://forums.minecraftforge.net/topic/59857-112-solved-keyinputevent-get-keypressed/
  @SideOnly(Side.CLIENT)
  @SubscribeEvent(priority= EventPriority.NORMAL, receiveCanceled=true)
  public void onKeyInput(TickEvent.ClientTickEvent event) {
    if (event.phase != TickEvent.Phase.END) {
      return;
    }

    // ignore if left ALT key is held down.
    // it might seem odd to not just return here, but it turns out that is we don't call
    // `isPressed` on the skin layer keys, and nothing else does either, letting go of alt
    // will cause all the other key presses to "catch up".
    boolean ignore = Keyboard.isKeyDown(Keyboard.KEY_LMENU);

    boolean enableAllPressed = this.hotLayer.enableAllLayersKeyBinding.isPressed();
    boolean disableAllPressed = this.hotLayer.disableAllLayersKeyBinding.isPressed();
    if ((enableAllPressed || disableAllPressed) && !ignore) {
      boolean enableAll = enableAllPressed;

      // all except cape
      Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.HAT, enableAll);
      Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.JACKET, enableAll);
      Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.LEFT_PANTS_LEG, enableAll);
      Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.LEFT_SLEEVE, enableAll);
      Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.RIGHT_PANTS_LEG, enableAll);
      Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.RIGHT_SLEEVE, enableAll);

      playSound(enableAll ? 1F : 0.6667F);
      return;
    }

    for (Map.Entry<EnumPlayerModelParts, KeyBinding> entry : this.hotLayer.layerPartsKeyBinding.entrySet()) {
      if (entry.getValue().isPressed() && !ignore) {
        EnumPlayerModelParts part = entry.getKey();

        // todo: if holding shift or control, but no other keys, strictly enable/disable the selected part.
        // play "flat" sound if already enabled/disabled
        Minecraft.getMinecraft().gameSettings.switchModelPartEnabled(part);

        boolean isEnabled = Minecraft.getMinecraft().gameSettings.getModelParts().contains(part);
        playSound(isEnabled ? 2F : 1.5F);
      }
    }
  }

  private static void playSound(float pitch) {
    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), pitch));
  }
}
