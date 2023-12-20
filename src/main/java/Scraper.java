import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManagerOutput;
import org.openqa.selenium.remote.service.DriverFinder;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Scraper {

    public static void main(String[] args) throws InterruptedException {

        //Login
        ChromeDriverService service = new ChromeDriverService.Builder().build();
        WebDriver driver  = new ChromeDriver(service);

        loginAndNavigateToPage(driver);
        //Wait and find messagelist
        List<Post> posts = new ArrayList<Post>();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        new WebDriverWait(driver, Duration.ofSeconds(20)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                int elementCount = driver.findElements(By.xpath("//div[contains(@class, 'messages')]/div")).size();
                if (elementCount > 1)
                    return true;
                else
                    return false;
            }
        });

        js.executeScript("document.querySelector(\"#message-list\").scrollBy(0,500);", "");

        while(posts.size() < 200) {
            try{

                new WebDriverWait(driver, Duration.ofSeconds(20)).until(new ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        int elementCount = driver.findElements(By.xpath("//div[contains(@class, 'messages')]/div")).size();
                        if (elementCount > 1)
                            return true;
                        else
                            return false;
                    }
                });

                List<WebElement> messageDivList = null;

                messageDivList = driver.findElements(By.xpath("//div[contains(@class, 'messages')]/div"));

                for (int j = messageDivList.size() - 1; j > 0 ; j--) {
                    WebElement currentElementDiv = messageDivList.get(j).findElement(By.tagName("div"));
                    //If it is a date line element e.g: ----- 21 March 2023 -----
                    String currentElementDivId = currentElementDiv.getAttribute("id");

                    if(currentElementDivId.equals("")){
                        continue;
                    }

                    //If it is an empty section (like an image) - no text
                    if(currentElementDiv.findElement(By.tagName("section")).getText().equals("")){
                        continue;
                    }

                    //Only relevant elements now
                    WebElement imageDiv;
                    WebElement contentSection;

                    //Image div and Section isolated
                    imageDiv = currentElementDiv.findElements(By.tagName("div")).get(0);
                    contentSection = currentElementDiv.findElement(By.tagName("section"));
                    List<WebElement> elementsInsideUsernameDateSection = contentSection.findElements(By.xpath("*"));

                    String author = "";
                    String timestamp = "";
                    String replyTo = "";
                    String body = "";
                    int engagement = 0;

                    //Single message - no username
                    if(imageDiv.findElement(By.tagName("span")).findElements(By.tagName("img")).size() < 1){
                        body = elementsInsideUsernameDateSection.get(0).getText();
                        //With engagement
                        if(elementsInsideUsernameDateSection.size() > 1){
                            //Engagement
                            engagement = findSum(elementsInsideUsernameDateSection.get(1).getText());
                            Post tmp = new Post(body, engagement);
                            if(!posts.contains(tmp)){
                                posts.add(tmp);
                            }
                            continue;
                        }
                        //Without engagement

                        Post tmp = new Post(body);
                        if(!posts.contains(tmp)){
                            posts.add(tmp);
                        }
                        continue;
                    }

                    List<WebElement> usernameTimeStampDiv = contentSection.findElements(By.xpath("div"));
                    List<WebElement> usernameTimeStampSpans = usernameTimeStampDiv.get(0).findElements(By.xpath("span"));
                    author = usernameTimeStampSpans.get(0).getText();
                    timestamp = usernameTimeStampSpans.get(1).getText();
                    body = contentSection.findElements(By.xpath("span")).get(0).getText();

                    if(currentElementDiv.getText().contains("In reply to")){
                        //If it is an In reply to
                        replyTo = currentElementDiv.findElements(By.tagName("button")).get(0).getText();
                    }
                    List<WebElement> classGapDivs = contentSection.findElements(By.xpath("div[\"@class=gap-2\"]"));
                    if(classGapDivs.size() > 1){
                        engagement = findSum(contentSection.findElements(By.xpath("div[\"@class=gap-2\"]")).get(classGapDivs.size()-1).getText());
                    }
                    Post tmp = new Post(author,timestamp,body,replyTo,engagement);
                    List<WebElement> usernameAndTimeStamp = elementsInsideUsernameDateSection.get(0).findElements(By.xpath("*"));
                    if(!posts.contains(tmp)){
                        posts.add(tmp);
                    }
                }

            }catch (StaleElementReferenceException e){
                continue;
            }

            //Scrolling simulation
            js.executeScript("document.querySelector(\"#message-list\").scroll(0,15);", "");
            js.executeScript("document.querySelector(\"#message-list\").scrollBy(0,300);", "");
        }
        System.out.println(posts.size());
        ExcelStuff.writeToExcel(posts);
    }

    static void loginAndNavigateToPage(WebDriver driver){
        driver.get("https://app.jointherealworld.com/");

        new WebDriverWait(driver, Duration.ofSeconds(7)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div/main/div[2]/div/div[2]/div[2]/a/button")));
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/div[2]/div/div[2]/div[2]/a/button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(7)).until(ExpectedConditions.elementToBeClickable(By.id("email")));
        driver.findElement(By.id("email")).sendKeys("nicklasfrederiksen@gmail.com");
        driver.findElement(By.id("password")).sendKeys("4585@Matrix");
        driver.findElement(By.xpath("//*[@id=\"root\"]/div/main/div[2]/div[2]/div/div/form/button")).click();
        new WebDriverWait(driver, Duration.ofSeconds(8));

        //Navigate to Hall of shame
        driver.get("https://app.jointherealworld.com/chat/01GGDHJAQMA1D0VMK8WV22BJJN/01GNQZG8WJS3B0Y29REVE29PHH");
        //Navigate to Daily motivation
        //driver.get("https://app.jointherealworld.com/chat/01GGDHJAQMA1D0VMK8WV22BJJN/01GNQXVC8X4S6GQ6GS8ZQS48WR");
        //navigate to daily check-in
        //driver.get("https://app.jointherealworld.com/chat/01GGDHJAQMA1D0VMK8WV22BJJN/01GNQXTX6NXBGV034MSJ0JED3N");
        //Navigate to PM Challenge chat
        //driver.get("https://app.jointherealworld.com/chat/01GGDHJAQMA1D0VMK8WV22BJJN/01GNQZAH7XA7PFWEGMA360S9JA");

        new WebDriverWait(driver, Duration.ofSeconds(7)).until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"message-list\"]")));
    }

    static int findSum(String str)
    {
        if(str.equals("")){
            return 0;
        }
        // A temporary string
        String temp = "0";

        // holds sum of all numbers present in the string
        int sum = 0;

        // read each character in input string
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            // if current character is a digit
            if (Character.isDigit(ch))
                temp += ch;

                // if current character is an alphabet
            else {
                // increment sum by number found earlier
                // (if any)
                sum += Integer.parseInt(temp);

                // reset temporary string to empty
                temp = "0";
            }
        }

        // atoi(temp.c_str()) takes care of trailing
        // numbers
        return sum + Integer.parseInt(temp);
    }
}
