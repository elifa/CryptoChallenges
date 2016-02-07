package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha1Hash;

import java.math.BigInteger;
import java.util.Arrays;

public class DiffieHellman {

    public static final int SECRET_SIZE = 128;

    public final BigInteger p;
    public final BigInteger g;
    private final BigInteger a;
    public final BigInteger pub;

    public DiffieHellman(final ByteBuffer p, final ByteBuffer g) {
        this.p = p.toInt();
        this.g = g.toInt();
        this.a = generateSecret(this.p);
        this.pub = this.g.modPow(this.a, this.p);
    }

    private BigInteger generateSecret(final BigInteger p) {
        final SecureRandomNumberGenerator secureRng = new SecureRandomNumberGenerator();
        return new BigInteger(SECRET_SIZE, secureRng.getSecureRandom()).mod(p);
    }
    
    public ByteBuffer handshake(final BigInteger otherPub) {
        final Sha1Hash hash = new Sha1Hash(otherPub.modPow(this.a, this.p).toByteArray());
        return new ByteBuffer(Arrays.copyOf(hash.getBytes(), 16));
    }
    
}
