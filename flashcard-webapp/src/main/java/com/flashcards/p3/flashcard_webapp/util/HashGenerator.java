package com.flashcards.p3.flashcard_webapp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HashGenerator {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java HashGenerator <rawPassword>");
            System.exit(1);
        }
        String raw = args[0];
        String hash = new BCryptPasswordEncoder().encode(raw);
        System.out.println(hash);
    }
}