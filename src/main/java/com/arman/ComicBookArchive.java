package com.arman;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.NativeStorage;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UnknownFormatConversionException;

public class ComicBookArchive {

    public static final String CBR = ".cbr";

    private ComicBookArchive() {

    }

    public static ComicBook load(String fileName) throws IOException {
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        if (!ext.equals(CBR)) {
            throw new UnknownFormatConversionException("Can't format file, " + fileName + ", as a " + CBR + " file.");
        }
        File file = new File(fileName);
        return load(file);
    }

    public static ComicBook load(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        List<BufferedImage> images = unrar(file);
        ComicPage[] pages = new ComicPage[images.size()];
        for (int i = 0; i < images.size(); i++) {
            pages[i] = new ComicPage(images.get(i));
        }
        return new ComicBook(file.getName(), pages);
    }

    private static List<BufferedImage> unrar(File file) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        Archive archive = null;
        try {
            archive = new Archive(new NativeStorage(file));
            List<FileHeader> headers = archive.getFileHeaders();
            for (int i = 0; i < headers.size(); i++) {
                FileHeader header = headers.get(i);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    archive.extractFile(header, baos);
                } finally {
                    baos.close();
                }
                byte[] fileBytes = baos.toByteArray();
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileBytes));
                images.add(image);
            }
            return images;
        } catch (RarException | IOException e) {
            throw new IOException();
        } finally {
            try {
                if (archive != null) {
                    archive.close();
                }
            } catch (IOException e) {

            }
        }
    }

}
