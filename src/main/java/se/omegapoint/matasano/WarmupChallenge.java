package se.omegapoint.matasano;

import se.omegapoint.matasano.utils.HexadecimalBuffer;

public class WarmupChallenge {

    private final HexadecimalBuffer keyBuffer;

    public WarmupChallenge(HexadecimalBuffer keyBuffer) {
        this.keyBuffer = keyBuffer;
    }

    public HexadecimalBuffer encrypt(final HexadecimalBuffer plantextBuffer) {
        return plantextBuffer.xor(keyBuffer);
    }

}
