package core;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import utils.AllureUtils;
import utils.ImageUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * Created by CTAJlb on 12.01.2016.
 */
public class Browser implements Closeable {


    private WebDriver webDriver;
    private ChromeDriverService service;
    private static Select select;

    public Browser() {

        Properties properties = new Properties();
        FileInputStream propFile;
        try {
            propFile = new FileInputStream("test.properties");
            properties.load(propFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
        Enumeration<String> e = (Enumeration<String>) properties.propertyNames();
        while (e.hasMoreElements()) {
            String key = e.nextElement();
            System.setProperty(key, properties.getProperty(key));
            Reporter.log(key + " - " + properties.getProperty(key), 2, true);
        }
//
        switch (System.getProperty("test.browser")) {
            case "firefox":
                FirefoxProfile profile = new FirefoxProfile();
                try {
                    webDriver = new FirefoxDriver(profile);
//            webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                break;
            case "chrome":
                try {
                    service = new ChromeDriverService.Builder()
                            .usingDriverExecutable(new File(System.getProperty("user.dir") + "\\driver\\chromedriver.exe"))
                            .usingAnyFreePort()
                            .build();
                    webDriver = new ChromeDriver(service);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
                break;

            default: throw new IllegalArgumentException("Driver is not supported or incorrect driver name");
        }
                webDriver.manage().timeouts().pageLoadTimeout(Integer.parseInt(System.getProperty("load.timeout")), TimeUnit.SECONDS);
    }

    public Browser openPage(String url) {
        webDriver.get(url);
        return this;
    }
    public Browser waitForElement(By element) {
        return waitForElement(element, 10);
    }


    public Browser waitForElement(final By element, int timeoutSec) {
        WebDriverWait wait = new WebDriverWait(webDriver, timeoutSec);
        wait.until(ExpectedConditions.visibilityOfElementLocated(element));
        return this;
    }

    public Browser click(By element) {
        webDriver.findElement(element).click();
        AllureUtils.saveImageAttach("Clicked ", this.makeScreenshot());
        return this;
    }

    public Browser typeText(By element, String text) {
        webDriver.findElement(element).sendKeys(text);
        return this;
    }

    public WebElement getElement(By element) {
        return webDriver.findElement(element);
    }

    public String getValue(By element) {
        return getElement(element).getAttribute("value");
    }

    public String getText(By element) {
        return getElement(element).getText();
    }


    public List<WebElement> getElements(By element) {
        return webDriver.findElements(element);
    }

    public boolean isElementPresent(By element) {
        return getElements(element).size() != 0;
    }

    public byte[] makeScreenshot() {
        try {
            return ImageUtils.toByteArray(((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void close() {
        webDriver.close();
    }

}
