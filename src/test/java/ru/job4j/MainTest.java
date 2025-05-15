package ru.job4j;

import org.junit.jupiter.api.Test;
import ru.job4j.bmb.content.Content;
import ru.job4j.bmb.services.MoodService;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class MainTest {
    private final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern("dd-MM-yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    static final long SECOND = 1000;
    static final long MINUTE = SECOND * 60;
    static final long HOUR = MINUTE * 60;
    static final long DAY = HOUR * 24;
    static final long MONTH = DAY * 30;

    @Test
    public void whenActionThenResult() {
        int excepted = 1;
        int result = 1;
        assertThat(result).isEqualTo(excepted);
    }
}
