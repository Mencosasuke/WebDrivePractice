package DavidMencos.Tests;

import DavidMencos.Bean.Driver;
import DavidMencos.Helpers.AuxTestMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

/**
 * Created by David Fernando on 8/29/2016.
 */
public class ExerciseFour extends Driver {

    @Test
    public void CompareCarsTest(){
        try {
            int numberOfCarsToCompare = new Random().nextInt(7) + 2;
            String[] selectedIdCars = new String[numberOfCarsToCompare];
            int[] selectedIndex = new int[numberOfCarsToCompare];
            int index = 0;
            Boolean flagCarAlreadySelected;

            // Set array to match car index of more than 0
            for(int i = 0; i < selectedIndex.length; i++){
                selectedIndex[i]--;
//            System.out.println(String.format("selectedIndex[%d] = %d", i, selectedIndex[i]));
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
//            System.out.println(String.format("Index of car that want to be selected: %d", randomCarToSelect));

                for(int j = 0; j < selectedIndex.length; j++){
                    if(selectedIndex[j] == randomCarToSelect){
                        flagCarAlreadySelected = true;
//                    System.out.println(String.format(">>>> The index %d already exist and will don't be added again.", randomCarToSelect));
                        break;
                    }
                }
                if(!flagCarAlreadySelected){
                    selectedIndex[index] = randomCarToSelect;
                    selectedIdCars[index] = carsShowedInPage.get(randomCarToSelect).getAttribute("id").replaceAll("vehicle-", "");

                    System.out.println(String.format("Index of car selected: %d - ID of selected car: %s", randomCarToSelect, selectedIdCars[index]));

                    WebElement selectedCar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format("//div[contains(@id, '%s')]/div[contains(@class, 'src-vil__box-footer')]/div[contains(@class, 'src-vil__box-footer__content')]/a[contains(@data-vehicle-add, '%s')]", carsShowedInPage.get(randomCarToSelect).getAttribute("id"), selectedIdCars[index]))));
                    AuxTestMethods.ClickElementsJS(driver, selectedCar);
//                selectedCar.findElement(By.xpath(String.format("div[contains(@class, 'src-vil__box-footer')]/div[contains(@class, 'src-vil__box-footer__content')]/a[contains(@data-vehicle-add, '%s')]", selectedIdCars[index]))).click();
//                wait.until(ExpectedConditions.attributeContains(By.xpath(String.format("//div[contains(@id, 'vehicle-%s')]", selectedIdCars[index])), "class", "src-vil__box--added"));
//                System.out.println(String.format(">>> Class 'src-vil__box--added' found for carID %s", selectedIdCars[index]));
                    wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[contains(@id, 'compareButton')]/span[contains(@class, 'number totalCompareVehicles')]"), String.valueOf(index + 1)));
                    System.out.println(String.format(">>> Number of selected cars: %d", index + 1));
                    index++;
                }
            }while(index < numberOfCarsToCompare);

            AuxTestMethods.ClickElementsJS(driver, driver.findElement(By.xpath("//div[contains(@id, 'compareButton')]")));
            AuxTestMethods.ClickElementsJS(driver, wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'compareNow')]"))));

            // Verify that all cars selected are showed in the compare webpage

            List<WebElement> carsSelectedToCompare = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@id, 'scrollContainerInner')]/div")));

            System.out.println(String.format("*** Number of cars in webpage: %d | Number of selected cars: %d", carsSelectedToCompare.size(), numberOfCarsToCompare));

            if(carsSelectedToCompare.size() != numberOfCarsToCompare){
                Assert.fail(String.format("### Number of cars in webpage (%d) is not the same of the number of cars selected (%d)", carsSelectedToCompare.size(), numberOfCarsToCompare));
            }

            for (WebElement car : carsSelectedToCompare) {
                boolean allCarsFound = true;
                String id = car.getAttribute("data-vehicle-id");
                for(int j = 0; j < selectedIdCars.length; j++){
                    if(selectedIdCars[j].equals(id)){
                        allCarsFound = true;
                        System.out.println(String.format(">>> selectedIdCars[%d] %s == current ID %s", j, selectedIdCars[j], id));
                        break;
                    }
                    allCarsFound = false;
                }
                if(!allCarsFound){
                    Assert.fail(String.format("The ID car %s must be present and it isn't.", id));
                }
            }
        }catch (Exception e){
            Assert.fail(String.format("Generic fail.%s", e.getMessage()));
        }
    }
}
