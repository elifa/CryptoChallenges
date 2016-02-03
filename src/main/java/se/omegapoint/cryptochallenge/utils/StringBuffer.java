package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.codec.CodecSupport;

public class StringBuffer extends HexadecimalBuffer {

    public StringBuffer(final String string) {
        super(CodecSupport.toBytes(string));
    }
}
