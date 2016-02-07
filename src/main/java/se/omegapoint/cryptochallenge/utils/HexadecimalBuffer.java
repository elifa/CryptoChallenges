package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.codec.Hex;

public class HexadecimalBuffer extends ByteBuffer {

    public HexadecimalBuffer(final String hexadecimalString) {
        super(Hex.decode(hexadecimalString));
    }
}
