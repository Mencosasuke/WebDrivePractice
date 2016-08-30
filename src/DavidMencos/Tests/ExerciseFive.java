package DavidMencos.Tests;

import DavidMencos.Bean.Driver;
import DavidMencos.Helpers.AuxTestMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.internal.Streams;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

/**
 * Created by David Fernando on 8/30/2016.
 */
public class ExerciseFive extends Driver{
    @Test
    public void ComparePageVerifyInfo(){
        try {
            int numberOfCarsToCompare = new Random().nextInt(7) + 2;
            String[] selectedIdCars = new String[numberOfCarsToCompare];
            int[] selectedIndex = new int[numberOfCarsToCompare];
            int index = 0;
            Boolean flagCarAlreadySelected;

            // Set array to match car index of more than 0
            for(int i = 0; i < selectedIndex.length; i++){
                selectedIndex[i]--;
            }

            driver.get("http://www.autoweb.com/search");
            AuxTestMethods.waitForPageLoad(driver);
            AuxTestMethods.ClickTrimModelVariationMessage(driver);

            // Select to the model view, if trim view is selected by default
            if(driver.findElement(By.xpath("//span[contains(@id, 'trimSwitchView')]")).getAttribute("class").contains("text--active")){
                AuxTestMethods.ClickElementsJS(driver, wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'changeVilSwitch')]"))));
                AuxTestMethods.ClickTrimModelVariationMessage(driver);
            }

            System.out.println(String.format("Random number of cars to compare: %d", numberOfCarsToCompare));
            Thread.sleep(500);

            // Select the random cars to compare
            List<WebElement> carsShowedInPage = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[contains(@id, 'allResults')]/div[contains(@class, 'src-vil__section')]/div[contains(@class, 'src-vil__section-content')]/div")));

            do{
                int randomCarToSelect = new Random().nextInt(carsShowedInPage.size());
                flagCarAlreadySelected = false;

                for(int j = 0; j < selectedIndex.length; j++){
                    if(selectedIndex[j] == randomCarToSelect){
                        flagCarAlreadySelected = true;
                        break;
                    }
                }
                if(!flagCarAlreadySelected){
                    selectedIndex[index] = randomCarToSelect;
                    selectedIdCars[index] = carsShowedInPage.get(randomCarToSelect).getAttribute("id").replaceAll("vehicle-", "");

                    System.out.println(String.format("ID of selected car: %s", selectedIdCars[index]));

                    WebElement selectedCar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//div[contains(@id, '%s')]/div[contains(@class, 'src-vil__box-footer')]/div[contains(@class, 'src-vil__box-footer__content')]/a[contains(@data-vehicle-add, '%s')]", carsShowedInPage.get(randomCarToSelect).getAttribute("id"), selectedIdCars[index]))));
                    AuxTestMethods.ClickElementsJS(driver, selectedCar);
                    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[contains(@id, 'compareButton')]/span[contains(@class, 'number totalCompareVehicles')]"), String.valueOf(index + 1)));
                    index++;
                }
            }while(index < numberOfCarsToCompare);

            AuxTestMethods.ClickElementsJS(driver, driver.findElement(By.xpath("//div[contains(@id, 'compareButton')]")));
            AuxTestMethods.ClickElementsJS(driver, wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'compareNow')]"))));

            // Scroll the page until new information appears
            Thread.sleep(500);
            AuxTestMethods.waitForPageLoad(driver);
            ((JavascriptExecutor) driver).executeScript("document.getElementById('price').scrollIntoView({block: 'start', behavior: 'smooth'});");

            Hashtable<String, String> carDetailInfo = new Hashtable<>();
            List<WebElement> carsSelectedToCompare = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@id, 'scrollContainerInner')]/div")));

            for (WebElement car : carsSelectedToCompare) {
                List<WebElement> detailSpans =  car.findElements(By.xpath("div[contains(@class, 'src-vil__box hidden-xs')]/div[contains(@class, 'src-vil__box-content')]/div[contains(@class, 'src-vil__box-style')]/span"));
                String carItemDetails = "";
                for (WebElement detail : detailSpans) {
                    carItemDetails = String.format("%s %s", carItemDetails, detail.getText());
                }
                carDetailInfo.put(car.getAttribute("data-vehicle-id"), carItemDetails.trim());
            }

            Hashtable<String, String> carDisplayedInfo = new Hashtable<>();
            List<WebElement> carsDisplayed = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@id, 'topCarBitsContainer')]/div")));

            for (WebElement car : carsDisplayed) {
                WebElement detailDiv = car.findElement(By.xpath("div[contains(@class, 'cp-topCarContainer')]/div[contains(@class, 'cp-topTextContainer')]/div[contains(@class, 'cp-topTextModel')]"));
                String detailSpans = detailDiv.getText().replaceAll("[\\t\\n\\r]"," ").trim();
                carDisplayedInfo.put(car.getAttribute("data-vehicle-id"), detailSpans);
            }

            for (String id : selectedIdCars) {
                System.out.println(String.format("Car ID %s on WebPage:      ###%s###", id, carDetailInfo.get(id)));
                System.out.println(String.format("Car ID %s displayed above: ###%s###", id, carDisplayedInfo.get(id)));
                if(!carDetailInfo.get(id).equals(carDisplayedInfo.get(id))){
                    Assert.fail(String.format("The data showed with the data displayed above isn't equal for the car %s", id));
                }
            }

        }catch (Exception e){
            Assert.fail(String.format("Generic fail.%s", e.getMessage()));
        }
    }
}
