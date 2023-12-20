import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class Test {


    public static ChromeDriver getChromeDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(chromeOptions);
    }

    public static void main(String[] args) throws InterruptedException {
    }

    static void loginAndNavigateToPage(WebDriver driver){
        driver.get("https://app.jointherealworld.com/chat/01GGDHJAQMA1D0VMK8WV22BJJN/01GNQXVC8X4S6GQ6GS8ZQS48WR");
        new WebDriverWait(driver, Duration.ofSeconds(7)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/main/div[2]/div/div[2]/div[2]/a/button")));
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/div[2]/div/div[2]/div[2]/a/button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(7)).until(ExpectedConditions.elementToBeClickable(By.id("email")));
        driver.findElement(By.id("email")).sendKeys("nicklasfrederiksen@gmail.com");
        driver.findElement(By.id("password")).sendKeys("4585@Matrix");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/div[2]/div[2]/div/div/form/button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(8));

        //Navigate to Hall of shame
        driver.get("https://app.jointherealworld.com/chat/01GGDHJAQMA1D0VMK8WV22BJJN/01GNQZG8WJS3B0Y29REVE29PHH");
    }

    static void countPosts(){
        //Login
        WebDriver driver = getChromeDriver();
        driver.get("file:///Users/dean/Documents/Real%20World%20Portal.html");

        //Wait and find messagelist
        WebElement messageList = driver.findElement(By.xpath("//*[@id=\"message-list\"]"));


        //Div elements
        List<WebElement> messagesDivsAsList = messageList.findElements(By.xpath("*"));

        int count = 0;

        System.out.println(messagesDivsAsList.size());

        for (int i = 0; i < messagesDivsAsList.size(); i++) {
            WebElement currentElementDiv = messagesDivsAsList.get(i);
            String currentElementDivId = currentElementDiv.getAttribute("id");

            if(currentElementDivId.equals("")){
                continue;
            }

            if(currentElementDiv.findElement(By.tagName("section")).getText().equals("")){
                continue;
            }
            count++;
        }
        System.out.println(count + "Posts without date lines");
    }

    static void writeToExcel(List<Post> posts){
        Workbook workbook = new XSSFWorkbook();
        Sheet dataSheet = workbook.createSheet("data");
        Row header = dataSheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = ((XSSFWorkbook) workbook).createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        headerStyle.setFont(font);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue("Name");
        headerCell.setCellStyle(headerStyle);

        headerCell = header.createCell(1);
        headerCell.setCellValue("Age");
        headerCell.setCellStyle(headerStyle);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        Row row = dataSheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue("John Smith");
        cell.setCellStyle(style);

        cell = row.createCell(1);
        cell.setCellValue(20);
        cell.setCellStyle(style);

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "temp.xlsx";

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
