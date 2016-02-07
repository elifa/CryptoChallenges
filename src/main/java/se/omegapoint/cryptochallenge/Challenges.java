package se.omegapoint.cryptochallenge;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;

public class Challenges {
    
    public static void main(final String[] args) throws IOException {
        System.out.println(Resources.toString(Resources.getResource("README.md"), Charsets.UTF_8));
    }
    
}
