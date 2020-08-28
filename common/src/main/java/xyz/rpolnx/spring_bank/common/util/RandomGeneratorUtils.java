package xyz.rpolnx.spring_bank.common.util;

public class RandomGeneratorUtils {

    public static String generateSizedRandomNumbers(Integer size) {
        return generateSizedRandomNumbers(size, "");
    }

    public static String generateSizedRandomNumbers(Integer size, String prefix) {
        long cardNumber = (long) (Math.random() * Math.pow(10, size));
        String baseNumber = String.valueOf(cardNumber);

        if (prefix.length() >= size) {
            return prefix.substring(0, size);
        }

        int count = baseNumber.length() + prefix.length();

        if (count > size) {
            baseNumber = baseNumber.substring(0, size - prefix.length());
        } else {
            String leftZeros = "0".repeat(size - count);
            baseNumber = leftZeros + baseNumber;
        }

        return prefix + baseNumber;
    }
}
