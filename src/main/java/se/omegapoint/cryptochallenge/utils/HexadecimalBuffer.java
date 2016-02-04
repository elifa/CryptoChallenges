package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HexadecimalBuffer {

    final byte[] bytes;

    public HexadecimalBuffer(final byte singleByte) {
        this.bytes = new byte[]{singleByte};
    }

    public HexadecimalBuffer(final byte[] byteArray) {
        this.bytes = byteArray;
    }

    public HexadecimalBuffer(final String hexadecimalString) {
        this.bytes = Hex.decode(hexadecimalString);
    }

    public HexadecimalBuffer xor(final HexadecimalBuffer otherBuffer) {
        final HexadecimalBuffer shortest = length() > otherBuffer.length() ? otherBuffer : this;
        final HexadecimalBuffer longest = length() > otherBuffer.length() ? this : otherBuffer;

        final byte[] result = new byte[longest.length()];

        for (int i = 0; i < longest.length(); i++) {
            result[i] = (byte) (longest.bytes[i] ^ shortest.bytes[i % shortest.length()]);
        }

        return new HexadecimalBuffer(result);
    }

    public List<HexadecimalBuffer> chunk(final int chuckSize) {
        final ArrayList<HexadecimalBuffer> buffers = new ArrayList<>();

        for (int i = 0; i < bytes.length; i += chuckSize) {
            buffers.add(new HexadecimalBuffer(Arrays.copyOfRange(bytes, i, i + chuckSize)));
        }
        System.out.println(buffers.size());
        return buffers;
    }

    public HexadecimalBuffer transpose(final int chuckSize) {
        /*final int transposedChunkSize = (int) Math.ceil(length() / chuckSize);
        final int resultLength = length();
        final byte[] resultArray = new byte[resultLength];

        for (int i = 0; i < length(); i++) {
            final int row = Math.floorDiv(i, transposedChunkSize);
            final int col = i % transposedChunkSize;

            resultArray[col * chuckSize + row] = bytes[i];
        }*/

        /*final int newChunkSize = (int) Math.ceil(length() / chuckSize);
        final int padding = length() % chuckSize == 0 ? 0 : chuckSize - length() % chuckSize;

        byte[] result = new byte[length() + padding];

        for (int i = 0; i < length(); i++) {
            byte value = bytes[i];
            int line = i / chuckSize;
            int row = i % chuckSize;

            int newPosition = row * newChunkSize + line;
            result[newPosition] = value;
        }*/

        return new HexadecimalBuffer(transpose(bytes, chuckSize));
    }

    public int transposedBlockLength(byte[] original, int blockLength) {
        return original.length / blockLength + (original.length % blockLength == 0 ? 0 : 1);
    }

    public byte[] transpose(byte[] original, int blockLength) {
        int newBlockLength = transposedBlockLength(original, blockLength);

        int padding = original.length % blockLength == 0 ? 0 : blockLength - original.length % blockLength;
        byte[] result = new byte[original.length + padding];

        for (int i = 0; i < original.length; i++) {
            byte value = original[i];
            int line = i / blockLength;
            int row = i % blockLength;

            int newPosition = row * newBlockLength + line;
            result[newPosition] = value;
        }
        return result;
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

    public static HexadecimalBuffer concat(final List<HexadecimalBuffer> buffers) {
        if (buffers.size() <= 0) {
            throw new IllegalArgumentException();
        } else if (buffers.size() == 1) {
            return buffers.get(0);
        }

        final HexadecimalBuffer first = buffers.get(0);
        final HexadecimalBuffer second = concat(buffers.subList(1, buffers.size()));

        final byte[] mergedBytes = new byte[first.length() + second.length()];

        System.arraycopy(first.bytes, 0, mergedBytes, 0, first.length());
        System.arraycopy(second.bytes, 0, mergedBytes, first.length(), second.length());
        System.out.println(mergedBytes.length);
        return new HexadecimalBuffer(mergedBytes);
    }

    @Override
    public boolean equals(final Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || getClass().isAssignableFrom(otherObject.getClass())) {
            return false;
        }

        final HexadecimalBuffer that = (HexadecimalBuffer) otherObject;
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
