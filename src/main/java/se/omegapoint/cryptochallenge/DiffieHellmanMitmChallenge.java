package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.*;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

import java.math.BigInteger;

public class DiffieHellmanMitmChallenge {
    
    public void doSometing() {
        final InitiatingClient initClient = new InitiatingClient();
        final EchoClient echoClient = new EchoClient();

        initClient.connectTo(echoClient);
        initClient.sendMessage(echoClient, "HEJ!!!!");
    }

    private static class InitiatingClient {

        private static final ByteBuffer G = new ByteBuffer(0x02);
        private static final ByteBuffer P = new HexadecimalBuffer(
                "ffffffffffffffffc90fdaa22168c234c4c6628b80dc1cd129024" +
                        "e088a67cc74020bbea63b139b22514a08798e3404ddef9519b3cd" +
                        "3a431b302b0a6df25f14374fe1356d6d51c245e485b576625e7ec" +
                        "6f44c42e9a637ed6b0bff5cb6f406b7edee386bfb5a899fa5ae9f" +
                        "24117c4b1fe649286651ece45b3dc2007cb8a163bf0598da48361" +
                        "c55d39a69163fa8fd24cf5f83655d23dca3ad961c62f356208552" +
                        "bb9ed529077096966d670c354e4abc9804f1746c08ca237327fff" +
                        "fffffffffffff");

        private DiffieHellman diffieHellman;
        private ByteBuffer sessionSecret;
        private AdvancedEncryptionStandard sessionEncryption;

        public InitiatingClient() {
            this.diffieHellman = new DiffieHellman(P, G);
        }

        public void connectTo(final EchoClient otherClient) {
            otherClient.onConnect(this, P, G, diffieHellman.pub);
        }
        
        public String sendMessage(final EchoClient otherClient, final String message) {
            final ByteBuffer initializationVector = new RandomBuffer(sessionSecret.length());
            final ByteBuffer cipherText = this.sessionEncryption.encrypt(new StringBuffer(message), initializationVector);
            System.out.println(getClass().getSimpleName() + ": " + message);
            return otherClient.onMessage(this, cipherText, initializationVector);
        }

        public void onHandshake(final EchoClient otherClient, final BigInteger pub) {
            this.sessionSecret = this.diffieHellman.handshake(pub);
            this.sessionEncryption = new AdvancedEncryptionStandard(this.sessionSecret);
        }

        public String onMessage(final EchoClient otherClient, final ByteBuffer receivedCipherText, final ByteBuffer receivedInitializationVector) {
            final ByteBuffer plainText = sessionEncryption.decrypt(receivedCipherText, receivedInitializationVector);

            System.out.println(getClass().getSimpleName() + ": " + plainText.toString());
            
            return plainText.toString();
        }
    }

    private static class EchoClient {

        private DiffieHellman diffieHellman;
        private ByteBuffer sessionSecret;
        private AdvancedEncryptionStandard sessionEncryption;

        public EchoClient() {

        }

        public void onConnect(final InitiatingClient otherClient, final ByteBuffer p, final ByteBuffer g, final BigInteger pub) {
            this.diffieHellman = new DiffieHellman(p, g);
            this.sessionSecret = this.diffieHellman.handshake(pub);
            this.sessionEncryption = new AdvancedEncryptionStandard(this.sessionSecret);
            // Respond with the public key of the echo client
            otherClient.onHandshake(this, diffieHellman.pub);
        }

        public String onMessage(final InitiatingClient otherClient, final ByteBuffer receivedCipherText, final ByteBuffer receivedInitializationVector) {
            final ByteBuffer plainText = sessionEncryption.decrypt(receivedCipherText, receivedInitializationVector);
            
            System.out.println(getClass().getSimpleName() + ": " + plainText.toString());

            final ByteBuffer initializationVector = new RandomBuffer(sessionSecret.length());
            final ByteBuffer cipherText = this.sessionEncryption.encrypt(plainText, initializationVector);
            
            otherClient.onMessage(this, cipherText, initializationVector);
            
            return plainText.toString();
        }
    }

}
