package com.lu3in033.projet;

import com.lu3in033.projet.parser.Parser;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: wirecheap [file]");
            return;
        }

        Path p = Path.of(args[0]);
        Parser parser = new Parser();
        try {
            System.out.println(parser.parse(Files.readString(p)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
