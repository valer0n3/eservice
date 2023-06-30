package vvv.eservice;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
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
        properties.load(fileInputStream);
        this.webDriverName = properties.getProperty("webdriver.name");
        this.webDriverPath = properties.getProperty("webdriver.path");
        this.start = LocalDateTime.parse(properties.getProperty("start"), DATE_TIME_FORMATTER_PATTERN);
        this.end = LocalDateTime.parse(properties.getProperty("end"), DATE_TIME_FORMATTER_PATTERN);
        this.alertsList = properties.getProperty("alerts");
    }
}


