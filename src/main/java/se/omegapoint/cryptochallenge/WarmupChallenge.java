package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.HexadecimalBuffer;

public class WarmupChallenge {

    private final HexadecimalBuffer keyBuffer;

    public WarmupChallenge(HexadecimalBuffer keyBuffer) {
        this.keyBuffer = keyBuffer;
    }

    public HexadecimalBuffer encrypt(final HexadecimalBuffer plantextBuffer) {
        return plantextBuffer.xor(keyBuffer);
    }

}
