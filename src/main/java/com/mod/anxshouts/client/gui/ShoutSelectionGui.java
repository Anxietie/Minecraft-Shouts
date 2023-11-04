package com.mod.anxshouts.client.gui;

import com.mod.anxshouts.client.util.ShoutHandler;
import com.mod.anxshouts.components.IShout;
import com.mod.anxshouts.networking.ModPackets;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import static com.mod.anxshouts.MinecraftShouts.MODID;

public class ShoutSelectionGui extends LightweightGuiDescription {
    WColorButton selectedShoutBuffer = null;

    public ShoutSelectionGui(ClientPlayerEntity player) {
        WGridPanel root = (WGridPanel) rootPanel;

        WBox shoutsBox = new WBox(Axis.VERTICAL);
        WBox unlockBox = new WBox(Axis.VERTICAL);
        WBox masterBox = new WBox(Axis.HORIZONTAL);

        root.setSize(10, 10);

        IShout data = IShout.KEY.get(player);

        WLabel soulsCount = new WLabel(Text.literal("Souls: " + data.getSoulCount()));

        for (int i = 1; i < ShoutHandler.Shout.values().length; i++) {
            ShoutHandler.Shout shout = ShoutHandler.Shout.fromOrdinal(i);
            // if (shout == ShoutHandler.Shout.NONE) continue;
            if (!data.hasUnlockedShout(i)) continue;

            WColorButton shoutSelector = createShoutSelectionButton(data, shout);

            WShoutUnlockButton unlocker = createShoutUnlockerButton(data, unlockBox, shoutSelector, soulsCount, shout);

            shoutsBox.add(shoutSelector, 162, 18);
            unlockBox.add(unlocker);
        }

        if (shoutsBox.streamChildren().findAny().isEmpty())
            masterBox.add(new WLabel(Text.literal("lol u have no shouts")));

        masterBox.setSpacing(0);
        masterBox.add(shoutsBox);
        masterBox.add(unlockBox);

        WScrollPanel scrollPanel = new WScrollPanel(masterBox);
        scrollPanel.setScrollingHorizontally(TriState.FALSE);

        root.add(soulsCount, 0, 0);
        root.add(scrollPanel, 0, 1, 10, 10);

        root.validate(this);
    }

    private void sendShoutPacket(Identifier channel, int shoutOrdinal) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(shoutOrdinal);
        ClientPlayNetworking.send(channel, buf);
    }

    private void sendSoulsPacket(int souls) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(souls);
        ClientPlayNetworking.send(ModPackets.SOUL_COUNT_ID, buf);
    }

    private WColorButton createShoutSelectionButton(IShout data, ShoutHandler.Shout shout) {
        int ordinal = shout.ordinal();
        WColorButton shoutSelector = new WColorButton(shout.getName()) {
            @Override
            public void addTooltip(TooltipBuilder builder) {
                builder.add(Text.translatable("anxshouts.shouts." + shout.getId() + ".description"));
            }
        };
        shoutSelector.setEnabled(data.hasObtainedShout(ordinal));
        if (data.getSelectedShout() == ordinal) {
            shoutSelector.setColor(0x4d_FFA500);
            selectedShoutBuffer = shoutSelector;
        }
        shoutSelector.setOnClick(() -> {
            data.setSelectedShout(ordinal);
            sendShoutPacket(ModPackets.SELECT_SHOUT_ID, ordinal);
            shoutSelector.setColor(0x4dFFA500);

            if (selectedShoutBuffer == null)
                selectedShoutBuffer = shoutSelector;
            else if (selectedShoutBuffer.equals(shoutSelector)) {
                data.setSelectedShout(0);
                sendShoutPacket(ModPackets.SELECT_SHOUT_ID, 0);

                selectedShoutBuffer = null;
                shoutSelector.setColor(0xFF_FFFFFF);
            }
            else {
                selectedShoutBuffer.setColor(0xFF_FFFFFF);
                selectedShoutBuffer.tick();
                selectedShoutBuffer = shoutSelector;
            }
            shoutSelector.tick();
        });

        return shoutSelector;
    }

    private WShoutUnlockButton createShoutUnlockerButton(IShout data, WBox unlockBox, WColorButton shoutSelector, WLabel soulsCount, ShoutHandler.Shout shout) {
        int ordinal = shout.ordinal();
        WShoutUnlockButton unlocker = new WShoutUnlockButton(ordinal, new TextureIcon(new Identifier(MODID, "textures/gui/locked_widget.png"))) {
            @Override
            public void addTooltip(TooltipBuilder builder) {
                builder.add(Text.literal("Costs " + shout.getCost() + " dragon souls"));
            }
        };
        unlocker.setEnabled(!data.hasObtainedShout(ordinal) && data.getSoulCount() >= shout.getCost());
        unlocker.setOnClick(() -> {
            data.obtainShout(ordinal);
            sendSoulsPacket(data.getSoulCount() - shout.getCost());
            sendShoutPacket(ModPackets.OBTAIN_SHOUT_ID, ordinal);
            soulsCount.setText(Text.literal("Souls: " + (data.getSoulCount() - shout.getCost())));
            unlocker.setEnabled(false);
            shoutSelector.setEnabled(true);
            shoutSelector.tick();
            soulsCount.tick();
            tickAllUnlockers(data, unlockBox, shout.getCost());
        });

        return unlocker;
    }

    private void tickAllUnlockers(IShout data, WBox unlockBox, int cost) {
        unlockBox.streamChildren().forEach(widget -> {
            ((WShoutUnlockButton) widget).setEnabled(data.getSoulCount() - cost >= ((WShoutUnlockButton) widget).getShout().getCost());
            widget.tick();
            widget.tick(); // need to tick twice for whatever reason so it doesnt get fucked up while the gui is still open
        });
    }

    static class WColorButton extends WButton {
        private static final Identifier DARK_WIDGETS_LOCATION = new Identifier("libgui", "textures/widget/dark_widgets.png");
        private static final int BUTTON_HEIGHT = 20;
        private static final int ICON_SPACING = 2;
        protected int color = 0xFF_FFFFFF; // first 2 is alpha value

        public WColorButton(@Nullable Text label) { super(label); }

        @Environment(EnvType.CLIENT)
        @Override
        public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
            boolean hovered = (mouseX >= 0 && mouseY >= 0 && mouseX < getWidth() && mouseY < getHeight());
            int state = 1; //1=regular. 2=hovered. 0=disabled.
            if (!isEnabled())
                state = 0;
            else if (hovered || isFocused())
                state = 2;

            float px = 1 / 256f;
            float buttonLeft = 0 * px;
            float buttonTop = (46 + (state * 20)) * px;
            int halfWidth = getWidth() / 2;
            if (halfWidth > 198) halfWidth = 198;
            float buttonWidth = halfWidth * px;
            float buttonHeight = 20 * px;

            float buttonEndLeft = (200 - (getWidth() / 2)) * px;

            Identifier texture = getTexture(this);
            ScreenDrawing.texturedRect(context, x, y, getWidth() / 2, 20, texture, buttonLeft, buttonTop, buttonLeft+buttonWidth, buttonTop+buttonHeight, color);
            ScreenDrawing.texturedRect(context, x + (getWidth() / 2), y, getWidth() / 2, 20, texture, buttonEndLeft, buttonTop, 200 * px, buttonTop+buttonHeight, color);

            if (getIcon() != null)
                getIcon().paint(context, x + ICON_SPACING, y + (BUTTON_HEIGHT - iconSize) / 2, iconSize);

            if (getLabel() != null) {
                int color = 0xE0E0E0;
                if (!isEnabled())
                    color = 0xA0A0A0;

                int xOffset = (getIcon() != null && alignment == HorizontalAlignment.LEFT) ? ICON_SPACING + iconSize + ICON_SPACING : 0;
                ScreenDrawing.drawStringWithShadow(context, getLabel().asOrderedText(), alignment, x + xOffset, y + ((20 - 8) / 2), width, color); //LibGuiClient.config.darkMode ? darkmodeColor : color);
            }
        }

        public static Identifier getTexture(WWidget widget) {
            return widget.shouldRenderInDarkMode() ? DARK_WIDGETS_LOCATION : ClickableWidget.WIDGETS_TEXTURE;
        }

        public void setColor(int color) { this.color = color; }
    }

    static class WShoutUnlockButton extends WButton {
        private final int shout;

        public WShoutUnlockButton(int shoutOrdinal, @Nullable Icon icon) {
            super(icon);
            this.shout = shoutOrdinal;
        }

        public ShoutHandler.Shout getShout() { return ShoutHandler.Shout.fromOrdinal(this.shout); }
        // public int getOrdinal() { return this.shout; }
    }
}
