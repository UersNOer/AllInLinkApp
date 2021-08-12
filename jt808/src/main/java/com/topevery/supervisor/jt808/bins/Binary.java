package com.example.android_supervisor.jt808.bins;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * @author wujie
 */
public class Binary {
    private int bufSize;
    private ByteBuffer byteBuf;

    public Binary() {
    }

    public Binary(byte[] bytes) {
        byteBuf = ByteBuffer.wrap(bytes);
    }

    public int getByteSize() {
        return bufSize;
    }

    ByteBuffer getBuffer() {
        if (byteBuf == null) {
            byteBuf = ByteBuffer.allocate(bufSize);
        }
        return byteBuf;
    }

    public class Int8 {
        private int index;

        protected Int8() {
            index = bufSize;
            bufSize += 1;
        }

        public void set(int value) {
            getBuffer().put(index, (byte) (value & 0xff));
        }

        public int get() {
            return getBuffer().get(index);
        }
    }

    public class Int16 {
        private int index;

        protected Int16() {
            this.index = bufSize;
            bufSize += 2;
        }

        public void set(int value) {
            getBuffer().putShort(index, (short) (value & 0xffff));
        }

        public int get() {
            return getBuffer().getShort(index);
        }
    }

    public class Int32 {
        private int index;

        protected Int32() {
            this.index = bufSize;
            bufSize += 4;
        }

        public void set(int value) {
            getBuffer().putInt(index, value);
        }

        public int get() {
            return getBuffer().getInt(index);
        }
    }

    public class Int64 {
        private int index;

        protected Int64() {
            this.index = bufSize;
            bufSize += 8;
        }

        public void set(long value) {
            getBuffer().putLong(index, value);
        }

        public long get() {
            return getBuffer().getLong(index);
        }
    }

    public class VarChar {
        private int index;
        private int size;

        protected VarChar(int size) {
            this.size = size;
            this.index = bufSize;
            bufSize += size;
        }

        public void set(String text, String charset) {
            byte[] bytes;
            try {
                bytes = text.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                bytes = text.getBytes();
            }
            int len = Math.min(size, bytes.length);
            for (int i = 0; i < len; i++) {
                getBuffer().put(index + i, bytes[i]);
            }
        }

        public void set(byte[] bytes) {
            int len = Math.min(size, bytes.length);
            for (int i = 0; i < len; i++) {
                getBuffer().put(index + i, bytes[i]);
            }
        }

        public String get(String charset) {
            int i;
            byte[] bytes = new byte[size];
            for (i = 0; i < size; i++) {
                byte b = getBuffer().get(index + i);
                if (b == 0) {
                    break;
                }
                bytes[i] = b;
            }
            String str;
            try {
                str = new String(bytes, 0, i, charset);
            } catch (UnsupportedEncodingException e) {
                str = new String(bytes, 0, i);
            }
            return str;
        }

        public byte[] get() {
            byte[] bytes = new byte[size];
            for (int i = 0; i < size; i++) {
                byte b = getBuffer().get(index + i);
                if (b == 0) {
                    break;
                }
                bytes[i] = b;
            }
            return bytes;
        }
    }

    public byte[] toByteArray() {
        return getBuffer().array();
    }
}
