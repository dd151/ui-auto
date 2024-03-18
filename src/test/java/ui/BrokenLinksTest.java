package ui;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class BrokenLinksTest {

	WebDriver driver = null;

	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get("https://www.makemytrip.com/flights/");
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void getBrokenLinks() throws IOException {
		List<WebElement> linkElList = driver.findElements(By.tagName("a"));
		for (WebElement linkEl : linkElList) {
			String url = linkEl.getAttribute("href");
			if(url != null && !url.isEmpty()) {
				System.out.println(url);
				HttpURLConnection conn = (HttpURLConnection)(new URL(url).openConnection());
				conn.setRequestMethod("HEAD");
				conn.connect();
				int responseCode = conn.getResponseCode();
				if(responseCode >= 400) {
					System.out.println("BROKEN: " + url);
				}
			}
		}
	}
}
