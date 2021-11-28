package com.projet.parser;

import com.projet.parser.combinators.Combinator;
import com.projet.parser.combinators.State;

import java.text.CharacterIterator;
import java.util.List;

import static com.projet.parser.combinators.Combinators.*;

public class Parser implements Runnable {
    CharacterIterator frames;

    public Parser(CharacterIterator frames) {
        this.frames = frames;
    }

    private static Combinator<Void> commentLine() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                Object result = state.getResult();
                if (hexOffset().apply(state))
                    return false;
                if (!skipTo(newline()).apply(state))
                    return false;

                state.setResult(result);

                return true;
            }
        };
    }

    static Combinator<Fragment> fragment() {
        return new Combinator<>() {
            @Override
            public Boolean apply(State<? extends CharSequence> state) {
                if (!hexOffset().apply(state))
                    return false;

                int offset = hexOffset().getResult(state);

                // We skip spaces
                spaces().apply(state);

                // Parse the bytes
                Combinator<List<Byte>> bytesParser = many1(hexByte().skip(space()));
                if (!bytesParser.apply(state))
                    return false;

                List<Byte> bytes = bytesParser.getResult(state);

                if (!space().apply(state))
                    return false;

                // We skip everything until newline
                skipTo(newline()).apply(state);

                Fragment fragment = new Fragment(offset, bytes);
                state.setResult(fragment);

                return true;
            }
        };
    }

    @Override
    public void run() {

    }
}