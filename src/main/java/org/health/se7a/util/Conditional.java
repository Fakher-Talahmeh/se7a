package org.health.se7a.util;

import java.util.Arrays;
import java.util.Objects;

public class Conditional {

    public static Object ifElse(Boolean value, Object ifResult, Object elseResult) {
        return value ? ifResult : elseResult;
    }

    public static void requireNonNullObjects(Object... values) {
        Arrays.stream(values).forEach(Objects::requireNonNull);
    }
}
