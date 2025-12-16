import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class    selenium_price_comparison {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        // Anti-Bot: Simulate a real user agent
        options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @Test
    public void priceComparisonTest() throws InterruptedException {
        List<Double> priceList = new ArrayList<>();
        // Product: Madame Bovary
        String productName = "Madame Bovary";
        System.out.println("Searching for book: " + productName);

        // --- SITE 1: N11  ---
        System.out.println("Navigating to Site 1 (N11)...");
        try {
            driver.get("https://www.n11.com/arama?q=" + productName.replace(" ", "+"));
            Thread.sleep(3000);

            // Get first price
            WebElement priceElement1 = driver.findElements(By.cssSelector(".newPrice ins")).get(0);
            double price1 = cleanPrice(priceElement1.getText());
            priceList.add(price1);
            System.out.println("N11 Price: " + price1 + " TL");
        } catch (Exception e) {
            System.out.println("N11 Price: Not Found (0.0 TL)");
        }


        // --- SITE 2: KITAPYURDU  ---
        System.out.println("Navigating to Site 2 (Kitapyurdu)...");
        try {
            driver.get("https://www.kitapyurdu.com/index.php?route=product/search&filter_name=" + productName.replace(" ", "+"));
            Thread.sleep(3000);

            // Get first price
            WebElement priceElement2 = driver.findElements(By.cssSelector(".price .value")).get(0);
            double price2 = cleanPrice(priceElement2.getText());
            priceList.add(price2);
            System.out.println("Kitapyurdu Price: " + price2 + " TL");
        } catch (Exception e) {
            System.out.println("Kitapyurdu Price: Not Found (0.0 TL)");
        }


        // --- SITE 3: AMAZON TR  ---
        System.out.println("Navigating to Site 3 (Amazon TR)...");
        try {

            driver.get("https://www.amazon.com.tr/s?k=" + productName.replace(" ", "+"));
            Thread.sleep(3000);

            // Amazon price parsing
            WebElement wholePrice = driver.findElement(By.cssSelector(".a-price-whole"));
            WebElement fractionPrice = driver.findElement(By.cssSelector(".a-price-fraction"));

            String fullPriceText = wholePrice.getText() + "," + fractionPrice.getText();
            double price3 = cleanPrice(fullPriceText);

            priceList.add(price3);
            System.out.println("Amazon Price: " + price3 + " TL");

        } catch (Exception e) {
            System.out.println("Amazon Price: Not Found (Blocked by Bot Protection)");
        }

        // --- REPORT ---
        generateReport(priceList);
    }

    // Helper method to clean price string
    public double cleanPrice(String text) {
        String clean = text
                .replace("TL", "")
                .replace(" ", "")
                .replace("\n", "")
                .replace(".", "")
                .replace(",", ".");
        return Double.parseDouble(clean);
    }

    public void generateReport(List<Double> prices) {
        if (prices.isEmpty()) {
            System.out.println("\nNo prices found to compare.");
            return;
        }

        double min = Collections.min(prices);
        double max = Collections.max(prices);
        double avg = prices.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        System.out.println("\n--- RESULT REPORT ---");
        System.out.println("Cheapest Price     : " + min + " TL");
        System.out.println("Most Expensive Price: " + max + " TL");
        System.out.println("Average Price      : " + String.format("%.2f", avg) + " TL");
        System.out.println("---------------------");


    }

    @AfterClass
    public void tearDown() {
        if(driver != null) driver.quit();
    }
}