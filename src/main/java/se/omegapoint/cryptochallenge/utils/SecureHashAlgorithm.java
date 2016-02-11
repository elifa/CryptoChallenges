package se.omegapoint.cryptochallenge.utils;

import java.util.Arrays;

public class SecureHashAlgorithm {

    private static final int DEFAULT_E = -1009589776;
    private static final int DEFAULT_D = 271733878;
    private static final int DEFAULT_C = -1732584194;
    private static final int DEFAULT_B = -271733879;
    private static final int DEFAULT_A = 1732584193;

    private final ByteBuffer buffer;

    public SecureHashAlgorithm(final ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer encode() {
        final int[] blocks = toPaddedIntegerArray(buffer, 0);
        final int[] words = encodePaddedInts(blocks, DEFAULT_A, DEFAULT_B, DEFAULT_C, DEFAULT_D, DEFAULT_E);

        return new ByteBuffer(words);
    }

    public ByteBuffer encode(int a, int b, int c, int d, int e, int addToLength) {
        final int[] blocks = toPaddedIntegerArray(buffer, addToLength);
        final int[] words = encodePaddedInts(blocks, a, b, c, d, e);

        return new ByteBuffer(words);
    }

    // calculate 160 bit SHA1 hash of the sequence of blocks
    private static int[] encodePaddedInts(int[] blks, int a, int b, int c, int d, int e) {
        for (int i = 0; i < blks.length; i += 16) {
            final int[] w = new int[80];
            final int olda = a;
            final int oldb = b;
            final int oldc = c;
            final int oldd = d;
            final int olde = e;

            for (int j = 0; j < 80; j++) {
                w[j] = (j < 16) ? blks[i + j] : (rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1));

                final int t = rol(a, 5) + e + w[j] + ((j < 20) ? 1518500249 + ((b & c) | ((~b) & d)) : (j < 40) ? 1859775393 + (b ^ c ^ d) : (j < 60) ? -1894007588 + ((b & c) | (b & d) | (c & d)) : -899497514 + (b ^ c ^ d));

                e = d;
                d = c;
                c = rol(b, 30);
                b = a;
                a = t;
            }

            a = a + olda;
            b = b + oldb;
            c = c + oldc;
            d = d + oldd;
            e = e + olde;
        }

        final int[] words = {a, b, c, d, e};

        return words;
    }

    public static int[] toPaddedIntegerArray(final ByteBuffer input, int addToLength) {
        final int[] words = input.toInts();

        // Convert an input to a sequence of 16-word blocks, stored as an array.
        // Append padding bits and the length, as described in the SHA1 standard
        int noOfBlocks = (((input.length() + 8) >> 6) + 1) * 16;
        int[] blocks = Arrays.copyOf(words, noOfBlocks);

        int idx = input.length() >> 2;
        int currentPos = input.length();

        blocks[idx] = ((blocks[idx] << 8) | (0x80)) << ((3 - (currentPos & 3)) << 3);
        blocks[blocks.length - 1] = (input.length() * 8) + addToLength;

        return blocks;
    }

    private static int rol(int num, int cnt) {
        return (num << cnt) | (num >>> (32 - cnt));
    }

}
