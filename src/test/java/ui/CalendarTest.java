package ui;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CalendarTest {

	WebDriver driver = null;

	@BeforeTest
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\drivers\\chromedriver.exe");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--incognito");
		options.addArguments("--remote-allow-origins=*");
		driver = new ChromeDriver(options);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
		driver.get("https://www.makemytrip.com/flights/");

	}

	@AfterTest
	public void tearDOwn() {
		driver.quit();
	}

	/*
	 * Navigate to MakeMyTrip. Click on Flights Select a Future departure Date.
	 * Verify the Departure Date is reflected as selected
	 */
	@Test(dataProvider = "calendarDataProvider")
	public void calendarTest(int date, String month, int year) throws InterruptedException {
		selectCalendarDate(date, month, year);

		WebElement departureEl = driver.findElement(By.xpath("//p[@data-cy='departureDate']"));

		WebElement departureDateEl = departureEl.findElement(By.xpath("span[1]"));
		WebElement departureMonthEl = departureEl.findElement(By.xpath("span[2]"));
		WebElement departureYearEl = departureEl.findElement(By.xpath("span[3]"));

		Assert.assertEquals(departureDateEl.getText(), "" + date);
		Assert.assertEquals(departureMonthEl.getText(), month.substring(0, 3));
		Assert.assertEquals(departureYearEl.getText(), new String("" + year).substring(2));
	}

	public void selectCalendarDate(int date, String month, int year) throws InterruptedException {
		String expectedMonthYearTxt = month + " " + year;

		WebElement departureEl = driver.findElement(By.xpath("//div[contains(@class, 'dates')][1]"));
		departureEl.click();
		WebElement currentMonthYear = driver
				.findElement(By.xpath("(//div[contains(@class, 'DayPicker-Caption')])[1]//div"));

		while (true) {
			if (currentMonthYear.getText().equals(expectedMonthYearTxt)) {
				break;
			} else {
				driver.findElement(By.xpath("//span[contains(@class,'--next')]")).click();
				// Thread.sleep(500);
			}
		}
		Thread.sleep(1000);
		List<WebElement> dateElList = driver.findElements(
				By.xpath("(//div[contains(@class,'DayPicker-Body')])[1]//div[@class='dateInnerCell']//p[1]"));
		for (WebElement dateEl : dateElList) {
			if (Integer.parseInt(dateEl.getText()) == date) {
				dateEl.click();
				break;
			}
		}
	}

	@DataProvider
	public Object[][] calendarDataProvider() {
		return new Object[][] { { 21, "June", 2024 }, { 13, "September", 2024 }, { 1, "January", 2025 } };
	}
}
