package net.typho.config.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.typho.config.ConfigOptionGroup;
import net.typho.config.ConfigOptionGroupChild;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class ConfigScreen extends Screen {
    private final List<ConfigOptionGroup> history = new LinkedList<>();
    private ConfigOptionGroup tab;
    private final Screen parent;

    public ConfigScreen(Text title, ConfigOptionGroup tab, @Nullable Screen parent) {
        super(title);
        this.tab = tab;
        this.parent = parent;
    }

    public ConfigScreen(ConfigOptionGroup tab, @Nullable Screen parent) {
        this(Text.translatable("menu.typho_config"), tab, parent);
    }

    @Override
    public <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
        return super.addDrawableChild(drawableElement);
    }

    @Override
    protected void init() {
        int width = Math.min(256, this.width / 2);
        int height = 24;
        int centerX = this.width / 2;
        int y = Math.max(this.height / 2 - 256, height);

        boolean side = false;

        for (ConfigOptionGroupChild child : tab) {
            addDrawableChild(new Node(side ? centerX : centerX - width, y, width, height, child));

            if (side) {
                y += height;
            }

            side = !side;
        }
    }

    public ConfigOptionGroup getTab() {
        return tab;
    }

    public void pushTab(ConfigOptionGroup tab) {
        if (this.tab != null) {
            history.add(this.tab);
        }

        setTab(tab);
    }

    public boolean popTab() {
        if (!history.isEmpty()) {
            setTab(history.removeLast());
            return true;
        }

        return false;
    }

    public void setTab(ConfigOptionGroup tab) {
        this.tab = tab;

        clearAndInit();
    }

    @Override
    public void close() {
        if (!popTab()) {
            if (parent == null) {
                super.close();
            } else {
                MinecraftClient.getInstance().setScreen(parent);
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public class Node extends ClickableWidget {
        public final ConfigOptionGroupChild child;

        public Node(int x, int y, int width, int height, ConfigOptionGroupChild child) {
            super(x, y, width, height, null);
            this.child = child;
            child.init(this);
        }

        public ConfigScreen parent() {
            return ConfigScreen.this;
        }

        public TextRenderer textRenderer() {
            return textRenderer;
        }

        @Override
        public void onClick(double mouseX, double mouseY) {
            if (child.enabled()) {
                child.click(this, mouseX, mouseY);
            }
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
            return child.enabled() && child.scroll(this, mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            context.fill(getX(), getY(), getX() + getWidth(), getY() + getHeight(), child.enabled() ? (hovered ? 0xFF000000 : 0xCC000000) : (hovered ? 0xFF7F0000 : 0xCC7F0000));

            float scale = 1;

            if (hovered) {
                scale *= 1.5f;
            }

            MatrixStack matrices = context.getMatrices();
            matrices.push();
            matrices.scale(scale, scale, scale);
            matrices.translate(-8 + (getX() + 8 + (height - 16) / 2f) / scale, -8 + (getY() + 8 + (height - 16) / 2f) / scale, 0);
            context.drawItem(child.icon(), 0, 0);
            matrices.pop();

            context.drawTextWithShadow(textRenderer, child.name(), getX() + 32, getY() + (height - textRenderer.fontHeight) / 2, 0xFFFFFFFF);
            child.render(this, context, mouseX, mouseY, delta);
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {
        }
    }
}
