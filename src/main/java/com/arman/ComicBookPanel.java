package com.arman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ComicBookPanel extends JPanel {

    private ComicBook comic;
    private int currentLeftPage;

    private JScrollPane scrollPane;
    private ComicBookRenderer renderer;
    private MouseAdapter mouse;

    public ComicBookPanel(ComicBook comic) {
        this.comic = comic;
        this.currentLeftPage = 0;
        this.mouse = new PanelMouse();
        this.setLayout(new BorderLayout());
        this.renderer = new ComicBookRenderer();
        this.scrollPane = new JScrollPane(renderer);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setWheelScrollingEnabled(false);
        this.add(scrollPane, BorderLayout.CENTER);
        renderer.addMouseListener(mouse);
        renderer.addMouseMotionListener(mouse);
        renderer.addMouseWheelListener(mouse);
    }

    private class ComicBookRenderer extends JComponent {

        private Dimension preferredSize;

        private ComicBookRenderer() {
            this.preferredSize = new Dimension(2 * comic.getMaximumPageWidth(), comic.getMaximumPageHeight());
        }

        public Dimension getPreferredSize() {
            return this.preferredSize;
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            BufferedImage pageLeft = comic.getImage(currentLeftPage);
            BufferedImage pageRight = null;
            if (currentLeftPage < comic.numPages() - 1) {
                pageRight = comic.getImage(currentLeftPage + 1);
            }
            int maxPageWidth = comic.getMaximumPageWidth();
            int leftX = this.getWidth() / 2 - maxPageWidth;
            int rightX = leftX + maxPageWidth;
            g.drawImage(pageLeft, leftX, 0, null);
            g.drawImage(pageRight, rightX, 0, null);
        }

    }

    private class PanelMouse extends MouseAdapter {

        private Point mousePos;

        private PanelMouse() {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (SwingUtilities.isMiddleMouseButton(e)) {
                int endX = e.getXOnScreen();
                int endY = e.getYOnScreen();
                int diffX = (int) (mousePos.getX() - endX);
                int diffY = (int) (mousePos.getY() - endY);
                moveBy(new Point(diffX, diffY));
                this.mousePos = new Point(endX, endY);
            }
        }

        private void moveTo(Point p) {
            renderer.scrollRectToVisible(new Rectangle(p, scrollPane.getViewport().getSize()));
        }

        private void moveBy(Point diff) {
            Point p = scrollPane.getViewport().getViewPosition();
            p.translate((int) diff.getX(), (int) diff.getY());
            renderer.scrollRectToVisible(new Rectangle(p, scrollPane.getViewport().getSize()));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                if (inRightSide(e.getPoint())) {
                    int maxLeftPage = comic.numPages() - 2;
                    maxLeftPage = maxLeftPage % 2 == 0 ? maxLeftPage : maxLeftPage + 1;
                    currentLeftPage = Math.min(currentLeftPage + 2, maxLeftPage);
                    renderer.repaint();
                    renderer.invalidate();
                    moveTo(new Point());
                } else {
                    currentLeftPage = Math.max(currentLeftPage - 2, 0);
                    renderer.repaint();
                    renderer.invalidate();
                    moveTo(new Point());
                }
            } else if (SwingUtilities.isMiddleMouseButton(e)) {
                this.mousePos = e.getLocationOnScreen();
            }
        }

    }

    private boolean inLeftSide(Point p) {
        return p.getX() < this.getWidth() / 2;
    }

    private boolean inRightSide(Point p) {
        return p.getX() >= this.getWidth() / 2;
    }

}
