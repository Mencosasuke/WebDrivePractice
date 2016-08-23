package DavidMencos.Tests;

import DavidMencos.Bean.Driver;
import DavidMencos.Helpers.AuxTestMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created by David Fernando on 8/18/2016.
 */
public class ExerciseTwo extends Driver {

    @DataProvider(name = "makeNModelData")
    public Object[][] getMakeNModelTableData() throws Exception {
        return AuxTestMethods.getTableArray("resources/datasources/TestData.xlsx", "MnM");
    }

    @Test(dataProvider = "makeNModelData")
    public void MakeNModelSearchTest(String make, String model){
        try {
            driver.get("http://www.autoweb.com");
            // Selects the Make & Model menu
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'src-home__bar-list__container')]/ul/li[contains(@class, 'src-home__bar-list__item')]/a/span[contains(@class, 'icon-makemodel')]"))).click();
            // Clicks on the Make drop down list to make it visible
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@id, 'aw-dropdown-makes')]"))).click();
            // Gets the Make drop down list and clicks the desired Make
            AuxTestMethods.ClickElementsJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//div[contains(@id, 'make-dropdown')]/ul/li/a[contains(@data-aw-make, '%s')]", make)))));
            // Gets the Model drop down list and clicks the desired Model
            AuxTestMethods.ClickElementsJS(driver, wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//div[contains(@id, 'model-dropdown')]/ul/li/a[contains(@data-aw-model, '%s')]", model)))));
            AuxTestMethods.ClickTrimModelVariationMessage(driver);
            WebElement makeTagFound;
            try{
                // Looks for the Make active tag and clicks on it
                makeTagFound = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@id, 'content-activeFilters')]/div[contains(@data-aw-make, '%s')]", make))));
                makeTagFound.click();
                // Looks for the active models in the Make filter
                WebElement activeFilters = shortWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@id, 'filterOptions')]", model))));
                try{
                    // Looks for the model selected
                    activeFilters.findElement(By.xpath(String.format("div[contains(@data-aw-model, '%s')]", model)));
                }catch (NoSuchElementException e){
                    Assert.fail(String.format("The model tag %s doesn't shows.", model));
                }
            }catch(Exception e){
                Assert.fail(String.format("The make tag %s doesn't shows.", make));
            }
        }catch (Exception e){
            Assert.fail(String.format("Generic fail %s", e.getMessage()));
        }
    }
}
