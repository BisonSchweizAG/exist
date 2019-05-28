package org.exist.util.io;

import java.nio.charset.StandardCharsets;

public final class ByteArrayContent implements ContentFile {
    private static final byte[] EMPTY_BUFFER = new byte[0];

    private byte[] data;

    public static ByteArrayContent of(byte[] data) {
        return new ByteArrayContent(data);
    }

    public static ByteArrayContent of(String data) {
        return new ByteArrayContent(data.getBytes(StandardCharsets.UTF_8));
    }

    private ByteArrayContent(byte[] data) {
        this.data = data;
    }

    @Override
    public void close() {
        data = null;
    }

    @Override
    public byte[] getBytes() {
        return data == null ? EMPTY_BUFFER : data;
    }

    @Override
    public long size() {
        return data == null ? 0 : data.length;
    }
}
