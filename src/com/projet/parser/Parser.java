package com.projet.parser;

import java.text.CharacterIterator;
import java.util.concurrent.locks.ReentrantLock;

public class Parser implements Runnable {
    CharacterIterator frames;
    private ReentrantLock lock;

    public Parser() {

    }

    @Override
    public void run() {
        StringBuilder offsetString = new StringBuilder();
        char c = frames.current();
        int offset;

        while (!Character.isSpaceChar(c)) {
            offsetString.append(c);
            c += frames.next();
        }

        try {
            offset = Integer.parseInt(offsetString.toString(), 16);
        } catch (NumberFormatException e) {

        }

        // Skip all whitespaces
        while (Character.isSpaceChar(frames.current())) frames.next();

        String hexByte = "";

        if (Character.digit(frames.current(), 16) != -1) {
            hexByte += frames.current();

        }
        frames.next();
        frames.next();
    }
}