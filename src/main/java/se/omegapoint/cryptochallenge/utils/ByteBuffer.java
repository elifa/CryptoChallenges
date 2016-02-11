package se.omegapoint.cryptochallenge.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteBuffer {

    final byte[] bytes;

    public ByteBuffer() {
        this.bytes = new byte[]{};
    }

    public ByteBuffer(final int singleInt) {
        this((byte) singleInt);
    }

    public ByteBuffer(final byte singleByte) {
        this.bytes = new byte[]{singleByte};
    }

    public ByteBuffer(final int[] intArray) {
        this.bytes = toByteArray(intArray);
    }

    public ByteBuffer(final byte[] byteArray) {
        this.bytes = byteArray;
    }

    public ByteBuffer pad(final int toLength, final ByteBuffer with) {
        byte[] result = Arrays.copyOf(bytes, toLength);
        Arrays.fill(result, length(), result.length, with.bytes[0]);
        return new ByteBuffer(result);
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

    public ByteBuffer chunk(final int index, final int chuckSize) {
        return new ByteBuffer(Arrays.copyOfRange(bytes, index * chuckSize, index * chuckSize + chuckSize));
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

    public ByteBuffer subBuffer(final int start) {
        return subBuffer(start, length() - start);
    }

    public ByteBuffer subBuffer(final int start, final int length) {
        return new ByteBuffer(Arrays.copyOfRange(bytes, start, start + length));
    }

    public int length() {
        return bytes.length;
    }

    public int noOfChunks(final int chunkSize) {
        int result = length() / chunkSize;
        result += length() % chunkSize == 0 ? 0 : 1;
        return result;
    }

    public String toHex() {
        return Hex.encodeToString(bytes);
    }

    public String toBase64() {
        return Base64.encodeToString(bytes);
    }

    public BigInteger toInt() {
        byte[] rev = ArrayUtils.addAll(bytes, (byte) 0x00, (byte) 0x00);

        ArrayUtils.reverse(rev);

        return new BigInteger(rev);
    }

    public int[] toInts() {
        final int length = length() / 4 + (length() % 4 == 0 ? 0 : 1);
        final int[] result = new int[length];

        for (int i = 0, j = 0; i < length(); i++, j = i / 4) {
            result[j] = result[j] << 8;
            result[j] += bytes[i] & 0xff;
        }

        return result;
    }

    private int calculateChunkLength(final int numberOfChunks) {
        return length() / numberOfChunks + (length() % numberOfChunks == 0 ? 0 : 1);
    }

    private byte[] toByteArray(final int[] wordBuffer) {
        final byte[] result = new byte[wordBuffer.length * 4];

        for (int i = 0; i < wordBuffer.length; i++) {
            int word = wordBuffer[i];
            final byte[] bytes = new byte[4];

            for (int j = 0; word != 0; j++, word >>>= 8) {
                bytes[4 - j - 1] = (byte) (word & 0xFF);
            }

            System.arraycopy(bytes, 0, result, i * 4, 4);
        }

        return result;
    }

    @Override
    public boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || !otherObject.getClass().isAssignableFrom(getClass())) {
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
