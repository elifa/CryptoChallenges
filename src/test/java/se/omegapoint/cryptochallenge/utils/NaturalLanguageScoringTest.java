package se.omegapoint.cryptochallenge.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class NaturalLanguageScoringTest {

    @Test
    public void shouldScoreLowerCase() throws Exception {
        final double abcScore = NaturalLanguageScoring.of(new StringBuffer("abc"));
        final double symbolScore = NaturalLanguageScoring.of(new StringBuffer("€3#"));

        assertTrue(abcScore > symbolScore);
    }

    @Test
    public void shouldScoreUpperCase() throws Exception {
        final double abcScore = NaturalLanguageScoring.of(new StringBuffer("ABC"));
        final double symbolScore = NaturalLanguageScoring.of(new StringBuffer("€3#"));

        assertTrue(abcScore > symbolScore);
    }
}