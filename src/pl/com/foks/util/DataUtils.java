package pl.com.foks.util;

import java.util.function.Consumer;

public class DataUtils {
    /**
     * Validates the data length
     * @param data data to validate
     * @param expectedLength expected length of the data
     */
    public static void validateData(String[] data, int expectedLength) {
        if (data == null || data.length != expectedLength) {
            throw new IllegalArgumentException("Invalid data");
        }
    }

    /**
     * Checks if the data is valid and then executes the consumer
     * @param data data to check
     * @param expectedLength expected length of the data
     * @param dataConsumer consumer of the data
     */
    public static void isDataValidThen(String[] data, int expectedLength, Consumer<String[]> dataConsumer) {
        if (data == null || data.length != expectedLength) {
            return;
        }
        dataConsumer.accept(data);
    }

    private DataUtils() {
    }
}
