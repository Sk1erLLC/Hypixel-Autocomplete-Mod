package club.sk1er.mods.autocomplete;

import club.sk1er.mods.autocomplete.config.FriendsConfig;
import club.sk1er.mods.autocomplete.config.GuildConfig;
import club.sk1er.mods.autocomplete.config.LocalConfig;
import club.sk1er.mods.autocomplete.config.MasterConfig;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.util.EnumChatFormatting;

import java.util.HashMap;
import java.util.function.Consumer;

public class AutocompleteConfigGUi extends GuiScreen {
    private HashMap<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private HashMap<GuiButton, Consumer<GuiButton>> update = new HashMap<>();
    private int id;

    @Override
    public void initGui() {
        super.initGui();
        AutocompleteMod instance = AutocompleteMod.instance;
        MasterConfig masterConfig = instance.getMasterConfig();
        LocalConfig localConfig = masterConfig.getLocalConfig();
        GuildConfig guildConfig = masterConfig.getGuildConfig();
        FriendsConfig friendsConfig = masterConfig.getFriendsConfig();
        this.reg(new GuiButton(++this.id, this.width / 2 - 100, 3, "TOGGLE"), (guiButton) -> {
            StringBuilder append = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("Mod Status: ");
            if (masterConfig.isEnabled()) {
                append.append(EnumChatFormatting.GREEN).append("Enabled");
            } else {
                append.append(EnumChatFormatting.RED).append("Disabled");
            }

            guiButton.displayString = append.toString();
        }, (guiButton) -> masterConfig.setEnabled(!masterConfig.isEnabled()));

        this.reg(new GuiButton(++this.id, this.width / 2 - 100, 3 + 22, "TOGGLE_LOCAL"), (guiButton) -> {
            StringBuilder append = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("Add Local Players: ");
            if (localConfig.enabled) {
                append.append(EnumChatFormatting.GREEN).append("Yes");
            } else {
                append.append(EnumChatFormatting.RED).append("No");
            }
            guiButton.displayString = append.toString();
        }, (guiButton) -> localConfig.enabled = !localConfig.enabled);
        this.reg(new GuiButton(++this.id, this.width / 2 - 100, 3 + 44, "TOGGLE_GUILD"), (guiButton) -> {
            StringBuilder append = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("Add Guild Players: ");
            if (guildConfig.enabled) {
                append.append(EnumChatFormatting.GREEN).append("Yes");
            } else {
                append.append(EnumChatFormatting.RED).append("No");
            }
            guiButton.displayString = append.toString();
        }, (guiButton) -> guildConfig.enabled = !guildConfig.enabled);

        this.reg(new GuiButton(++this.id, this.width / 2 - 100, 3 + 66, "TOGGLE_FRIENDS"), (guiButton) -> {
            StringBuilder append = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("Add Friends: ");
            if (friendsConfig.enabled) {
                append.append(EnumChatFormatting.GREEN).append("Yes");
            } else {
                append.append(EnumChatFormatting.RED).append("No");
            }
            guiButton.displayString = append.toString();
        }, (guiButton) -> friendsConfig.enabled = !friendsConfig.enabled);


        int it = 0;

        for (HypixelRank value : HypixelRank.values()) {
            this.reg(new GuiButton(++this.id, this.width / 2 - (it % 2 == 0 ? 205 : -5), 3 + 88 + it / 2 * 22, ""), (guiButton) -> {
                StringBuilder append = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("Include ").append(value.getDisplayName()).append("s in friends: ");
                if (friendsConfig.get(value)) {
                    append.append(EnumChatFormatting.GREEN).append("Yes");
                } else {
                    append.append(EnumChatFormatting.RED).append("No");
                }
                guiButton.displayString = append.toString();
            }, (guiButton) -> friendsConfig.toggle(value));
            it++;
        }


    }

    protected void actionPerformed(GuiButton button) {
        Consumer<GuiButton> guiButtonConsumer = this.clicks.get(button);
        if (guiButtonConsumer != null) {
            guiButtonConsumer.accept(button);
        }
    }

    public void updateScreen() {
        super.updateScreen();
        for (GuiButton guiButton : this.update.keySet()) {
            this.update.get(guiButton).accept(guiButton);
        }

    }

    private void regSlider(GuiSlider slider) {
        this.reg(slider, null, null);
    }

    private void reg(GuiButton button, Consumer<GuiButton> onUpdate, Consumer<GuiButton> onClick) {
        this.buttonList.removeIf((button1) -> button1.id == button.id);
        this.buttonList.add(button);
        this.clicks.keySet().removeIf((button1) -> button1.id == button.id);
        if (onClick != null) {
            this.clicks.put(button, onClick);
        }

        this.update.keySet().removeIf((button1) -> button1.id == button.id);
        if (onUpdate != null) {
            this.update.put(button, onUpdate);
        }

    }
}
