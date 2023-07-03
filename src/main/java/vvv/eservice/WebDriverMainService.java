package vvv.eservice;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class WebDriverMainService implements CommandLineRunner {
    private final WebDriverConfig webDriverConfig;

    @Autowired
    public WebDriverMainService(WebDriverConfig webDriverConfig) {
        this.webDriverConfig = webDriverConfig;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello, CSE. You feel tiered and want to have wet night dreams...");
        startJob();
    }

    public void startJob() {
        System.setProperty(webDriverConfig.getWebDriverName(), webDriverConfig.getWebDriverPath());
        WebDriver driver = new ChromeDriver();
        driver.get("https://remote-eu.eservice.huawei.com:2007/?random=0.6424087888580134&amp;request_locale=en&amp;siteStatus=1#/resourceAll?showType=site&amp;from=eservicePortalIframe");
        loginToEserviceWebSite(driver);
        waitUntilPageLoads(driver);
        int amountOfMaskedClients = getClientsNameForEachPage(driver, getNumberOfPages(driver));
        SendMessageHowManyClientsWereMasked(amountOfMaskedClients);
        driver.quit();
    }

    private void SendMessageHowManyClientsWereMasked(int amountOfMaskedClients) {
        System.out.println(String.format("The amount of succesfully masked clients is: %d", amountOfMaskedClients));
    }

    private int getClientsNameForEachPage(WebDriver driver, int numberOfPages) {
        int amountOfMaskedClients = 0;
        for (int pageNumber = 1; pageNumber <= numberOfPages; pageNumber++) {
            List<WebElement> getClientsName = getClientsName(driver, pageNumber);
            for (int j = 0; j < getClientsName.size(); j++, amountOfMaskedClients++) {
                String clientName = getClientsName.get(j).getText();
                performMaskAlert(driver, pageNumber, j, clientName);
            }
            if (pageNumber < numberOfPages) {
                moveToAnotherPage(driver, pageNumber);
            }
        }
        return amountOfMaskedClients;
    }

    private void moveToAnotherPage(WebDriver driver, int pageNumber) {
        ++pageNumber;
        driver.findElement(By.xpath("//a[@ng-repeat='page in pages track by $index' and contains (text(),'" + pageNumber + "')]")).click();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        //TODO try to change to timeouts
/*
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        //проверяем что номер страницы активный и подставляем номер, чтобы убет=диться что мы именно на нужной странице
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains (@class, 'active') and contains (text(),'" + pageNumber + "')]")));
*/
    }

    private void performMaskAlert(WebDriver driver, int i, int j, String clientName) {
        driver.findElement(By.xpath("//a[@target='_blank' and contains (text(), '" + clientName + "')]")).click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        addDataToMaskAlert(driver);
        System.out.println("Mask Alert # " + i + "." + j);
    }

    private void addDataToMaskAlert(WebDriver driver) {
        //  driver.findElement(By.id("mani_icon_div")).click();
        String startDate = webDriverConfig.getStart().toLocalDate().toString();
        String startHour = String.valueOf(webDriverConfig.getStart().getHour());
        String startMinute = String.valueOf(webDriverConfig.getStart().getMinute());
        String startSeconds = String.valueOf(webDriverConfig.getStart().getSecond());
        String enddate = webDriverConfig.getEnd().toLocalDate().toString();
        String endHour = String.valueOf(webDriverConfig.getEnd().getHour());
        String endMinute = String.valueOf(webDriverConfig.getEnd().getMinute());
        String endSeconds = String.valueOf(webDriverConfig.getEnd().getSecond());
        Set<String> windowsSet = driver.getWindowHandles();
        System.out.println("****" + windowsSet);
        Iterator<String> stringIterator = windowsSet.iterator();
        String parentWindow = stringIterator.next();
        String childWindow = stringIterator.next();
        driver.switchTo().window(childWindow);
        driver.findElement(By.xpath("//span[@ng-bind='m1.label' and contains (text(), 'Alarm Masking')]")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        driver.findElement(By.xpath("//button[contains (text(), 'Create')]")).click();
        driver.findElement(By.xpath("//*[@id=\"alarmShieldHandleForm\"]/table/tbody/tr[1]/td[3]/span/input")).sendKeys("AlertK");
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div/span[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/input")).clear();
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/input")).sendKeys(webDriverConfig.getStart().toLocalDate().toString());
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/div[1]/div/input[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/div[1]/div/input[1]")).sendKeys(String.valueOf(webDriverConfig.getStart().getHour()));
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/div[1]/div/input[2]")).click();
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/div[1]/div/input[2]")).sendKeys(String.valueOf(webDriverConfig.getStart().getMinute()));
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/div[1]/div/input[3]")).click();
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/div[1]/div/input[3]")).sendKeys(String.valueOf(webDriverConfig.getStart().getSecond()));
        driver.findElement(By.xpath("//*[@id=\"startDate\"]/div[2]/div[2]/div[2]/button[2]")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        //END DAte
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[1]/span")).click();
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/input")).clear();
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/input")).sendKeys(webDriverConfig.getEnd().toLocalDate().toString());
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/div[1]/div/input[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/div[1]/div/input[1]")).sendKeys(String.valueOf(webDriverConfig.getEnd().getHour()));
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/div[1]/div/input[2]")).click();
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/div[1]/div/input[2]")).sendKeys(String.valueOf(webDriverConfig.getEnd().getMinute()));
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/div[1]/div/input[3]")).click();
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/div[1]/div/input[3]")).sendKeys(String.valueOf(webDriverConfig.getEnd().getSecond()));
        driver.findElement(By.xpath("//*[@id=\"endDate\"]/div[2]/div[2]/div[2]/button[2]")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        //Masking scope current client
        driver.findElement(By.xpath("//*[@id=\"alarmShieldHandleForm\"]/table/tbody/tr[6]/td[3]/section[1]/label/label")).click();
        //Insert alerts #
        driver.findElement(By.xpath("//*[@id=\"alarmShieldHandleForm\"]/table/tbody/tr[8]/td[3]/section[2]/input")).sendKeys(webDriverConfig.getAlertsList());
        //*[@id="alarmShieldHandleForm"]/table/tbody/tr[8]/td[3]/section[2]/input
        driver.findElement(By.xpath("//*[@id=\"alarmShieldHandleForm\"]/table/tbody/tr[8]/td[3]/section[2]/button")).click();
        //  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        driver.findElement(By.xpath("//*[@id=\"alarmShieldHandleForm\"]/table/tbody/tr[9]/td[3]/div/textarea")).sendKeys("customer request");
        driver.findElement(By.xpath("/html/body/div[1]/div/div/div[3]/button[1]")).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.close();
        driver.switchTo().window(parentWindow);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    }

    private void waitUntilPageLoads(WebDriver driver) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@target='_blank' and contains (text(), 'RG')]")));
    }

    private int getNumberOfPages(WebDriver driver) {
        return driver.findElements(By.xpath("//a[@ng-repeat]")).size();
    }

    private void loginToEserviceWebSite(WebDriver driver) {
        driver.findElement(By.id("username")).sendKeys("v84281061");
        driver.findElement(By.name("password")).sendKeys("Htxmzs_8891");
        driver.findElement(By.name("Submit")).click();
    }

    private List<WebElement> getClientsName(WebDriver driver, int pageNumber) {
        List<WebElement> listElements = driver.findElements(By.xpath("//a[@target='_blank']"));
        for (WebElement element : listElements) {
            System.out.println("HH" + element.getText());
        }
        return listElements;
    }
}
