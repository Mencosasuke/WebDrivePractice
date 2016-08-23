package DavidMencos.Tests;

import DavidMencos.Bean.Driver;
import DavidMencos.Helpers.AuxTestMethods;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.event.InputEvent;

/**
 * Created by David Fernando on 8/19/2016.
 */
public class ExerciseThree extends Driver {

    @Test
    public void TestFinancePaymentFilter(){
        try {
            driver.get("http://qa.autoweb.com/search");
            AuxTestMethods.waitForPageLoad(driver);
            AuxTestMethods.ClickTrimModelVariationMessage(driver);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'financePayment')]"))).click();
            WebElement upperHandler = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'financeSlider')]/div[contains(@class, 'noUi-base')]/div[contains(@class, 'noUi-origin noUi-background')]/div[contains(@class, 'noUi-handle-upper')]")));
            upperHandler.click();
//  System.out.println(upperHandler.getAttribute("class"));
            //new Actions(driver).clickAndHold(upperHandler).release(upperHandler).perform();



//            driver.get("http://www.google.com");
//            WebElement search = driver.findElement(By.name("q"));
//            search.sendKeys("autoweb");
//            search.submit();
//            Thread.sleep(2000);
//            WebElement entrada = driver.findElement(By.xpath("//div[contains(@id, 'rso')]/div[contains(@class, 'g')]/div/div[contains(@class, 'rc')]/h3/a"));
//            WebElement busqueda = driver.findElement(By.name("q"));
//            new Actions(driver).dragAndDrop(entrada, busqueda).perform();

        }catch (Exception e){
            Assert.fail(String.format("Generic fail test %s", e.getMessage()));
        }
    }
}
