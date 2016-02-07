package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;

public class RandomBuffer extends ByteBuffer {

    public RandomBuffer(final int length) {
        super(new SecureRandomNumberGenerator().nextBytes(length).getBytes());
    }
}
