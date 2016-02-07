package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.NaturalLanguageScoring;

import java.util.stream.IntStream;

public class SingleByteXorChallenge {

    private static final int KEY_MIN = 0;
    private static final int KEY_MAX = 255;

    public ByteBuffer decrypt(final ByteBuffer cipherText) {
        final byte bestGuess = IntStream.range(KEY_MIN, KEY_MAX).boxed()
                .map(Integer::byteValue)
                .map(guess -> new Candidate(guess, NaturalLanguageScoring.of(cipherText.xor(new ByteBuffer(guess)))))
                .max(Candidate::compareTo)
                .get()
                .guess;

        return cipherText.xor(new ByteBuffer(bestGuess));
    }

    private static class Candidate implements Comparable<Candidate> {
        public final byte guess;
        public final double score;

        public Candidate(byte guess, double score) {
            this.guess = guess;
            this.score = score;
        }

        @Override
        public int compareTo(final Candidate otherCandidate) {
            return Double.compare(score, otherCandidate.score);
        }
    }

}
