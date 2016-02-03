package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.CodecSupport;
import org.apache.shiro.codec.Hex;

import java.util.Arrays;

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

    public int length() {
        return bytes.length;
    }

    public String toHex() {
        return Hex.encodeToString(bytes);
    }

    public String toBase64() {
        return Base64.encodeToString(bytes);
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
