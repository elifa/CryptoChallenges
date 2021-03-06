package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.ByteBuffer;

public class XorWarmupChallenge {

    private final ByteBuffer keyBuffer;

    public XorWarmupChallenge(ByteBuffer keyBuffer) {
        this.keyBuffer = keyBuffer;
    }

    public ByteBuffer encrypt(final ByteBuffer plantextBuffer) {
        return plantextBuffer.xor(keyBuffer);
    }

}
