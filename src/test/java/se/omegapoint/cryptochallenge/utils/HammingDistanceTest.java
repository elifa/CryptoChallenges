package se.omegapoint.cryptochallenge.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HammingDistanceTest {

    @Test
    public void verifyHammingDistanceCalculation() throws Exception {
        final StringBuffer firstBuffer = new StringBuffer("this is a test");
        final StringBuffer secondBuffer = new StringBuffer("wokka wokka!!!");

        assertEquals(37, HammingDistance.between(firstBuffer).and(secondBuffer), 0.01);
    }

    @Test
    public void verifyHammingDistanceNormalization() throws Exception {
        final double keyLength = 14;
        final StringBuffer firstBuffer = new StringBuffer("this is a test");
        final StringBuffer secondBuffer = new StringBuffer("wokka wokka!!!");

        assertEquals(37 / keyLength, HammingDistance.normalizedBetween(firstBuffer).and(secondBuffer), 0.01);
    }
}