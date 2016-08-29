package DavidMencos.Bean;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;


/**
 * Created by David Fernando on 8/17/2016.
 */
public abstract class Driver {

    public WebDriver driver;
    public WebDriverWait wait;
    public WebDriverWait shortWait;

    @BeforeMethod (alwaysRun = true)
    public void InitializeWebDriver(){
        System.out.println("Open Driver for Firefox.");
        // Create instance of firefox driver
        System.setProperty("webdriver.gecko.driver", "lib/geckodriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setCapability("marionette", true);
        driver = new FirefoxDriver(capabilities);
        wait = new WebDriverWait(driver, 10, 1);
        shortWait = new WebDriverWait(driver, 4, 1);
//        driver.manage().window().maximize();
    }

    @AfterMethod (alwaysRun = true)
    public void CloseWebDriver(){
//        driver.quit();
        System.out.println("Attempt to close driver");
    }

}
