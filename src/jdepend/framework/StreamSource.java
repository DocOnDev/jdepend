package jdepend.framework;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

class StreamSource {
    private final JarFile jarFile;
    private final ZipEntry entry;
    private final File file;

    public StreamSource(File file) {
        this.file = file;
        this.jarFile = null;
        this.entry = null;
    }

    public StreamSource(JarFile jarFile, ZipEntry entry) {
        this.jarFile = jarFile;
        this.entry = entry;
        this.file = null;
    }

    public BufferedInputStream invoke() throws IOException {
        if (isZipSource()) return new BufferedInputStream(jarFile.getInputStream(entry));
        return new BufferedInputStream(new FileInputStream(file));
    }

    private boolean isZipSource() {
        return this.jarFile != null && this.entry != null;
    }
}
