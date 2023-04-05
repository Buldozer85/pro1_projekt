package views;

import javax.swing.*;

public abstract class BaseFrameLayout extends JFrame {

    private int width = 800;
    private int height = 600;
    public BaseFrameLayout(String title) {
        super(title);
        this.setSize(this.width, this.height);
    }

    @Override
    public int getWidth() {
        return width;
    }

    public BaseFrameLayout setWidth(int width) {
        this.width = width;
        this.setSize(this.width, this.height);
        return this;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public BaseFrameLayout setHeight(int height) {
        this.height = height;
        this.setSize(this.width, this.height);
        return this;
    }
}
