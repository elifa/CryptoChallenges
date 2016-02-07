package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.HammingDistance;
import se.omegapoint.cryptochallenge.utils.NaturalLanguageScoring;

import java.util.List;
import java.util.stream.IntStream;

public class RepeatingKeyXorChallenge {

    private static final int KEYSIZE_MIN = 2;
    private static final int KEYSIZE_MAX = 40;
    private static final int KEY_MIN = 0;
    private static final int KEY_MAX = 255;

    public ByteBuffer decrypt(final ByteBuffer cipherText) {
        return IntStream.range(KEYSIZE_MIN, KEYSIZE_MAX).boxed()
                .map(keySize -> toHammingCandidate(keySize, cipherText.chunk(keySize)))
                .sorted(HammingDistanceCandidate::compareTo)
                .limit(1)
                .map(hammingDistance -> {
                    final ByteBuffer keyBuffer = cipherText.transpose(hammingDistance.length).split(hammingDistance.length).stream().map(hexadecimalBuffer ->
                            new ByteBuffer(IntStream.range(KEY_MIN, KEY_MAX).boxed()
                                    .map(Integer::byteValue)
                                    .map(guess -> toKeyCandidate(hexadecimalBuffer, guess))
                                    .max(KeyCandidate::compareTo)
                                    .get()
                                    .guess)).reduce(new ByteBuffer(), ByteBuffer::concat);
                    return cipherText.xor(keyBuffer);
                })
                .findFirst()
                .get();
    }

    private HammingDistanceCandidate toHammingCandidate(final int chunkSize, final List<ByteBuffer> chunkBuffers) {
        final int numberOfDistances = chunkBuffers.size() - 1;
        final double distance = IntStream.range(0, numberOfDistances).boxed()
                .map(i -> HammingDistance.normalizedBetween(chunkBuffers.get(i)).and(chunkBuffers.get(i + 1)))
                .reduce(0.0, (a, b) -> a + b);

        return new HammingDistanceCandidate(chunkSize, distance / numberOfDistances);
    }

    private KeyCandidate toKeyCandidate(ByteBuffer cipherText, Byte guess) {
        return new KeyCandidate(guess, NaturalLanguageScoring.of(cipherText.xor(new ByteBuffer(guess))));
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
