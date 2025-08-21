import de.winona.IBANChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IbanCheckerTest {
    @Test
    @DisplayName("Valide IBAN: DE22790200760027913168")
    void validIban() {
        String iban = "DE22790200760027913168";
        assertTrue(IBANChecker.validate(iban), "sollte gültig sein");
    }

    @Test
    @DisplayName("Nicht-valide IBAN (falsche Prüfziffern)")
    void invalidCheckDigits() {
        String iban = "DE21790200760027913173";
        assertFalse(IBANChecker.validate(iban), "sollte ungültig sein (Prüfziffern)");
    }

    @Test
    @DisplayName("Falsche Länge für DE")
    void wrongLength() {
        String iban = "DE227902007600279131";
        assertFalse(IBANChecker.validate(iban), "sollte ungültig sein (Länge)");
    }

    @Test
    @DisplayName("Unbekannter Ländercode")
    void unknownCountryCode() {
        String iban = "XX22790200760027913168";
        assertFalse(IBANChecker.validate(iban), "sollte ungültig sein (unbekannter Ländercode)");
    }

    @Test
    @DisplayName("Abdeckung des impliziten Klassenkonstruktors IBANChecker")
    void coverDefaultConstructor() {
        new IBANChecker();
    }
}
