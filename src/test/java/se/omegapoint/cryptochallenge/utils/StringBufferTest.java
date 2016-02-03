package se.omegapoint.cryptochallenge.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringBufferTest {

    @Test
    public void shouldBeAbleToRecreateString() {
        final String string = "My test string!";
        final StringBuffer buffer = new StringBuffer(string);

        assertEquals(string, buffer.toString());
    }

}