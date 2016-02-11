package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.SecureHashAlgorithm;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

public class ExtendShaChallenge {

    public static final ByteBuffer FORGED_SUFFIX = new StringBuffer("HAHA!");

    private final ByteBuffer originalPlainText;
    private final ByteBuffer originalMac;
    private final int keyLength;

    public ExtendShaChallenge(final ByteBuffer originalPlainText, final ByteBuffer originalMac, final int keyLength) {
        this.originalPlainText = originalPlainText;
        this.originalMac = originalMac;
        this.keyLength = keyLength;
    }

    public ForgedMessage constructForgedMessage() {
        final int[] abcde = originalMac.toInts();

        final ByteBuffer suffix = simulateFirstByteIntegerPosition(FORGED_SUFFIX);

        final ByteBuffer forgedMessageIncludingKey = simulateFullForgedMessage(keyLength, suffix);
        final ByteBuffer forgedMessageWithoutKey = forgedMessageIncludingKey.subBuffer(keyLength);
        final ByteBuffer forged = new SecureHashAlgorithm(suffix).encode(abcde[0], abcde[1], abcde[2], abcde[3], abcde[4], forgedMessageIncludingKey.length() * 8 - suffix.length() * 8);
        
        return new ForgedMessage(forgedMessageWithoutKey, forged);
    }

    private ByteBuffer simulateFullForgedMessage(final int keyLength, final ByteBuffer suffix) {
        final ByteBuffer paddedMessage = new ByteBuffer(padMessage(keyLength));
        return paddedMessage.concat(suffix);
    }

    private int[] padMessage(final int keyLength) {
        return SecureHashAlgorithm.toPaddedIntegerArray(new ByteBuffer(new byte[keyLength]).concat(originalPlainText), 0);
    }

    private ByteBuffer simulateFirstByteIntegerPosition(final ByteBuffer customSuffix) {
        final int prefixLength = (4 - (customSuffix.length() % 4) % 4);
        return new ByteBuffer(new byte[prefixLength]).concat(customSuffix);
    }

    public static class ForgedMessage {

        public final ByteBuffer forgedPlainText;
        public final ByteBuffer forgedHash;

        public ForgedMessage(ByteBuffer forgedPlainText, ByteBuffer forgedHash) {
            this.forgedPlainText = forgedPlainText;
            this.forgedHash = forgedHash;
        }
    }
}
