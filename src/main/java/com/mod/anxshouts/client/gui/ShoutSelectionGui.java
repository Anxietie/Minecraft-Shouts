package com.mod.anxshouts.client.gui;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.components.IShout;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class ShoutSelectionGui extends LightweightGuiDescription {
    WColorButton selectedShoutBuffer = null;

    public ShoutSelectionGui(PlayerEntity player) {
        WGridPanel root = (WGridPanel) rootPanel;

        WBox shoutsBox = new WBox(Axis.VERTICAL);
        WBox unlockBox = new WBox(Axis.VERTICAL);
        WBox masterBox = new WBox(Axis.HORIZONTAL);

        root.setSize(10, 10);

        for (ShoutHandler.Shout shout : ShoutHandler.Shout.values()) {
            if (shout == ShoutHandler.Shout.NONE) continue;

            WColorButton shoutSelector = new WColorButton(Text.translatable("shouts." + shout.getId()));
            shoutSelector.setEnabled(IShout.KEY.get(player).hasShout(shout.ordinal()));
            if (IShout.KEY.get(player).getSelectedShout() == shout.ordinal()) {
                shoutSelector.setColor(0x4d_FFA500);
                selectedShoutBuffer = shoutSelector;
            }
            else {
                shoutSelector.setColor(0xFF_FFFFFF);
            }
            shoutSelector.setOnClick(() -> {
                // PacketByteBuf selectShoutPacket = PacketByteBufs.create();
                // selectShoutPacket.writeInt(shout.ordinal());
                // ClientPlayNetworking.send(ModPackets.SHOUT_SELECTION_ID, selectShoutPacket);
                // ShoutData.setSelectedShout((IEntityData) player, shout);
                IShout.KEY.get(player).setSelectedShout(shout.ordinal());
                shoutSelector.setColor(0x4dFFA500);
                shoutSelector.tick();
                if (selectedShoutBuffer != null) {
                    selectedShoutBuffer.setColor(0xFF_FFFFFF);
                    selectedShoutBuffer.tick();
                }
                selectedShoutBuffer = shoutSelector;
            });

            WButton unlocker = new WButton(new TextureIcon(new Identifier(MODID, "textures/gui/locked_widget.png")));
            unlocker.setEnabled(!IShout.KEY.get(player).hasShout(shout.ordinal()));
            unlocker.setOnClick(() -> {
                // PacketByteBuf unlockShoutPacket = PacketByteBufs.create();
                // unlockShoutPacket.writeInt(shout.ordinal());
                // ClientPlayNetworking.send(ModPackets.SHOUT_OBTAINED_ID, unlockShoutPacket);
                // ShoutData.obtainShout((IEntityData) player, shout);
                IShout.KEY.get(player).obtainShout(shout.ordinal());
                unlocker.setEnabled(false);
                shoutSelector.setEnabled(true);
                unlocker.tick();
                shoutSelector.tick();
            });

            shoutsBox.add(shoutSelector, 162, 18);
            unlockBox.add(unlocker);
        }

        masterBox.setSpacing(0);
        masterBox.add(shoutsBox);
        masterBox.add(unlockBox);

        root.add(new WScrollPanel(masterBox).setScrollingHorizontally(TriState.FALSE), 0, 0, 10, 10);

        root.validate(this);
    }

    public static class WColorButton extends WButton {
        private static final Identifier DARK_WIDGETS_LOCATION = new Identifier("libgui", "textures/widget/dark_widgets.png");
        private static final int BUTTON_HEIGHT = 20;
        private static final int ICON_SPACING = 2;
        protected int color = 0xFF_FFFFFF; // first 2 is alpha value

        public WColorButton() {}
        public WColorButton(@Nullable Text label) { super(label); }
        public WColorButton(@Nullable Icon icon) { super(icon); }
        public WColorButton(int color) {
            super();
            this.color = color;
        }
        public WColorButton(@Nullable Icon icon, @Nullable Text label) { super(icon, label); }
        public WColorButton(@Nullable Icon icon, @Nullable Text label, int color) {
            super(icon, label);
            this.color = color;
        }

        @Environment(EnvType.CLIENT)
        @Override
        public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
            boolean hovered = (mouseX>=0 && mouseY>=0 && mouseX<getWidth() && mouseY<getHeight());
            int state = 1; //1=regular. 2=hovered. 0=disabled.
            if (!isEnabled())
                state = 0;
            else if (hovered || isFocused())
                state = 2;

            float px = 1/256f;
            float buttonLeft = 0 * px;
            float buttonTop = (46 + (state*20)) * px;
            int halfWidth = getWidth()/2;
            if (halfWidth>198) halfWidth=198;
            float buttonWidth = halfWidth*px;
            float buttonHeight = 20*px;

            float buttonEndLeft = (200-(getWidth()/2)) * px;

            Identifier texture = getTexture(this);
            ScreenDrawing.texturedRect(context, x, y, getWidth()/2, 20, texture, buttonLeft, buttonTop, buttonLeft+buttonWidth, buttonTop+buttonHeight, color);
            ScreenDrawing.texturedRect(context, x+(getWidth()/2), y, getWidth()/2, 20, texture, buttonEndLeft, buttonTop, 200*px, buttonTop+buttonHeight, color);

            if (getIcon() != null)
                getIcon().paint(context, x+ICON_SPACING, y+(BUTTON_HEIGHT-iconSize)/2, iconSize);

            if (getLabel() != null) {
                int color = 0xE0E0E0;
                if (!isEnabled()) {
                    color = 0xA0A0A0;
                } /*else if (hovered) {
				color = 0xFFFFA0;
			}*/

                int xOffset = (getIcon() != null && alignment == HorizontalAlignment.LEFT) ? ICON_SPACING+iconSize+ICON_SPACING : 0;
                ScreenDrawing.drawStringWithShadow(context, getLabel().asOrderedText(), alignment, x + xOffset, y + ((20 - 8) / 2), width, color); //LibGuiClient.config.darkMode ? darkmodeColor : color);
            }
        }

        public static Identifier getTexture(WWidget widget) {
            return widget.shouldRenderInDarkMode() ? DARK_WIDGETS_LOCATION : ClickableWidget.WIDGETS_TEXTURE;
        }

        public void setColor(int color) { this.color = color; }
    }
}
