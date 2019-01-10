package com.arman;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UnknownFormatConversionException;

public class ComicBookReader extends JFrame {

    public static final String TITLE = "Comic Book Reader";

    private JPanel comicBookPanel;
    private JMenuBar menuBar;

    public ComicBookReader() {
        super(TITLE);
        this.menuBar = new ReaderMenuBar();
        this.setJMenuBar(menuBar);
    }

    public void display() {
        SwingUtilities.invokeLater(() -> {
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setExtendedState(Frame.MAXIMIZED_BOTH);
            validate();
            setVisible(true);
        });
    }

    private class ReaderMenuBar extends JMenuBar {

        public ReaderMenuBar() {
            JMenu menu = new JMenu("File");
            this.add(menu);
            JMenuItem menuItem = new JMenuItem("Open");
            menuItem.addActionListener(e -> {
                try {
                    openComic();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });
            menu.add(menuItem);
        }

        private void openComic() throws IOException {
            JFileChooser fileChooser = new JFileChooser();
            int answer = fileChooser.showOpenDialog(ComicBookReader.this);
            if (answer == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.exists()) {
                    throw new FileNotFoundException();
                }
                ComicBook comic;
                if (FileTypeDetector.isZip(file)) {
                    comic = ComicBookZip.load(file);
                } else if (FileTypeDetector.isRar(file)) {
                    comic = ComicBookArchive.load(file);
                } else {
                    throw new UnknownFormatConversionException("Can't format file: " + file.getName());
                }
                comicBookPanel = new ComicBookPanel(comic);
                setContentPane(comicBookPanel);
                revalidate();
                repaint();
            }
        }

    }

}
