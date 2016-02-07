package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.codec.CodecSupport;

public class StringBuffer extends ByteBuffer {

    public StringBuffer(final String string) {
        super(CodecSupport.toBytes(string));
    }
}
