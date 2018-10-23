package com.wuyou.user.network.ipfs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by DELL on 2018/10/23.
 */

public interface ChainNamedStreamable {
    InputStream getInputStream() throws IOException;

    String getName();

    List<ChainNamedStreamable> getChildren();

    boolean isDirectory();

    default byte[] getContents() throws IOException {
        InputStream in = this.getInputStream();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] tmp = new byte[4096];

        int r;
        while ((r = in.read(tmp)) >= 0) {
            bout.write(tmp, 0, r);
        }

        return bout.toByteArray();
    }

    public static class DirWrapper implements ChainNamedStreamable {
        private final String name;
        private final List<ChainNamedStreamable> children;

        public DirWrapper(String name, List<ChainNamedStreamable> children) {
            this.name = name;
            this.children = children;
        }

        public InputStream getInputStream() throws IOException {
            throw new IllegalStateException("Cannot get an input stream for a directory!");
        }

        public String getName() {
            return this.name;
        }

        public List<ChainNamedStreamable> getChildren() {
            return this.children;
        }

        public boolean isDirectory() {
            return true;
        }
    }

    public static class ByteArrayWrapper implements ChainNamedStreamable {
        private final String name;
        private final byte[] data;

        public ByteArrayWrapper(byte[] data) {
            this("", data);
        }

        public ByteArrayWrapper(String name, byte[] data) {
            this.name = name;
            this.data = data;
        }

        public boolean isDirectory() {
            return false;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.data);
        }

        public List<ChainNamedStreamable> getChildren() {
            return Collections.emptyList();
        }

        public String getName() {
            return this.name;
        }
    }

    public static class FileWrapper implements ChainNamedStreamable {
        private final File source;

        public FileWrapper(File source) {
            if (!source.exists()) {
                throw new IllegalStateException("File does not exist: " + source);
            } else {
                this.source = source;
            }
        }

        public InputStream getInputStream() throws IOException {
            return new FileInputStream(this.source);
        }

        public boolean isDirectory() {
            return this.source.isDirectory();
        }

        public List<ChainNamedStreamable> getChildren() {
            List<ChainNamedStreamable> list = new ArrayList<>();
            for (File file : this.source.listFiles()) {
                ChainNamedStreamable fileWrapper = new FileWrapper(file);
                list.add(fileWrapper);
            }
            return list;
//            return this.isDirectory()?(List) Stream.of(this.source.listFiles()).map(ChainNamedStreamable.FileWrapper::<init>).collect(Collectors.toList()): Collections.emptyList();
        }

        public String getName() {
            try {
                return URLEncoder.encode(this.source.getName(), "UTF-8");
            } catch (UnsupportedEncodingException var2) {
                throw new RuntimeException(var2);
            }
        }
    }
}
