package com.github.wizz.pchannel;

import android.util.Log;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.flutter.BuildConfig;
import io.flutter.plugin.common.MessageCodec;

public class StandardMessageCodec1 implements MessageCodec<Object> {
    private static final String TAG = "StandardMessageCodec#";
    public static final StandardMessageCodec1 INSTANCE = new StandardMessageCodec1();

    @Override
    public ByteBuffer encodeMessage(Object message) {
        if (message == null) {
            return null;
        }
        ExposedByteArrayOutputStream byteAOS = new ExposedByteArrayOutputStream();
        CodedOutputStream coded = CodedOutputStream.newInstance(byteAOS);
        try {
            writeValue(coded, message);
            coded.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ByteBuffer buffer = ByteBuffer.allocateDirect(byteAOS.size());
        buffer.put(byteAOS.buffer(), 0, byteAOS.size());
        return buffer;
    }

    @Override
    public Object decodeMessage(ByteBuffer message) {
        if (message == null) {
            return null;
        }
        CodedInputStream decode = CodedInputStream.newInstance(message);
        message.order(ByteOrder.nativeOrder());
        Object value = null;
        try {
            value = readValue(decode);
            if (!decode.isAtEnd()) {
                throw new IllegalArgumentException("Message corrupted");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static final Charset UTF8 = Charset.forName("UTF8");
    private static final byte NULL = 0;
    private static final byte TRUE = 1;
    private static final byte FALSE = 2;
    private static final byte INT = 3;
    private static final byte LONG = 4;
    private static final byte BIGINT = 5;
    private static final byte DOUBLE = 6;
    private static final byte STRING = 7;
    private static final byte BYTE_ARRAY = 8;
    private static final byte INT_ARRAY = 9;
    private static final byte LONG_ARRAY = 10;
    private static final byte DOUBLE_ARRAY = 11;
    private static final byte LIST = 12;
    private static final byte MAP = 13;

    private static final byte BYTE = 50;
    private static final byte FLOAT = 50;

    /**
     * Writes an int representing a size to the specified stream. Uses an expanding code of 1 to 5
     * bytes to optimize for small values.
     */
    protected static final void writeSize(CodedOutputStream stream, int value) throws IOException {
        if (BuildConfig.DEBUG && 0 > value) {
            Log.e(TAG, "Attempted to write a negative size.");
        }
        if (value < 127) {
            stream.writeRawByte(value);
        } else {
            stream.writeRawByte(127);
            stream.writeInt32NoTag(value);
        }
    }


    /**
     * Writes a type discriminator byte and then a byte serialization of the specified value to the
     * specified stream.
     *
     * <p>Subclasses can extend the codec by overriding this method, calling super for values that the
     * extension does not handle.
     */
    protected void writeValue(CodedOutputStream stream, Object value) throws IOException {
        if (value == null) {
            stream.write(NULL);
        } else if (value == Boolean.TRUE) {
            stream.write(TRUE);
        } else if (value == Boolean.FALSE) {
            stream.write(FALSE);
        } else if (value instanceof Number) {
            if (value instanceof Byte) {
                stream.write(BYTE);
                stream.write(((Byte) value));
            } else if (value instanceof Integer || value instanceof Short) {
                stream.write(INT);
                stream.writeInt32NoTag(((Number) value).intValue());
            } else if (value instanceof Long) {
                stream.write(LONG);
                stream.writeInt64NoTag((long) value);
            } else if (value instanceof Float) {
                stream.write(FLOAT);
                stream.writeFloatNoTag((Float) value);
            } else if (value instanceof Double) {
                stream.write(FLOAT);
                stream.writeDoubleNoTag(((Double) value));
            } else if (value instanceof BigInteger) {
                stream.write(BIGINT);
                stream.writeByteArrayNoTag(((BigInteger) value).toString(16).getBytes(UTF8));
            } else {
                throw new IllegalArgumentException("Unsupported Number type: " + value.getClass());
            }
        } else if (value instanceof String) {
            stream.write(STRING);
            byte[] bytes = ((String) value).getBytes(UTF8);
            stream.writeByteArrayNoTag(bytes);
        } else if (value instanceof byte[]) {
            stream.write(BYTE_ARRAY);
            writeSize(stream, ((byte[]) value).length);
            stream.writeByteArrayNoTag((byte[]) value);
        } else if (value instanceof int[]) {
            stream.write(INT_ARRAY);
            final int[] array = (int[]) value;
            writeSize(stream, array.length);
            for (final int n : array) {
                stream.writeInt32NoTag(n);
            }
        } else if (value instanceof long[]) {
            stream.write(LONG_ARRAY);
            final long[] array = (long[]) value;
            writeSize(stream, array.length);
            for (final long n : array) {
                stream.writeInt64NoTag(n);
            }
        } else if (value instanceof double[]) {
            stream.write(DOUBLE_ARRAY);
            final double[] array = (double[]) value;
            writeSize(stream, array.length);
            for (final double d : array) {
                stream.writeDoubleNoTag(d);
            }
        } else if (value instanceof List) {
            stream.write(LIST);
            final List<?> list = (List) value;
            writeSize(stream, list.size());
            for (final Object o : list) {
                writeValue(stream, o);
            }
        } else if (value instanceof Map) {
            stream.write(MAP);
            final Map<?, ?> map = (Map) value;
            writeSize(stream, map.size());
            for (final Entry<?, ?> entry : map.entrySet()) {
                writeValue(stream, entry.getKey());
                writeValue(stream, entry.getValue());
            }
        } else {
            throw new IllegalArgumentException("Unsupported value: " + value);
        }
    }

    /**
     * Reads an int representing a size as written by writeSize.
     */
    protected static int readSize(CodedInputStream buffer) throws IOException {
        if (buffer.isAtEnd()) {
            throw new IllegalArgumentException("Message corrupted");
        }
        final int value = buffer.readRawByte();
        if (value < 127) {
            return value;
        } else {
            return buffer.readInt32();
        }
    }

    /**
     * Reads a byte array as written by writeBytes.
     */
    protected static byte[] readBytes(CodedInputStream buffer) throws IOException {
        final int length = readSize(buffer);
        return buffer.readRawBytes(length);
    }

    /**
     * Reads a value as written by writeValue.
     */
    protected final Object readValue(CodedInputStream buffer) throws IOException {
        if (buffer.isAtEnd()) {
            throw new IllegalArgumentException("Message corrupted");
        }
        final byte type = buffer.readRawByte();
        return readValueOfType(type, buffer);
    }

    /**
     * Reads a value of the specified type.
     *
     * <p>Subclasses may extend the codec by overriding this method, calling super for types that the
     * extension does not handle.
     */
    protected Object readValueOfType(byte type, CodedInputStream buffer) throws IOException {
        final Object result;
        switch (type) {
            case NULL:
                result = null;
                break;
            case TRUE:
                result = true;
                break;
            case FALSE:
                result = false;
                break;
            case INT:
                result = buffer.readInt32();
                break;
            case LONG:
                result = buffer.readInt64();
                break;
            case BIGINT: {
                final byte[] hex = readBytes(buffer);
                result = new BigInteger(new String(hex, UTF8), 16);
                break;
            }
            case DOUBLE:
                result = buffer.readDouble();
                break;
            case STRING: {
                final byte[] bytes = readBytes(buffer);
                result = new String(bytes, UTF8);
                buffer.readString();
                break;
            }
            case BYTE_ARRAY: {
                result = readBytes(buffer);
                break;
            }
            case INT_ARRAY: {
                final int length = readSize(buffer);
                final int[] array = new int[length];
                for (int i = 0; i < length; i++) {
                    array[i] = buffer.readInt32();
                }
                result = array;
                break;
            }
            case LONG_ARRAY: {
                final int length = readSize(buffer);
                final long[] array = new long[length];
                for (int i = 0; i < length; i++) {
                    array[i] = buffer.readInt64();
                }
                result = array;
                break;
            }
            case DOUBLE_ARRAY: {
                final int length = readSize(buffer);
                final double[] array = new double[length];
                for (int i = 0; i < length; i++) {
                    array[i] = buffer.readDouble();
                }
                result = array;
                break;
            }
            case LIST: {
                final int size = readSize(buffer);
                final List<Object> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    list.add(readValue(buffer));
                }
                result = list;
                break;
            }
            case MAP: {
                final int size = readSize(buffer);
                final Map<Object, Object> map = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    map.put(readValue(buffer), readValue(buffer));
                }
                result = map;
                break;
            }
            default:
                throw new IllegalArgumentException("Message corrupted");
        }
        return result;
    }

    static final class ExposedByteArrayOutputStream extends ByteArrayOutputStream {
        byte[] buffer() {
            return buf;
        }
    }
}

