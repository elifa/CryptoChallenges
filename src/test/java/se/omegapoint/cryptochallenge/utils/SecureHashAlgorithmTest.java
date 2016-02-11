package se.omegapoint.cryptochallenge.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

@RunWith(Parameterized.class)
public class SecureHashAlgorithmTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"da39a3ee5e6b4b0d3255bfef95601890afd80709", ""},
                {"a9993e364706816aba3e25717850c26c9cd0d89d", "abc"},
                {"84983e441c3bd26ebaae4aa1f95129e5e54670f1", "abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"},
                {"a49b2446a02c645bf419f995b67091253a04a259", "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"}
        });
    }

    private final ByteBuffer hashValue;
    private final ByteBuffer plainText;

    public SecureHashAlgorithmTest(final String hashValue, final String plainText) {
        this.hashValue = new HexadecimalBuffer(hashValue);
        this.plainText = new StringBuffer(plainText);
    }

    @Test
    public void testSha() throws Exception {
        assertEquals(hashValue, new SecureHashAlgorithm(plainText).encode());
    }

}