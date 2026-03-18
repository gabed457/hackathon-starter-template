package hackathon;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.*;

/**
 * Selenium UI tests for the hackathon application.
 *
 * Prerequisites:
 *   - The Next.js app must be running: cd app && npm run dev
 *   - Chrome must be installed
 *
 * Run with: cd tests && mvn test
 */
public class UITest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void homePageShowsBothSections() {
        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("h1")));

        String heading = driver.findElement(By.tagName("h1")).getText();
        assertEquals(heading, "Hackathon");

        var sections = driver.findElements(By.tagName("section"));
        assertTrue(sections.size() >= 2, "Expected at least 2 sections on the page");

        String pageText = driver.findElement(By.tagName("main")).getText();
        assertTrue(pageText.contains("Pre-computed Predictions"));
        assertTrue(pageText.contains("Live ONNX Prediction"));
    }

    @Test
    public void preComputedPredictionsShowData() {
        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("li")));

        var items = driver.findElements(By.cssSelector("section:first-of-type li"));
        assertEquals(items.size(), 3, "Expected 3 pre-computed predictions");
        assertTrue(items.get(0).getText().contains("Alice"));
    }

    @Test
    public void livePredictionReturnsHighForHighScore() {
        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='number']")));

        WebElement input = driver.findElement(By.cssSelector("input[type='number']"));
        input.clear();
        input.sendKeys("0.9");

        driver.findElement(By.cssSelector("section:nth-of-type(2) button")).click();

        WebElement result = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("section:nth-of-type(2) pre"))
        );

        String json = result.getText();
        assertTrue(json.contains("\"prediction\": 1"), "Expected prediction 1 for score 0.9");
        assertTrue(json.contains("\"label\": \"high\""), "Expected label 'high' for score 0.9");
    }

    @Test
    public void livePredictionReturnsLowForLowScore() {
        driver.get("http://localhost:3000");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='number']")));

        WebElement input = driver.findElement(By.cssSelector("input[type='number']"));
        input.clear();
        input.sendKeys("0.3");

        driver.findElement(By.cssSelector("section:nth-of-type(2) button")).click();

        WebElement result = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.cssSelector("section:nth-of-type(2) pre"))
        );

        String json = result.getText();
        assertTrue(json.contains("\"prediction\": 0"), "Expected prediction 0 for score 0.3");
        assertTrue(json.contains("\"label\": \"low\""), "Expected label 'low' for score 0.3");
    }
}
