package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.HammingDistance;
import se.omegapoint.cryptochallenge.utils.HexadecimalBuffer;
import se.omegapoint.cryptochallenge.utils.NaturalLanguageScoring;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class RepeatingKeyXorChallenge {

    private static final int KEYSIZE_MIN = 2;
    private static final int KEYSIZE_MAX = 40;
    private static final int KEY_MIN = 0;
    private static final int KEY_MAX = 255;

    public HexadecimalBuffer decrypt(final HexadecimalBuffer cipherText) {
        final int calculatedLength = IntStream.range(KEYSIZE_MIN, KEYSIZE_MAX).boxed()
                .map(keySize -> toHammingCandidate(keySize, cipherText.chunk(keySize)))
                .map(c -> {
                    System.out.println(c.length + " " + c.score);
                    return c;
                })
                .min(HammingDistanceCandidate::compareTo)
                .get()
                .length;
        System.out.println("LENGTH " + calculatedLength);
        System.out.println("LENGTH " + Math.ceil(cipherText.length() / calculatedLength));
        int size = (int) Math.ceil(cipherText.length() / calculatedLength);
        List<HexadecimalBuffer> collect = cipherText.transpose(calculatedLength).chunk(size).stream().map(hexadecimalBuffer -> {
            final byte bestGuess = IntStream.range(KEY_MIN, KEY_MAX).boxed()
                    .map(Integer::byteValue)
                    .map(guess -> toKeyCandidate(hexadecimalBuffer, guess))
                    .max(KeyCandidate::compareTo)
                    .get()
                    .guess;
            System.out.println(bestGuess);
            System.out.println(cipherText.xor(new HexadecimalBuffer(bestGuess)).toString());
            return new HexadecimalBuffer(bestGuess);
        }).collect(toList());
        System.out.println(collect.size());
        System.out.println(cipherText.xor(HexadecimalBuffer.concat(collect)).toString());
        return null;
    }

    private HammingDistanceCandidate toHammingCandidate(final int chunkSize, final List<HexadecimalBuffer> chunkBuffers) {
        final int numberOfDistances = chunkBuffers.size() - 1;
        final double distance = IntStream.range(0, numberOfDistances).boxed()
                .map(i -> HammingDistance.normalizedBetween(chunkBuffers.get(i)).and(chunkBuffers.get(i + 1)))
                .reduce(0.0, (a, b) -> a + b);

        return new HammingDistanceCandidate(chunkSize, distance / numberOfDistances);
    }

    private KeyCandidate toKeyCandidate(HexadecimalBuffer cipherText, Byte guess) {
        return new KeyCandidate(guess, NaturalLanguageScoring.of(cipherText.xor(new HexadecimalBuffer(guess))));
    }

    private static class HammingDistanceCandidate implements Comparable<HammingDistanceCandidate> {
        public final int length;
        public final double score;

        public HammingDistanceCandidate(final int length, final double score) {
            this.length = length;
            this.score = score;
        }

        @Override
        public int compareTo(final HammingDistanceCandidate otherCandidate) {
            return Double.compare(score, otherCandidate.score);
        }
    }

    private static class KeyCandidate implements Comparable<KeyCandidate> {
        public final byte guess;
        public final double score;

        public KeyCandidate(final byte guess, final double score) {
            this.guess = guess;
            this.score = score;
        }

        @Override
        public int compareTo(final KeyCandidate otherCandidate) {
            return Double.compare(score, otherCandidate.score);
        }
    }

}
