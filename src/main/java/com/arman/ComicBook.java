package com.arman;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ComicBook {

    private String name;
    private ComicPage[] pages;

    public ComicBook(String name, ComicPage... pages) {
        this.name = name;
        this.pages = pages;
    }

    public BufferedImage getImage(int pageNumber) {
        return this.getPage(pageNumber).getImage();
    }

    public ComicPage getPage(int pageNumber) {
        return this.pages[pageNumber];
    }

    public Dimension getMaximumPageSize() {
        int width = 0;
        int height = 0;
        for (int i = 0; i < this.pages.length; i++) {
            ComicPage page = this.pages[i];
            if (page != null) {
                width = Math.max(width, page.getWidth());
                height = Math.max(height, page.getHeight());
            }
        }
        return new Dimension(width, height);
    }

    public int getMaximumPageWidth() {
        return (int) getMaximumPageSize().getWidth();
    }

    public int getMaximumPageHeight() {
        return (int) getMaximumPageSize().getHeight();
    }

    public int numPages() {
        return this.pages.length;
    }

}
