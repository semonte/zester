package org.zalando.zester.file;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class FileProvider {

    @NotNull
    public File[] listFiles(String path) {
        final File[] files = new File(path).listFiles();
        if (files == null) {
            return new File[0];
        }

        return files;
    }

    public boolean fileExists(String path) {
        return new File(path).exists();
    }

}
