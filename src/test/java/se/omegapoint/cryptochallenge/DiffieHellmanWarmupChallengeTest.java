package se.omegapoint.cryptochallenge;

import org.junit.Before;
import org.junit.Test;
import se.omegapoint.cryptochallenge.DiffieHellmanWarmupChallenge.MasterClient;
import se.omegapoint.cryptochallenge.DiffieHellmanWarmupChallenge.SlaveClient;
import se.omegapoint.cryptochallenge.utils.ByteBuffer;
import se.omegapoint.cryptochallenge.utils.HexadecimalBuffer;
import se.omegapoint.cryptochallenge.utils.StringBuffer;

import static junit.framework.TestCase.assertEquals;

public class DiffieHellmanWarmupChallengeTest {

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

    private SlaveClient slaveClient;
    private MasterClient masterClient;

    @Before
    public void before() {
        final DiffieHellmanWarmupChallenge challenge = new DiffieHellmanWarmupChallenge();

        slaveClient = challenge.createSlave();
        masterClient = challenge.createMaster(P, G);
    }

    @Test
    public void sessionKeysShouldBeEqual() throws Exception {
        masterClient.connectTo(slaveClient);

        assertEquals(masterClient.sessionSecret(), slaveClient.sessionSecret());
    }

    @Test
    public void messageShouldBeTheLastForBothClient() throws Exception {
        final ByteBuffer message = new StringBuffer("Test message: Hey yo!!");

        masterClient.connectTo(slaveClient);
        masterClient.sendMessage(slaveClient, message);

        assertEquals(message, masterClient.lastMessage());
        assertEquals(message, slaveClient.lastMessage());
    }
}