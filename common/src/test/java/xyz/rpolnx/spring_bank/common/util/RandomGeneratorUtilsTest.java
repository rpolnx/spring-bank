package xyz.rpolnx.spring_bank.common.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Log4j2
public class RandomGeneratorUtilsTest {

    @DisplayName("When passed a number and prefix, should generate random numbers with passed size")
    @RepeatedTest(value = 70)
    public void shouldGenerateWithPrefix() {
        String generated = RandomGeneratorUtils.generateSizedRandomNumbers(10, "25");

        assertDoesNotThrow(() -> Long.valueOf(generated));

        log.info("Generated number: {}", generated);

        Assertions.assertEquals(10, generated.length());
        Assertions.assertEquals("25", generated.substring(0, 2));
    }

    @RepeatedTest(value = 70)
    @DisplayName("When pass a number, should generate random numbers with informed length")
    public void shouldGenerateWithoutPrefix() {
        String generated = RandomGeneratorUtils.generateSizedRandomNumbers(15);

        assertDoesNotThrow(() -> Long.valueOf(generated));

        log.info("Generated number: {}", generated);

        Assertions.assertEquals(15, generated.length());
    }

    @Test
    @DisplayName("When pass zero as size, should throw NumberFormatException")
    public void shouldGenerateWithError() {
        String generated = RandomGeneratorUtils.generateSizedRandomNumbers(0);

        assertThrows(NumberFormatException.class, () -> Long.valueOf(generated));

        Assertions.assertEquals(0, generated.length());
    }

    @Test
    @DisplayName("When pass a prefix greater than size, should return trunc prefix with passed size")
    public void shouldGenerateWithPartialPrefix() {
        String generated = RandomGeneratorUtils.generateSizedRandomNumbers(5, "123456");

        assertDoesNotThrow(() -> Long.valueOf(generated));

        Assertions.assertEquals(5, generated.length());
        Assertions.assertEquals("12345", generated);
    }

}