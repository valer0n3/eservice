package vvv.eservice;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Properties;

import static vvv.eservice.ProjectConstants.DATE_TIME_FORMATTER_PATTERN;

@Component
@Getter
public class WebDriverConfig {
    private final String webDriverName;
    private final String webDriverPath;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String alertsList;

    public WebDriverConfig() throws IOException {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\application.properties");
        String path = WebDriverConfig.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        properties.load(fileInputStream);
        this.webDriverName = properties.getProperty("webdriver.name");
        this.webDriverPath = properties.getProperty("webdriver.path");
        this.start = LocalDateTime.parse(properties.getProperty("start"), DATE_TIME_FORMATTER_PATTERN);
        this.end = LocalDateTime.parse(properties.getProperty("end"), DATE_TIME_FORMATTER_PATTERN);
        checkDates();
        this.alertsList = properties.getProperty("alerts");
    }

    private void checkDates() {
        if (start.isBefore(LocalDateTime.now()) || end.isBefore(LocalDateTime.now())) {
            throw new DateTimeException("The dat can't be earlier then today!");
        }
        if (start.isAfter(LocalDateTime.now().plusDays(3)) || end.isAfter(LocalDateTime.now().plusDays(4))) {
            throw new DateTimeException("You can't perform mask 4 days in advance!");
        }
        if (start.isAfter(end)) {
            throw new DateTimeException("Start date can't be alter then end date!");
        }
    }
}


