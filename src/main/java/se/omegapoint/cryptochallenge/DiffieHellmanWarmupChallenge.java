package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.AdvancedEncryptionStandard;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.DiffieHellman;

import java.math.BigInteger;

public class DiffieHellmanWarmupChallenge {

    private final ByteBuffer p;
    private final ByteBuffer g;

    private AdvancedEncryptionStandard sessionEncryption;

    public DiffieHellmanWarmupChallenge(final ByteBuffer p, final ByteBuffer g) {
        this.p = p;
        this.g = g;
    }

    public BigInteger handshake(final BigInteger pub) {
        final DiffieHellman diffieHellman = new DiffieHellman(p, g);

        this.sessionEncryption = new AdvancedEncryptionStandard(diffieHellman.handshake(pub));

        return diffieHellman.pub;
    }

    public ByteBuffer message(final ByteBuffer cipherText, final ByteBuffer initializationVector) {
        return sessionEncryption.decrypt(cipherText, initializationVector);
    }
}
