package se.omegapoint.cryptochallenge;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.omegapoint.cryptochallenge.utils.Base64Buffer;
import se.omegapoint.cryptochallenge.utils.HexadecimalBuffer;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class RepeatingKeyXorChallengeTest {

    private static String TXT_FILE;

    static {
        try {
            TXT_FILE = Resources.toString(Resources.getResource("repeatingKeyXor.txt"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {TXT_FILE, "Omegapoint Crypto Challenge"}
        });
    }

    private final HexadecimalBuffer cipherText;
    private final HexadecimalBuffer plainText;

    public RepeatingKeyXorChallengeTest(final String cipherText, final String plainText) {
        this.cipherText = new Base64Buffer(cipherText);
        this.plainText = new StringBuffer(plainText);
    }

    @Test
    public void testDecrypt() throws Exception {
        RepeatingKeyXorChallenge repeatingKeyXorChallenge = new RepeatingKeyXorChallenge();
        repeatingKeyXorChallenge.decrypt(cipherText);
    }
}