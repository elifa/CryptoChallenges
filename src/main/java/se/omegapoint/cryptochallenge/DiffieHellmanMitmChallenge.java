package se.omegapoint.cryptochallenge;

import se.omegapoint.cryptochallenge.utils.AdvancedEncryptionStandard;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.DiffieHellman;
import se.omegapoint.cryptochallenge.utils.RandomBuffer;

import java.math.BigInteger;

public class DiffieHellmanMitmChallenge {

    private static final boolean DEBUG = false;

    public interface Client {
        ByteBuffer sessionSecret();

        ByteBuffer lastMessage();

        void onMessage(final Client sender, final ByteBuffer receivedCipherText, final ByteBuffer receivedInitializationVector);
    }

    public interface MasterClient extends Client {
        void connectTo(final SlaveClient target);

        void sendMessage(final Client target, final ByteBuffer message);

        void onHandshake(final Client sender, final BigInteger pub);
    }

    public interface SlaveClient extends Client {
        void onConnect(final MasterClient sender, final ByteBuffer p, final ByteBuffer g, final BigInteger pub);
    }

    public interface MitmClient extends MasterClient, SlaveClient {
    }

    public MasterClient createMaster(final ByteBuffer p, final ByteBuffer g) {
        return new InitiatingClient(p, g);
    }

    public SlaveClient createSlave() {
        return new EchoClient();
    }

    public MitmClient createMitm(final MasterClient master, final SlaveClient slave) {
        return new EvilClient(master, slave);
    }

    public static class InitiatingClient implements MasterClient {

        private final ByteBuffer p;
        private final ByteBuffer g;

        private ByteBuffer sessionSecret;
        private ByteBuffer lastMessage;
        private DiffieHellman diffieHellman;
        private AdvancedEncryptionStandard sessionEncryption;

        public InitiatingClient(final ByteBuffer p, final ByteBuffer g) {
            this.p = p;
            this.g = g;
            this.diffieHellman = new DiffieHellman(p, g);
        }

        @Override
        public void connectTo(final SlaveClient target) {
            target.onConnect(this, p, g, diffieHellman.pub);
        }

        @Override
        public void sendMessage(final Client target, final ByteBuffer message) {
            final ByteBuffer initializationVector = new RandomBuffer(sessionSecret.length());
            final ByteBuffer cipherText = this.sessionEncryption.encrypt(message, initializationVector);

            target.onMessage(this, cipherText, initializationVector);
        }

        @Override
        public void onHandshake(final Client sender, final BigInteger pub) {
            this.sessionSecret = this.diffieHellman.handshake(pub);
            this.sessionEncryption = new AdvancedEncryptionStandard(this.sessionSecret);
        }

        @Override
        public void onMessage(final Client sender, final ByteBuffer receivedCipherText, final ByteBuffer receivedInitializationVector) {
            final ByteBuffer plainText = sessionEncryption.decrypt(receivedCipherText, receivedInitializationVector);

            handleMessage(plainText);
        }

        @Override
        public ByteBuffer sessionSecret() {
            return sessionSecret;
        }

        @Override
        public ByteBuffer lastMessage() {
            return lastMessage;
        }

        private void handleMessage(final ByteBuffer message) {
            this.lastMessage = message;

            if (DEBUG) {
                System.out.println(getClass().getSimpleName() + ": " + message.toString());
            }
        }
    }

    public static class EchoClient implements SlaveClient {

        private ByteBuffer sessionSecret;
        private ByteBuffer lastMessage;
        private DiffieHellman diffieHellman;
        private AdvancedEncryptionStandard sessionEncryption;

        @Override
        public void onConnect(final MasterClient sender, final ByteBuffer p, final ByteBuffer g, final BigInteger pub) {
            this.diffieHellman = new DiffieHellman(p, g);
            this.sessionSecret = this.diffieHellman.handshake(pub);
            this.sessionEncryption = new AdvancedEncryptionStandard(this.sessionSecret);
            // Respond with the public key of the echo slave
            sender.onHandshake(this, diffieHellman.pub);
        }

        @Override
        public void onMessage(final Client sender, final ByteBuffer receivedCipherText, final ByteBuffer receivedInitializationVector) {
            final ByteBuffer plainText = sessionEncryption.decrypt(receivedCipherText, receivedInitializationVector);

            final ByteBuffer initializationVector = new RandomBuffer(sessionSecret.length());
            final ByteBuffer cipherText = this.sessionEncryption.encrypt(plainText, initializationVector);

            sender.onMessage(this, cipherText, initializationVector);

            handleMessage(plainText);
        }

        @Override
        public ByteBuffer sessionSecret() {
            return sessionSecret;
        }

        @Override
        public ByteBuffer lastMessage() {
            return lastMessage;
        }

        private void handleMessage(final ByteBuffer message) {
            this.lastMessage = message;

            if (DEBUG) {
                System.out.println(getClass().getSimpleName() + ": " + message.toString());
            }
        }
    }

    public static class EvilClient implements MitmClient {

        private final MasterClient master;
        private final SlaveClient slave;

        private ByteBuffer sessionSecret;
        private ByteBuffer lastMessage;
        private DiffieHellman diffieHellman;
        private AdvancedEncryptionStandard sessionEncryption;

        public EvilClient(final MasterClient master, final SlaveClient slave) {
            this.master = master;
            this.slave = slave;
        }

        @Override
        public void connectTo(final SlaveClient slave) {
            // Should never be called for the MitM-client
        }

        @Override
        public void sendMessage(final Client target, final ByteBuffer message) {
            // Should never be called for the MitM-client
        }

        @Override
        public void onConnect(final MasterClient sender, final ByteBuffer p, final ByteBuffer g, final BigInteger pub) {
            this.diffieHellman = new DiffieHellman(p, g);
            this.sessionSecret = this.diffieHellman.handshake(p.toInt());
            this.sessionEncryption = new AdvancedEncryptionStandard(this.sessionSecret);
            // Delegate the connect to the other client, but change the public key and use p instead
            slave.onConnect(this, p, g, p.toInt());
        }

        @Override
        public void onHandshake(final Client sender, final BigInteger pub) {
            master.onHandshake(this, diffieHellman.p);
        }

        @Override
        public void onMessage(final Client sender, final ByteBuffer receivedCipherText, final ByteBuffer receivedInitializationVector) {
            if (sender == slave) {
                master.onMessage(this, receivedCipherText, receivedInitializationVector);
            } else if (sender == master) {
                slave.onMessage(this, receivedCipherText, receivedInitializationVector);
            }

            final ByteBuffer plainText = sessionEncryption.decrypt(receivedCipherText, receivedInitializationVector);

            handleMessage(plainText);
        }

        @Override
        public ByteBuffer sessionSecret() {
            return sessionSecret;
        }

        @Override
        public ByteBuffer lastMessage() {
            return lastMessage;
        }

        private void handleMessage(final ByteBuffer message) {
            this.lastMessage = message;

            if (DEBUG) {
                System.out.println(getClass().getSimpleName() + ": " + message.toString());
            }
        }
    }

}
