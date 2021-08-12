package com.example.android_supervisor.jt808.utils;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;

import io.netty.buffer.ByteBuf;

/**
 * @author wujie
 */
public class JT808Utils {

    public static byte[] pkgMsg(byte[] header, byte[] body) {
        byte[] bytes = concat(header, body);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put((byte) 0x7e);
        buffer.put(encodeBuf(bytes));
        buffer.put((byte) calcVCode(bytes));
        buffer.put((byte) 0x7e);
        buffer.flip();

        bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }

    public static byte[] parseMsg(ByteBuf buffer) throws ParseException {
        int byteSize = buffer.readableBytes();
        byte[] bytes = new byte[byteSize - 1];

        buffer.readBytes(bytes);
        bytes = decodeBuf(bytes);

        byte vcode = buffer.readByte();
        if (vcode != (byte) calcVCode(bytes)) {
            throw new ParseException("Invalid check code.", buffer.readerIndex());
        }

        return bytes;
    }

    private static byte[] concat(byte[] header, byte[] body) {
        int length = header.length + body.length;
        byte[] bytes = new byte[length];
        System.arraycopy(header, 0, bytes, 0, header.length);
        System.arraycopy(body, 0, bytes, header.length, body.length);
        return bytes;
    }

    private static int calcVCode(byte[] bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result ^= bytes[i];
        }
        return result;
    }

    public static byte[] decodeBuf(byte[] source) {
        ByteArrayOutputStream stream;
        try {
            stream = new ByteArrayOutputStream();
            for (int i = 0; i < source.length; i++) {
//                if (source[i] == 0x7d && source[i + 1] == 0x01) {
//                    stream.write(0x7d);
//                    i++;
//                } else
                if (source[i] == 0x7d && source[i + 1] == 0x02) {
                    stream.write(0x7e);
                    i++;
                } else {
                    stream.write(source[i]);
                }
            }
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }

    public static byte[] encodeBuf(byte[] source) {
        ByteArrayOutputStream stream;
        try {
            stream = new ByteArrayOutputStream();
            for (int i = 0; i < source.length; i++) {
                if (source[i] == 0x7e) {
                    stream.write(0x7d);
                    stream.write(0x02);
                } else {
                    stream.write(source[i]);
                }
            }
            return stream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return source;
    }
}
