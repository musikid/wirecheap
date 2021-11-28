package com.projet;

import com.projet.parser.Parser;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        Path p = Path.of(args[0]);
        Parser parser = new Parser();
        try {
            System.out.println(parser.parse(Files.readString(p)));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
