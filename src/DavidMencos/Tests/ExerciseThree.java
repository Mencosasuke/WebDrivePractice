package DavidMencos.Tests;

import DavidMencos.Bean.Driver;
import DavidMencos.Helpers.AuxTestMethods;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
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
            driver.get("http://www.autoweb.com/search");
            AuxTestMethods.waitForPageLoad(driver);
            AuxTestMethods.ClickTrimModelVariationMessage(driver);
            // Moves the Ideal Monthly Payment to select the desired value
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@id, 'financePayment')]"))).click();
            WebElement upperHandler = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'financeSlider')]/div[contains(@class, 'noUi-base')]/div[contains(@class, 'noUi-origin noUi-background')]/div[contains(@class, 'noUi-handle-upper')]")));
            AuxTestMethods.ClickElementsJS(driver, upperHandler);
            this.movePaymentSlide(upperHandler, "max", 600);
            WebElement lowerHandler = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'financeSlider')]/div[contains(@class, 'noUi-base')]/div[contains(@class, 'noUi-origin noUi-connect')]/div[contains(@class, 'noUi-handle-lower')]")));
            AuxTestMethods.ClickElementsJS(driver, lowerHandler);
            this.movePaymentSlide(lowerHandler, "min", 400);
            // Select to the model view, if trim view is selected by default
            if(driver.findElement(By.xpath("//span[contains(@id, 'trimSwitchView')]")).getAttribute("class").contains("text--active")){
                AuxTestMethods.ClickElementsJS(driver, driver.findElement(By.xpath("//div[contains(@id, 'changeVilSwitch')]")));
                AuxTestMethods.ClickTrimModelVariationMessage(driver);
            }
            // Waits for the page is loaded
//            Thread.sleep(1000);
            AuxTestMethods.waitForPageLoad(driver);
            // Begins the loop on the page to find all the cars that have to be present
            int totalCarsFound = Integer.valueOf(driver.findElement(By.xpath("//span[contains(@id, 'totalResults')]")).getText());
            int vehiclesCount = 0;
            WebElement resultsNode = wait.until(ExpectedConditions.visibilityOfElementLocated((By.xpath("//div[contains(@id, 'allResults')]/div/div[contains(@class, 'src-vil__section-content')]"))));
            WebElement div = resultsNode.findElement(By.xpath("div"));
            String id = "";
            do{
                while(div != null){
                    try {
                        id = div.getAttribute("id");
                        System.out.println(id);
                        vehiclesCount++;
                        div = div.findElements(By.xpath("following-sibling::div")).get(0);
                    }catch (Exception e){
                        div = null;
                    }
                }
                ((JavascriptExecutor) driver).executeScript(String.format("document.getElementById('%s').scrollIntoView({block: 'start', behavior: 'smooth'});", id));
                System.out.println(String.format("Cars found: %d. Totals cars found: %d.", vehiclesCount, totalCarsFound));
                Thread.sleep(1000);
//                div = driver.findElement(By.xpath(String.format("//div[contains(@id, '%s')]/following-sibling::div", id)));
                try{
                    div = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(String.format("//div[contains(@id, '%s')]/following-sibling::div", id))));
                }catch (Exception e){
                    Assert.assertEquals(vehiclesCount, totalCarsFound);
                }
            }while(vehiclesCount < totalCarsFound);

            System.out.println(String.format(">>>> Total cars found: %d. Totals cars count: %d.", vehiclesCount, totalCarsFound));

        }catch (Exception e){
            Assert.fail(String.format("Generic fail test %s", e.getMessage()));
        }
    }

    /**
     * Moves the Ideal Monthly Payment to select the desired value.
     * @param element Element to move
     * @param direction "max" if want to move the max pay range | "min" if want to move the min pay range
     * @param value pay range limit
     */
    private void movePaymentSlide(WebElement element, String direction, int value){
        try{
            Point p = element.getLocation();
            int x = p.getX();
            int y = p.getY();

            Dimension d = element.getSize();
            int h = d.getHeight();
            int w = d.getWidth();

            Robot robot = new Robot();
            robot.mouseMove(x + (w / 2), y + (h / 2) + 100);
            robot.mousePress(InputEvent.BUTTON1_MASK);

            String[] payRange = new String[2];
            String payMinRange;
            String payMaxRange;

            switch (direction){
                case "max":
                    do{
                        x += 1;
                        robot.mouseMove(x + (w / 2), y + (h / 2) + 100);
                        payMaxRange = driver.findElement(By.xpath("//div[contains(@id, 'financeLowValue')]")).getText().replaceAll("\\$|<|\\+|\\s", "").split("-")[1];
                    }while(!payMaxRange.equals(String.valueOf(value)));
                    break;
                case "min":
                    do{
                        x += 1;
                        robot.mouseMove(x + (w / 2), y + (h / 2) + 100);
                        payMinRange = driver.findElement(By.xpath("//div[contains(@id, 'financeLowValue')]")).getText().replaceAll("\\$|<|\\+|\\s", "").split("-")[0];
                    }while(!payMinRange.equals(String.valueOf(value)));
                    break;
            }
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }catch (Exception e){
            Assert.fail("Error trying to move slide.");
        }
    }
}
