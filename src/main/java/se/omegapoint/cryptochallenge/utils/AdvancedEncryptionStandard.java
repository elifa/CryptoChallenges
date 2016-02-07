package se.omegapoint.cryptochallenge.utils;

import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.crypto.OperationMode;
import org.apache.shiro.crypto.PaddingScheme;
import org.apache.shiro.util.ByteSource;

import java.util.Arrays;

public class AdvancedEncryptionStandard {

    private final ByteBuffer encryptionKey;
    private final AesCipherService cipherService;

    public AdvancedEncryptionStandard(final ByteBuffer encryptionKey) {
        this.encryptionKey = encryptionKey;
        this.cipherService = new AesCipherService();

        this.cipherService.setMode(OperationMode.ECB);
        this.cipherService.setPaddingScheme(PaddingScheme.NONE);
    }

    public ByteBuffer encrypt(final ByteBuffer plainText, final ByteBuffer initializationVector) {
        final ByteBuffer paddedPlainText = pkcs7pad(plainText, blockLength());
        final int noOfChunks = paddedPlainText.noOfChunks(blockLength());

        ByteBuffer result = new ByteBuffer();
        ByteBuffer previousChunk = initializationVector;

        for (int i = 0; i < noOfChunks; i++) {
            final ByteBuffer plainTextChunk = paddedPlainText.chunk(i, blockLength());
            final ByteBuffer chunkToEncrypt = plainTextChunk.xor(previousChunk);

            previousChunk = encrypt(chunkToEncrypt);

            result = result.concat(previousChunk);
        }

        return result;
    }

    public ByteBuffer decrypt(final ByteBuffer cipherText, final ByteBuffer initializationVector) {
        final int noOfChunks = cipherText.noOfChunks(blockLength());

        ByteBuffer result = new ByteBuffer();
        ByteBuffer currentChunk = cipherText.chunk(noOfChunks - 1, blockLength());

        for (int i = noOfChunks - 1; i >= 0; i--) {
            final ByteBuffer decryptedChunk = decrypt(currentChunk);
            final ByteBuffer previousChunk = i == 0 ? initializationVector : cipherText.chunk(i, blockLength());
            final ByteBuffer plainTextChunk = decryptedChunk.xor(previousChunk);

            result = result.concat(plainTextChunk);

            currentChunk = previousChunk;
        }

        return pkcs7unpad(result);
    }

    private ByteBuffer pkcs7pad(final ByteBuffer plainText, final int blockSize) {
        final int fullBlocks = plainText.length() / blockSize;
        final int paddedLength = (fullBlocks + 1) * blockSize;
        final int paddingLength = paddedLength - plainText.length();

        return plainText.pad(paddedLength, new ByteBuffer(paddingLength));
    }

    public ByteBuffer pkcs7unpad(final ByteBuffer plainText) {
        final byte paddingLength = plainText.bytes[plainText.length() - 2];
        
        if ((plainText.length() - paddingLength) < 0) {
            return plainText;
        }

        return new ByteBuffer(Arrays.copyOf(plainText.bytes, plainText.length() - paddingLength));
    }

    private ByteBuffer encrypt(final ByteBuffer plainText) {
        final ByteSource cipherText = cipherService.encrypt(plainText.bytes, encryptionKey.bytes);

        return new ByteBuffer(cipherText.getBytes());
    }

    private ByteBuffer decrypt(final ByteBuffer cipherText) {
        final ByteSource plainText = cipherService.decrypt(cipherText.bytes, encryptionKey.bytes);

        return new ByteBuffer(plainText.getBytes());
    }

    private int blockLength() {
        return encryptionKey.length();
    }

}
