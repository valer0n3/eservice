package vvv.eservice;

import java.time.format.DateTimeFormatter;

public final class ProjectConstants {
    public static final DateTimeFormatter DATE_TIME_FORMATTER_PATTERN = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    private ProjectConstants() {
    }
}
