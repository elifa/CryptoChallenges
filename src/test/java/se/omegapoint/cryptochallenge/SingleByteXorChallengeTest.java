package se.omegapoint.cryptochallenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.HexadecimalBuffer;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class SingleByteXorChallengeTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"e8cac2c0c6d7c8cec9d387e4d5ded7d3c887e4cfc6cbcbc2c9c0c2", "Omegapoint Crypto Challenge"},
                {"1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736", "Cooking MC's like a pound of bacon"}
        });
    }

    private final ByteBuffer cipherText;
    private final ByteBuffer plainText;

    public SingleByteXorChallengeTest(final String cipherText, final String plainText) {
        this.cipherText = new HexadecimalBuffer(cipherText);
        this.plainText = new StringBuffer(plainText);
    }

    @Test
    public void testDecrypt() throws Exception {
        System.out.println(new SingleByteXorChallenge().decrypt(cipherText));
        assertEquals(plainText, new SingleByteXorChallenge().decrypt(cipherText));
    }
}