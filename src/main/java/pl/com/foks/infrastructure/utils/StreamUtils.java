package pl.com.foks.infrastructure.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamUtils {
    private StreamUtils() {}

    public static <T> List<T> concatLists(List<T> list1, List<T> list2) {
        return Stream.concat(list1.stream(), list2.stream()).collect(Collectors.toList());
    }
}
