package com.divani.android.handystockmanager.custom;

/**
 * Created by ADMIN on 13-Mar-17.
 */

public class StringFormatter {

    public static String toTitleCase(String input) {

        String result = "";
        if (input.length() == 0) {
            return result;
        }
        char firstChar = input.charAt(0);
        char firstCharToUpperCase = Character.toUpperCase(firstChar);
        result = result + firstCharToUpperCase;

        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            char previousChar = input.charAt(i - 1);
            if (previousChar == ' ') {
                char currentCharToUpperCase = Character.toUpperCase(currentChar);
                result = result + currentCharToUpperCase;
            } else {
                char currentCharToLowerCase = Character.toLowerCase(currentChar);
                result = result + currentCharToLowerCase;
            }
        }
        return result;
    }
}
