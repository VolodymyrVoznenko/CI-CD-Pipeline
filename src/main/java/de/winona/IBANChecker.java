package de.winona;

import java.util.*;

/**
 * Ein einfacher IBAN-Prüfer.
 *
 * <p>Prüfschritte:</p>
 * <ul>
 *   <li>Längenprüfung anhand des Ländercodes</li>
 *   <li>Umstellung der ersten 4 Zeichen an das Ende</li>
 *   <li>Umwandlung von Buchstaben in Zahlen (A=10 .. Z=35)</li>
 *   <li>Berechnung des Modulo 97 (Ergebnis muss 1 sein)</li>
 * </ul>
 *
 * <p><b>Unterstützte Länder:</b> AT, BE, CZ, DE, DK, FR</p>
 *
 * @since 1.0
 */
public class IBANChecker {

    /** Map mit erwarteten IBAN-Längen pro Land. */
    private static final Map<String, Integer> chars = new HashMap<>();
    static {
        chars.put("AT", 20);
        chars.put("BE", 16);
        chars.put("CZ", 24);
        chars.put("DE", 22);
        chars.put("DK", 18);
        chars.put("FR", 27);
    }

    /**
     * Einstiegspunkt der Anwendung. Demonstriert die IBAN-Prüfung
     * mit einer Beispiel-IBAN und gibt das Ergebnis auf der Konsole aus.
     *
     * @param args Konsolenargumente (nicht verwendet)
     */
    public static void main(String[] args) {
        String iban = "DE227902007600279131";
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + validate(iban));
    }

    /**
     * Führt die komplette Validierung einer IBAN durch:
     * <ol>
     *   <li>Längenprüfung</li>
     *   <li>Umstellung der Zeichen</li>
     *   <li>Konvertierung in eine Zahl</li>
     *   <li>Modulo-97-Prüfung</li>
     * </ol>
     *
     * @param iban IBAN als Zeichenkette (ohne Leerzeichen)
     * @return {@code true}, wenn die IBAN gültig ist, sonst {@code false}
     * @throws StringIndexOutOfBoundsException falls die Eingabe kürzer als 4 Zeichen ist
     */
    public static boolean validate(String iban) {
        if (!checkLength(iban)) {
            return false;
        }
        String rearrangedIban = rearrangeIban(iban);
        String convertedIban = convertToInteger(rearrangedIban);
        List<String> segments = createSegments(convertedIban);
        return calculate(segments) == 1;
    }

    /**
     * Berechnet den Rest der Modulo-97-Prüfung anhand von Segmenten.
     *
     * @param segments Liste von numerischen Segmenten
     * @return Rest der Division durch 97
     */
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

    /**
     * Prüft, ob die Länge der IBAN dem Land entspricht.
     *
     * @param iban IBAN
     * @return {@code true}, wenn der Ländercode bekannt ist und die Länge passt
     */
    private static boolean checkLength(String iban) {
        String countryCode = iban.substring(0, 2);
        return chars.containsKey(countryCode) && chars.get(countryCode) == iban.length();
    }

    /**
     * Wandelt die IBAN in eine reine Zahlenfolge um.
     * Buchstaben werden dabei in Zahlen umgerechnet (A=10 .. Z=35).
     *
     * @param iban IBAN nach Umstellung
     * @return numerische Darstellung der IBAN
     */
    private static String convertToInteger(String iban) {
        StringBuilder convertedIban = new StringBuilder();
        String upperIban = iban.toUpperCase(Locale.ROOT);
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

    /**
     * Teilt eine lange Zahl in kleinere Segmente auf, um
     * die Modulo-97-Prüfung stabil durchzuführen.
     * <p>Das erste Segment hat 9 Stellen, danach folgen Segmente
     * mit 7 Stellen, der Rest wird am Ende angehängt.</p>
     *
     * @param iban numerische IBAN als String
     * @return Liste von Segmenten
     */
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

    /**
     * Verschiebt die ersten vier Zeichen (Ländercode + Prüfziffern)
     * an das Ende der IBAN.
     *
     * @param iban ursprüngliche IBAN
     * @return umgestellte IBAN
     */
    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }
}
