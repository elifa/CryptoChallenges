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

    @Test
    public void shouldScoreSpacesAndFrequentSpecialCharacters() throws Exception {
        final double specialScore = NaturalLanguageScoring.of(new StringBuffer(" '\n '!."));
        final double symbolScore = NaturalLanguageScoring.of(new StringBuffer("#%€/&)1"));

        assertTrue(specialScore > symbolScore);
    }

    @Test
    public void shouldScoreTextAboveBytes() throws Exception {
        final double textScore = NaturalLanguageScoring.of(new StringBuffer("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?"));
        final double byteScore = NaturalLanguageScoring.of(new StringBuffer("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?").xor(new HexadecimalBuffer("324faa")));

        assertTrue(textScore > byteScore);
    }
}