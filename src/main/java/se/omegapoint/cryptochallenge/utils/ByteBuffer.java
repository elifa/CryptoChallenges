package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteBuffer {

    final byte[] bytes;

    public ByteBuffer() {
        this.bytes = new byte[]{};
    }

    public ByteBuffer(final byte singleByte) {
        this.bytes = new byte[]{singleByte};
    }

    public ByteBuffer(final byte[] byteArray) {
        this.bytes = byteArray;
    }

    public ByteBuffer xor(final ByteBuffer otherBuffer) {
        final ByteBuffer shortest = length() > otherBuffer.length() ? otherBuffer : this;
        final ByteBuffer longest = length() > otherBuffer.length() ? this : otherBuffer;

        final byte[] result = new byte[longest.length()];

        for (int i = 0; i < longest.length(); i++) {
            result[i] = (byte) (longest.bytes[i] ^ shortest.bytes[i % shortest.length()]);
        }

        return new ByteBuffer(result);
    }

    public List<ByteBuffer> split(final int numberOfChunks) {
        return chunk(calculateChunkLength(numberOfChunks));
    }

    public List<ByteBuffer> chunk(final int chuckSize) {
        final ArrayList<ByteBuffer> buffers = new ArrayList<>();

        for (int i = 0; i < bytes.length; i += chuckSize) {
            buffers.add(new ByteBuffer(Arrays.copyOfRange(bytes, i, i + chuckSize)));
        }

        return buffers;
    }

    public ByteBuffer transpose(final int numberOfChunks) {
        final int chunkLength = calculateChunkLength(numberOfChunks);
        final int chunkPadding = length() % numberOfChunks == 0 ? 0 : numberOfChunks - length() % numberOfChunks;

        final byte[] result = new byte[length() + chunkPadding];

        for (int i = 0; i < length(); i++) {
            final byte value = bytes[i];
            final int line = i / numberOfChunks;
            final int row = i % numberOfChunks;

            result[row * chunkLength + line] = value;
        }

        return new ByteBuffer(result);
    }

    public ByteBuffer concat(final ByteBuffer otherBuffer) {
        final byte[] mergedBytes = new byte[length() + otherBuffer.length()];

        System.arraycopy(bytes, 0, mergedBytes, 0, length());
        System.arraycopy(otherBuffer.bytes, 0, mergedBytes, length(), otherBuffer.length());

        return new ByteBuffer(mergedBytes);
    }

    public int length() {
        return bytes.length;
    }

    public String toHex() {
        return Hex.encodeToString(bytes);
    }

    public String toBase64() {
        return Base64.encodeToString(bytes);
    }

    private int calculateChunkLength(final int numberOfChunks) {
        return length() / numberOfChunks + (length() % numberOfChunks == 0 ? 0 : 1);
    }

    @Override
    public boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass().isAssignableFrom(otherObject.getClass())) {
            return false;
        }

        final ByteBuffer that = (ByteBuffer) otherObject;
        return Arrays.equals(bytes, that.bytes);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    @Override
    public String toString() {
        return CodecSupport.toString(bytes);
    }
}
