package de.winona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IBANChecker {
    private static final Map<String, Integer> chars = new HashMap<>();
    static {
        chars.put("AT", 20);
        chars.put("BE", 16);
        chars.put("CZ", 24);
        chars.put("DE", 22);
        chars.put("DK", 18);
        chars.put("FR", 27);
    }
    public static void main(String[] args) {
        String iban = "DE227902007600279131";
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + validate(iban));
    }
    public static boolean validate(String iban) {
        if (!checkLength(iban)) {
            return false;
        }
        String rearrangedIban = rearrangeIban(iban);
        String convertedIban = convertToInteger(rearrangedIban);
        List<String> segments = createSegments(convertedIban);
        return calculate(segments) == 1;
    }
    private static int calculate(List<String> segments) {
        long n = 0;
        for (String segment : segments) {
            if (segment.length() == 9) {
                n = Long.parseLong(segment) % 97;
            } else {
                segment = n + segment;
                n = Long.parseLong(segment) % 97;
            }
        }
        return (int) n;
    }
    private static boolean checkLength(String iban) {
        String countryCode = iban.substring(0, 2);
        return chars.containsKey(countryCode) && chars.get(countryCode) ==
                iban.length();
    }
    private static String convertToInteger(String iban) {
        StringBuilder convertedIban = new StringBuilder();
        String upperIban = iban.toUpperCase();
        for (char c : upperIban.toCharArray()) {
            if (Character.isDigit(c)) {
                convertedIban.append(c);
            }
            if (Character.isLetter(c)) {
                convertedIban.append(c - 55);
            }
        }
        return convertedIban.toString();
    }
    private static List<String> createSegments(String iban) {
        List<String> segments = new ArrayList<>();
        String remainingIban = iban;
        segments.add(remainingIban.substring(0, 9));
        remainingIban = remainingIban.substring(9);
        while (remainingIban.length() >= 9) {
            segments.add(remainingIban.substring(0, 7));
            remainingIban = remainingIban.substring(7);
        }
        segments.add(remainingIban);
        return segments;
    }
    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }
}