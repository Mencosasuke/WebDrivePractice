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
            int numberOfCarTypesFound = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'src-home__section-content')]/div/a/span[contains(@class, 'src-home__section-item__text')]"))).size();
            for(int i = 0; i < numberOfCarTypesFound; i++){
                anchorCarTypeFound = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@class, 'src-home__section-content')]/div/a"))).get(i);
                spanCarTypeFound = anchorCarTypeFound.findElement(By.xpath("span[contains(@class, 'src-home__section-item__text')]"));
                selectedCarType = anchorCarTypeFound.getAttribute("title");
                System.out.println(String.format("Test will click the type \"%s\".", selectedCarType));
                spanCarTypeFound.click();
                AuxTestMethods.ClickTrimModelVariationMessage(driver);
                carTypeTagActive = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(String.format("//div[contains(@id, 'content-activeFilters')]/div[contains(@data-aw-filter-value, '%s')]", selectedCarType)))).getText();
                Assert.assertEquals(selectedCarType.toLowerCase(), carTypeTagActive.toLowerCase());
                driver.navigate().back();
            }
        }catch(Exception e) {
            Assert.fail(String.format("Generic fail %s", e.getMessage()));
        }
    }
}
