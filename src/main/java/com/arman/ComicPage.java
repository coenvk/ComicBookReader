package com.arman;

import java.awt.image.BufferedImage;

public class ComicPage {

    private BufferedImage image;

    public ComicPage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public int getWidth() {
        if (this.image == null) {
            return 0;
        }
        return this.image.getWidth();
    }

    public int getHeight() {
        if (this.image == null) {
            return 0;
        }
        return this.image.getHeight();
    }

}
