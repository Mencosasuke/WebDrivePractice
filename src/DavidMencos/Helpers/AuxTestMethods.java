package DavidMencos.Helpers;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

/**
 * Created by David Fernando on 8/18/2016.
 */
public class AuxTestMethods {

    private static XSSFSheet ExcelWSheet;
    private static XSSFWorkbook ExcelWBook;
    private static XSSFCell Cell;

    public static void waitForPageLoad(WebDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver wdriver) {
                    return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                }
            });
        } catch (Exception e) {
            Assert.fail("Problem waiting for page load.");
        }
    }

    public static void ClickTrimModelVariationMessage(WebDriver driver){
        try{
            WebDriverWait wait = new WebDriverWait(driver, 4, 1);
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@id, 'modalSwitch')]/div[contains(@class, 'content-modal')]/div[contains(@class, 'modal-button')]/a/span[contains(@class, 'aw__button__text')]"))).click();
            new WebDriverWait(driver, 3, 1).until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver wdriver){
                    return driver.findElement(By.xpath("//div[contains(@id, 'modalSwitch')]")).getCssValue("display").equals("none");
                }
            });
        }catch (Exception e){}
    }

    public static void ClickElementsJS(WebDriver driver, WebElement element){
        try{
            JavascriptExecutor jsExecutor = (JavascriptExecutor)driver;
            jsExecutor.executeScript("arguments[0].click()", element);
        }catch(Exception e){
            System.out.println(String.format("Cannot click on non visible element %s.", element.getTagName()));
        }
    }

    public static Object[][] getTableArray(String FilePath, String SheetName) throws Exception {
        String[][] tabArray = null;
        try {
            ExcelWBook = new XSSFWorkbook(new FileInputStream(FilePath));
            ExcelWSheet = ExcelWBook.getSheet(SheetName);
            int startRow = 1;
            int startCol = 0;
            int ci,cj;
            int totalRows = ExcelWSheet.getLastRowNum();
            short minColIx = ExcelWSheet.getRow(startRow).getFirstCellNum();
            short maxColIx = ExcelWSheet.getRow(startRow).getLastCellNum();
            int totalCols = maxColIx - minColIx;
            tabArray = new String[totalRows][totalCols];
            ci = 0;
            for (int i = startRow; i <= totalRows; i++, ci++) {
                cj = 0;
                for (int j = startCol; j < totalCols; j++, cj++){
                    ExcelWSheet.getRow(i).getCell(j).setCellType(Cell.CELL_TYPE_STRING);
                    tabArray[ci][cj] = getCellData(i, j);
//                    System.out.println(tabArray[ci][cj]);
                }
            }
        } catch (FileNotFoundException e){
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        } catch (IOException e){
            System.out.println("Could not read the Excel sheet");
            e.printStackTrace();
        }
        return(tabArray);
    }

    public static String getCellData(int RowNum, int ColNum) throws Exception {
        try {
            Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
            int dataType = Cell.getCellType();
            if (dataType == 3) {
                return "";
            } else {
                String CellData = Cell.getStringCellValue();
                return CellData;
            }
        } catch (Exception e){
                System.out.println(e.getMessage());
                throw (e);
        }
    }
}
