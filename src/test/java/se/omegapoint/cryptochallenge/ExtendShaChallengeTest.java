package se.omegapoint.cryptochallenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import se.omegapoint.cryptochallenge.ExtendShaChallenge.ForgedMessage;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.SecureHashAlgorithm;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;

@RunWith(Parameterized.class)
public class ExtendShaChallengeTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"MyKey", "MyMessage"},
                {"Sommar2016", "{'fromAccount': '5463', 'toAccount': '6723', 'amount': '2000'}"},
                {"Omegapoint only uses secure keys for constructing MACs", "{'fromAccount': '5463', 'toAccount': '6723', 'amount': '2000'}"}
        });
    }

    private final ByteBuffer keyValue;
    private final ByteBuffer plainText;

    public ExtendShaChallengeTest(final String keyValue, final String plainText) {
        this.keyValue = new StringBuffer(keyValue);
        this.plainText = new StringBuffer(plainText);
    }

    @Test
    public void forgedMessageShouldHaveTheSameResultAsIfCorrectlyConstructed() throws Exception {
        // Original hash, typically constructed by the client knowing the secret key
        final ByteBuffer originalHash = new SecureHashAlgorithm(keyValue.concat(plainText)).encode();

        final ExtendShaChallenge challenge = new ExtendShaChallenge(plainText, originalHash, keyValue.length());
        final ForgedMessage forgedMessage = challenge.constructForgedMessage();

        // Verification hash, typically calculated on the recipient also knowing the secret key to verify that the client knew it
        final ByteBuffer verificationHash = new SecureHashAlgorithm(keyValue.concat(forgedMessage.forgedPlainText)).encode();
        
        assertEquals(verificationHash, forgedMessage.forgedHash);
        assertNotSame(plainText, forgedMessage.forgedPlainText);
    }

}