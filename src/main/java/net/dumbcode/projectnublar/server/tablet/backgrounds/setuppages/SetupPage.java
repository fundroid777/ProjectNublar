package net.dumbcode.projectnublar.server.tablet.backgrounds.setuppages;

import net.dumbcode.projectnublar.server.tablet.backgrounds.TabletBackground;

public interface SetupPage<T extends TabletBackground> {
    int getWidth();
    int getHeight();

    void render(int x, int y, int mouseX, int mouseY);

    default void setupFromPage(T page) {
    }

    default void initPage(int x, int y) {
    }

    default void updatePage(int x, int y) {
    }

    default void mouseClicked(int x, int y, int mouseX, int mouseY, int mouseButton) {
    }

    default void mouseClickMove(int x, int y, int mouseX, int mouseY, int mouseButton, long timeSinceLastClick) {
    }

    default void mouseReleased(int x, int y, int mouseX, int mouseY, int mouseButton) {
    }

    default void keyTyped(char typedChar, int keyCode) {
    }

    default void handleMouseInput(int x, int y, int mouseX, int mouseY) {
    }

    T create();
}
