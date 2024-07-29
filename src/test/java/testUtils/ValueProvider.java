package testUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ValueProvider {
    private static final Random random = new Random();

    public static String randomString(String base) {
        int letterSmallA = 97;
        int letterSmallZ = 122;
        int targetStringLength = 10;

        return String.join(base, random.ints(letterSmallA, letterSmallZ + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString());
    }

    public static long randomId() {
        return random.nextLong();
    }

    public static BigDecimal randomBigDecimal() {
        return BigDecimal.valueOf(random.nextDouble(0.50, 100));
    }


    public static Integer randomInt() {
        return random.nextInt();
    }

    public static Integer randomIntBetween(int min, int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    public static Double randomDouble() {
        return random.nextDouble();
    }

    public static Integer randomPercentage() {
        return random.nextInt(1, 100);
    }

    public static boolean randomBoolean() {
        return random.nextBoolean();
    }

    public static LocalDate randomLocalDate() {

        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2025, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
