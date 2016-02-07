package se.omegapoint.cryptochallenge;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.omegapoint.cryptochallenge.utils.Base64Buffer;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

@RunWith(Parameterized.class)
public class RepeatingKeyXorChallengeTest {

    private static String ENC_TXT_FILE;
    private static String PLAIN_TXT_FILE;

    static {
        try {
            ENC_TXT_FILE = Resources.toString(Resources.getResource("repeatingKeyXor.txt"), Charsets.UTF_8);
            PLAIN_TXT_FILE = Resources.toString(Resources.getResource("repeatingKeyXorPlain.txt"), Charsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ENC_TXT_FILE, PLAIN_TXT_FILE}
        });
    }

    private final ByteBuffer cipherText;
    private final ByteBuffer plainText;

    public RepeatingKeyXorChallengeTest(final String cipherText, final String plainText) {
        this.cipherText = new Base64Buffer(cipherText);
        this.plainText = new StringBuffer(plainText);
    }

    @Test
    public void testDecrypt() throws Exception {
        final RepeatingKeyXorChallenge repeatingKeyXorChallenge = new RepeatingKeyXorChallenge();

        final ByteBuffer decryptedText = repeatingKeyXorChallenge.decrypt(cipherText);

        assertEquals(plainText, decryptedText);
    }
}