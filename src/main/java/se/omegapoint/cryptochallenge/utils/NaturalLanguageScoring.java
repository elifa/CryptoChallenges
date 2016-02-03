package se.omegapoint.cryptochallenge.utils;

import java.util.stream.IntStream;

public class NaturalLanguageScoring {

    private static double MISS = -5;
    private static double SPACE = 0;
    private static double[] FREQUENCIES = {
            8.167, 1.492, 2.782, 4.253, 12.702, 2.228, 2.015, 6.094, 6.966,
            0.153, 0.772, 4.025, 2.406, 6.749, 7.507, 1.929, 0.095, 5.987,
            6.327, 9.056, 2.758, 0.978, 2.360, 0.150, 1.974, 0.074
    };

    public static double of(final HexadecimalBuffer buffer) {
        return IntStream.range(0, buffer.bytes.length).asDoubleStream()
                .map(i -> toScore(buffer.bytes[(int) i]))
                .sum();
    }

    private static double toScore(final byte bufferByte) {
        final double lcScore = toScore(bufferByte, 'a');
        final double ucScore = toScore(bufferByte, 'A');
        final double spScore = bufferByte == ' ' ? SPACE : MISS;

        return Math.max(Math.max(lcScore, ucScore), spScore);
    }

    private static double toScore(final byte bufferByte, final int startByte) {
        final int position = bufferByte - startByte;

        if ((position < 0) || (position >= FREQUENCIES.length)) {
            return MISS;
        }

        return FREQUENCIES[position];
    }

}
