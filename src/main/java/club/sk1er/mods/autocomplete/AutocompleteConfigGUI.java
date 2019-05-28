package club.sk1er.mods.autocomplete;

import club.sk1er.mods.autocomplete.config.MasterConfig;
import club.sk1er.mods.autocomplete.sources.AutocompleteSources;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlider;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.config.GuiCheckBox;
import org.lwjgl.input.Mouse;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Consumer;

public class AutocompleteConfigGUI extends GuiScreen {
    public int offset = 0;
    private HashMap<GuiButton, Consumer<GuiButton>> clicks = new HashMap<>();
    private HashMap<GuiButton, Consumer<GuiButton>> update = new HashMap<>();
    private int id;
    private GuiTextField textField;

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        AutocompleteMod instance = AutocompleteMod.instance;
        MasterConfig masterConfig = instance.getMasterConfig();
        this.reg(new GuiButton(++this.id, 2, 2 + offset, 100, 20, "TOGGLE"), (guiButton) -> {
            StringBuilder append = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("Mod Status: ");
            if (masterConfig.isEnabled()) {
                append.append(EnumChatFormatting.GREEN).append("Enabled");
            } else {
                append.append(EnumChatFormatting.RED).append("Disabled");
            }
            guiButton.displayString = append.toString();
        }, (guiButton) -> masterConfig.setEnabled(!masterConfig.isEnabled()));

        AutocompleteSources[] values = AutocompleteSources.values();
        this.reg(new GuiButton(++this.id, 104, 2 + offset, 100, 20, "REFRESH"), (guiButton) -> {
            guiButton.displayString = EnumChatFormatting.GREEN + "Refresh Data";
        }, (guiButton) -> {
            Multithreading.runAsync(() -> {
                        for (AutocompleteSources value : values) {
                            value.refresh();
                        }
                    }
            );

        });
        int collumns = values.length + 2 + 1;
        int tempY = 35 + offset;
        HashMap<String, Integer> commands = masterConfig.getCommands();
        System.out.println("s");

        buttonList.add(new GuiButton(++this.id, width / collumns, tempY, 100, 20, "Command"));
        for (int i = 0; i < values.length; i++) {
            buttonList.add(new GuiButton(++this.id, width / collumns * (i + 2), tempY, 50, 20, values[i].name()));
        }
        buttonList.add(new GuiButton(++this.id, width / collumns * (values.length + 2), tempY, 50, 20, "Delete"));

        tempY += 22;
        Set<String> strings = commands.keySet();
        ArrayList<String> list = new ArrayList<>(strings);
        Collections.sort(list);
        for (String s : list) {
            buttonList.add(new GuiButton(++this.id, width / collumns, tempY, 100, 20, s));
            for (int i = 0; i < values.length; i++) {
                int finalI = i;
                reg(new GuiCheckBox(++this.id, width / collumns * (i + 2) + 20, tempY + 2, "", ((commands.get(s) >> i) & 1) == 1), guiButton -> {

                }, guiButton -> {
                    boolean checked = ((GuiCheckBox) guiButton).isChecked();
                    int tmp = commands.get(s);
                    if (checked) {
                        tmp |= 1 << finalI;
                    } else {
                        tmp &= ~(1 << finalI);
                    }
                    commands.put(s, tmp);
                });
            }
            reg(new GuiCheckBox(++this.id, width / collumns * (values.length + 2), tempY, "Delete", false), guiButton -> {

            }, guiButton -> {
                commands.remove(s);
                instance.delete(s);
                initGui();

            });
            tempY += 22;
        }
        String init = "";
        if (this.textField != null)
            init = textField.getText();
        textField = new GuiTextField(++id, fontRendererObj, width / collumns, tempY, 100, 20);
        textField.setText(init);
        reg(new GuiButton(++id, width / collumns, tempY + 22, 100, 20, "Add Command"), guiButton -> {

        }, guiButton -> {
            commands.put(textField.getText(), 0b111);
            textField.setText("");
            initGui();
            instance.ensure(textField.getText(), AutocompleteSources.values());
        });
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0, 0, width, height, new Color(0, 0, 0, 100).getRGB());
        textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        textField.textboxKeyTyped(typedChar, keyCode);
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

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        int scollMultiplier = -1;
        if (i < 0) {
            offset += 11 * scollMultiplier;
        } else if (i > 0) {
            offset -= 11 * scollMultiplier;
        }
        for (GuiButton guiButton : buttonList) {
            guiButton.yPosition += offset;
        }
        textField.yPosition +=offset;
        offset = 0;
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
