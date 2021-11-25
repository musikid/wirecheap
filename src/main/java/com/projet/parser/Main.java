package com.projet.parser;

import com.projet.parser.combinators.Combinator;
import com.projet.parser.combinators.Combinators;
import com.projet.parser.combinators.ParseException;

public class Main {
    public static void main(String[] args) throws ParseException {
        Combinator<Character> cs = Combinators.satisfy(c -> c == ' ');
        System.out.println(cs.parse("\ndsqfmlk"));
    }
}
