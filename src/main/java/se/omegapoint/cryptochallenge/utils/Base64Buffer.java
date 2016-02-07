package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.codec.Base64;

public class Base64Buffer extends ByteBuffer {

    public Base64Buffer(final String string) {
        super(Base64.decode(string));
    }
}
