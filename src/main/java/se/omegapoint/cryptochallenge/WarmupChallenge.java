package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.ByteBuffer;

public class WarmupChallenge {

    private final ByteBuffer keyBuffer;

    public WarmupChallenge(ByteBuffer keyBuffer) {
        this.keyBuffer = keyBuffer;
    }

    public ByteBuffer encrypt(final ByteBuffer plantextBuffer) {
        return plantextBuffer.xor(keyBuffer);
    }

}
