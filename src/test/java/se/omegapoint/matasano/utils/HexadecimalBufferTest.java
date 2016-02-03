package se.omegapoint.matasano.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class HexadecimalBufferTest {

    @Test
    public void testXor() throws Exception {

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