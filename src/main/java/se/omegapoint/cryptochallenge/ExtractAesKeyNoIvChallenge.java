package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.AdvancedEncryptionStandard;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;

import java.util.Arrays;

public class ExtractAesKeyNoIvChallenge {

    private final AdvancedEncryptionStandard aes;
    private final ByteBuffer iv;

    public ExtractAesKeyNoIvChallenge(final AdvancedEncryptionStandard aes, final ByteBuffer iv) {
        this.aes = aes;
        this.iv = iv;
    }

    public ByteBuffer extractKeyFromCipherText(final ByteBuffer cipherText) {
        final ByteBuffer firstCipherTextChunk = cipherText.chunk(1, aes.blockLength());

        ByteBuffer attackBuffer = firstCipherTextChunk.concat(new ByteBuffer(emptyByteArray(aes.blockLength()))).concat(firstCipherTextChunk);
        for (int i = 2; i < cipherText.noOfChunks(aes.blockLength()); i++) {
            attackBuffer = attackBuffer.concat(cipherText.chunk(i, aes.blockLength()));
        }

        final ByteBuffer decryptedBuffer = aes.decrypt(attackBuffer, iv);

        //P'_1 = decrypt(K, C_1) xor K = P_1
        //P'_2 = decrypt(K, C_2) xor C_1 = (some unimportant junk)
        //P'_3 = decrypt(K, C_1) xor 0 = P_1 xor K

        return decryptedBuffer.chunk(0, aes.blockLength()).xor(decryptedBuffer.chunk(2, aes.blockLength()));
    }

    private byte[] emptyByteArray(final int length) {
        final byte[] byteArray = new byte[length];

        Arrays.fill(byteArray, (byte) 0x00);

        return byteArray;
    }

}
