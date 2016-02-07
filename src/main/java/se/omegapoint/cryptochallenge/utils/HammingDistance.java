package se.omegapoint.cryptochallenge.utils;

public class HammingDistance {

    public static class OngoingHammingDistanceCalculation {
        protected final ByteBuffer baseBuffer;

        public OngoingHammingDistanceCalculation(final ByteBuffer baseBuffer) {
            this.baseBuffer = baseBuffer;
        }

        public double and(final ByteBuffer compareBuffer) {
            final int minLength = Math.min(baseBuffer.length(), compareBuffer.length());
            final int maxLength = Math.max(baseBuffer.length(), compareBuffer.length());

            int result = 0;

            for (int i = 0; i < minLength; i++) {
                result += Integer.bitCount(baseBuffer.bytes[i] ^ compareBuffer.bytes[i]);
            }

            result += 8 * (maxLength - minLength);

            return result;
        }
    }

    public static class OngoingNormalizedHammingDistanceCalculation extends OngoingHammingDistanceCalculation {

        public OngoingNormalizedHammingDistanceCalculation(final ByteBuffer baseBuffer) {
            super(baseBuffer);
        }

        @Override
        public double and(final ByteBuffer compareBuffer) {
            final double distance = super.and(compareBuffer);
            final int maxLength = Math.max(baseBuffer.length(), compareBuffer.length());

            return distance / (double) maxLength;
        }
    }

    public static OngoingHammingDistanceCalculation between(final ByteBuffer buffer) {
        return new OngoingHammingDistanceCalculation(buffer);
    }

    public static OngoingNormalizedHammingDistanceCalculation normalizedBetween(final ByteBuffer buffer) {
        return new OngoingNormalizedHammingDistanceCalculation(buffer);
    }
}
