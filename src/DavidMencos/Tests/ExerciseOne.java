package DavidMencos.Tests;

import DavidMencos.Bean.Driver;
import DavidMencos.Helpers.AuxTestMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by David Fernando on 8/18/2016.
 */
public class ExerciseOne extends Driver {

    @Test
    public void VerifyVehicleTypeTag(){
        try{
            String carTypeTagActive;
            String selectedCarType;
            WebElement spanCarTypeFound;
            WebElement anchorCarTypeFound;
            driver.get("http://www.autoweb.com");
            AuxTestMethods.waitForPageLoad(driver);
            // Get the number of car types on the page
            int numberOfCarTypesFound = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'src-home__section-content')]/div/a/span[contains(@class, 'src-home__section-item__text')]"))).size();
            // For each type, make a click on it
            for(int i = 0; i < numberOfCarTypesFound; i++){
                // Get the link of the category and click it
                anchorCarTypeFound = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'src-home__section-content')]/div/a"))).get(i);
                spanCarTypeFound = anchorCarTypeFound.findElement(By.xpath("span[contains(@class, 'src-home__section-item__text')]"));
                selectedCarType = anchorCarTypeFound.getAttribute("title");
                System.out.println(String.format("Test will click the type \"%s\".", selectedCarType));
                AuxTestMethods.ClickElementsJS(driver, spanCarTypeFound);
//                spanCarTypeFound.click();
                AuxTestMethods.waitForPageLoad(driver);
                AuxTestMethods.ClickTrimModelVariationMessage(driver);
                // Verify that the active filter corresponds to the selected car type, and then, go back to te main page
                carTypeTagActive = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@id, 'content-activeFilters')]/div[contains(@data-aw-filter-value, '%s')]", selectedCarType)))).getText();
                Assert.assertEquals(selectedCarType.toLowerCase(), carTypeTagActive.toLowerCase());
                driver.navigate().back();
            }
        }catch(Exception e) {
            Assert.fail(String.format("Generic fail %s", e.getMessage()));
        }
    }
}
