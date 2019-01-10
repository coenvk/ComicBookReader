package com.arman;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UnknownFormatConversionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ComicBookZip {

    public static final String CBZ = ".cbz";

    private ComicBookZip() {

    }

    public static ComicBook load(String fileName) throws IOException {
        String ext = fileName.substring(fileName.lastIndexOf('.'));
        if (!ext.equals(CBZ)) {
            throw new UnknownFormatConversionException("Can't format file, " + fileName + ", as a " + CBZ + " file.");
        }
        File file = new File(fileName);
        return load(file);
    }

    public static ComicBook load(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        List<BufferedImage> images = unzip(file);
        ComicPage[] pages = new ComicPage[images.size()];
        for (int i = 0; i < images.size(); i++) {
            pages[i] = new ComicPage(images.get(i));
        }
        return new ComicBook(file.getName(), pages);
    }

    private static List<BufferedImage> unzip(File file) throws IOException {
        List<BufferedImage> images = new ArrayList<>();
        ZipFile zip = null;
        try {
            zip = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zip.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                InputStream is = zip.getInputStream(entry);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    int read;
                    byte[] data = new byte[16384];
                    while ((read = is.read(data, 0, data.length)) != -1) {
                        baos.write(data, 0, read);
                    }
                } finally {
                    baos.close();
                }
                byte[] fileBytes = baos.toByteArray();
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(fileBytes));
                images.add(image);
            }
            return images;
        } catch (IOException e) {
            throw new IOException();
        } finally {
            try {
                if (zip != null) {
                    zip.close();
                }
            } catch (IOException e) {

            }
        }
    }

}
