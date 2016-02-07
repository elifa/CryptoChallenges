package se.omegapoint.cryptochallenge;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import se.omegapoint.cryptochallenge.utils.HexadecimalBuffer;

import java.util.Arrays;
import java.util.Collection;

import static junit.framework.TestCase.assertEquals;

@RunWith(Parameterized.class)
public class XorWarmupChallengeTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", "", ""},
                {"ff", "00", "ff"},
                {"00", "ff", "ff"},
                {"1c0111001f010100061a024b53535009181c", "686974207468652062756c6c277320657965", "746865206b696420646f6e277420706c6179"}
        });
    }

    private final HexadecimalBuffer keyString;
    private final HexadecimalBuffer plainText;
    private final HexadecimalBuffer cipherText;

    public XorWarmupChallengeTest(final String keyString, final String plainText, final String cipherText) {
        this.keyString = new HexadecimalBuffer(keyString);
        this.plainText = new HexadecimalBuffer(plainText);
        this.cipherText = new HexadecimalBuffer(cipherText);
    }

    @Test
    public void verifyXorEncryption() throws Exception {
        assertEquals(cipherText, new XorWarmupChallenge(keyString).encrypt(plainText));
    }
}