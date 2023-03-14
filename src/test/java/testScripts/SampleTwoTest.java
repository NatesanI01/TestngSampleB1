package testScripts;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.edge.EdgeDriver;
//import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SampleTwoTest {
//  @Parameters("browser1")
//	@Test
  @Test(retryAnalyzer=RetrySampleTest.class)
//  public void cypressSearchTest(String strBrowser) {
  public void cypressSearchTest() {
	  	WebDriver driver=null;
//	  	if(strBrowser.equalsIgnoreCase("edge")) {
//		  	System.setProperty("webdriver.chrome.driver","E:\\WebDrivers\\chromedriver.exe");
			WebDriverManager.edgedriver().setup();
		  	driver=new EdgeDriver();
//	  	}
//	  	WebDriverManager.edgedriver().setup();
//	  	WebDriver driver=new EdgeDriver();
//	  	WebDriverManager.firefoxdriver().setup();
//	  	WebDriver driver=new FirefoxDriver();
		driver.manage().window().maximize();
		driver.get("https://www.google.com/");
		WebElement searchBox =driver.findElement(By.name("q"));
		searchBox.sendKeys("Cypress Tutorial");
//		searchBox.submit();
		searchBox.sendKeys(Keys.ENTER);
		Assert.assertEquals(driver.getTitle(), "Cypress Tutorial - Google Search page");
		driver.close();
  }
}
