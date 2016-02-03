package se.omegapoint.cryptochallenge.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class HexadecimalBufferTest {

    @Test
    public void verifyRepeatingKeyXor() throws Exception {
        final String plainText = "Burning 'em, if you ain't quick and nimble\nI go crazy when I hear a cymbal";
        final String keyString = "ICE";

        final String cipherText = new StringBuffer(plainText).xor(new StringBuffer(keyString)).toHex();

        assertEquals("0b3637272a2b2e63622c2e69692a23693a2a3c6324202d623d63343c2a26226324272765272a282b2f20430a652e2c652a3124333a653e2b2027630c692b20283165286326302e27282f", cipherText);
    }

    @Test
    public void verifyHexEncoding() throws Exception {
        final String hexadecimalString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        final HexadecimalBuffer buffer = new HexadecimalBuffer(hexadecimalString);

        final String generatedHexadecimalString = buffer.toHex();

        assertEquals(hexadecimalString, generatedHexadecimalString);
    }

    @Test
    public void verifyBase64Encoding() throws Exception {
        final String hexadecimalString = "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d";
        final HexadecimalBuffer buffer = new HexadecimalBuffer(hexadecimalString);

        final String base64String = buffer.toBase64();

        assertEquals("SSdtIGtpbGxpbmcgeW91ciBicmFpbiBsaWtlIGEgcG9pc29ub3VzIG11c2hyb29t", base64String);
    }
}