package ui;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class RedBusCalendarTest {

	WebDriver driver = null;

	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		options.addArguments("--remote-allow-origins=*");
		options.addArguments("--disable-notifications");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
		driver.get("https://www.redbus.in/");
	}

	@AfterTest
	public void tearDown() {
		driver.quit();
	}

	@Test
	public void calendarTest() throws InterruptedException {
//		System.out.println(getWeekends("Mar 2024"));
		System.out.println(getWeekends("Mar 2025"));
	}

	public List<String> getWeekends(String yearMonth) throws InterruptedException {
		WebElement travelDateEl = driver.findElement(By.id("onwardCal"));
		travelDateEl.click();
		while (true) {
			WebElement yearMonthEl = driver
					.findElement(By.xpath("//div[contains(@class,'DayNavigator__IconBlock')][2]"));
			String currentYearMonth = yearMonthEl.getText().split("\n")[0];
			System.out.println(currentYearMonth + ": ");
			try {
				WebElement holidayCountEl = driver.findElement(By.className("holiday_count"));
				System.out.println(holidayCountEl.getText());
			} catch (NoSuchElementException e) {
				System.out.println("No Holidays");
			}

			if (currentYearMonth.trim().equals(yearMonth)) {
				break;
			} else {
				driver.findElement(By.xpath("//div[contains(@class,'DayNavigator__IconBlock')][last()]")).click();
			}
		}

		Thread.sleep(1000);

		List<String> weekendList = driver.findElements(By.xpath(
				"//span[contains(@class,'DayTilesWrapper__SpanContainer')]//div/span[contains(@class,'bwoYtA')]"))
				.stream().map(el -> el.getText()).collect(Collectors.toList());
		return weekendList;

	}
}
