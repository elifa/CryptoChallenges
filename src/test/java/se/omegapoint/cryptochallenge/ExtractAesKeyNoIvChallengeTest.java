package se.omegapoint.cryptochallenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.omegapoint.cryptochallenge.utils.AdvancedEncryptionStandard;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.RandomBuffer;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ExtractAesKeyNoIvChallengeTest {


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"12345678123456781234567812345678123456781234567"},
                {"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"},
                {"aaaaaaaaaaaaaaaaabcd1234abcd1234"},
                {"3k24ibfnweiurfu3b4ibgwhjg 3j4t wbvywibgu"},
        });
    }

    private final ByteBuffer plainText;
    private final ByteBuffer bothIvAndKey;

    public ExtractAesKeyNoIvChallengeTest(final String plainText) {
        this.plainText = new StringBuffer(plainText);
        this.bothIvAndKey = new RandomBuffer(16);
    }

    @Test
    public void shouldBeAbleToDetermineKeyFromCipherText() throws Exception {
        final AdvancedEncryptionStandard.NoIv aes = new AdvancedEncryptionStandard.NoIv(bothIvAndKey);
        final ExtractAesKeyNoIvChallenge challenge = new ExtractAesKeyNoIvChallenge(aes);

        final ByteBuffer cipherText = aes.encrypt(plainText);

        assertEquals(bothIvAndKey, challenge.extractKeyFromCipherText(cipherText));
    }
}