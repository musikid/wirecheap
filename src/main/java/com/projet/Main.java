package com.projet;

import com.projet.parser.Fragment;
import com.projet.parser.Parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Path p = Path.of(args[0]);
        Parser parser = new Parser();
        try {
             parser.parse(Files.readString(p));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
