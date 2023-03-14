package testScripts;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SampleOneTest {
	WebDriver driver;
	Properties temProp;
	@Parameters("browser1")
	@BeforeTest
	public void setup(String strBrowser) throws IOException {
//		if(strBrowser.equalsIgnoreCase("chrome")) {
//			WebDriverManager.chromedriver().setup();
//			driver=new ChromeDriver();
//		}
		if(strBrowser.equalsIgnoreCase("edge")) {
			WebDriverManager.edgedriver().setup();
			driver=new EdgeDriver();
		}
		driver.manage().window().maximize();
		temProp=new Properties();
		String path=System.getProperty("user.dir")+"//src//test//resources//configFiles//config.properties";
		FileInputStream obtained =new FileInputStream(path);
		temProp.load(obtained);
	}
	
	
//  @Test(priority=1)
	@Test(alwaysRun=true,dependsOnMethods="seleniumSearchTest")
  public void javaSearchTest() {
//	    System.setProperty("webdriver.chrome.driver","E:\\WebDrivers\\chromedriver.exe");
//		WebDriver driver=new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get("https://www.google.com/");
		driver.get(temProp.getProperty("url"));
		WebElement searchBox =driver.findElement(By.name("q"));
		searchBox.sendKeys("Java Tutorial");
//		searchBox.submit();
		searchBox.sendKeys(Keys.ENTER);
		Assert.assertEquals(driver.getTitle(), "Java Tutorial - Google Search");
//		driver.close();
  }
  
  
  
//  @Test
//  public void seleniumSearchTest() {
//	  	driver.get("https://www.google.com/");
//		driver.get(temProp.getProperty("url"));
//		WebElement searchBox =driver.findElement(By.name("q"));
//		SoftAssert softAssert=new SoftAssert();
//		softAssert.assertEquals(driver.getTitle(), "Google page");
//		searchBox.sendKeys("Selenium Tutorial");
//		Assert.assertEquals(driver.getTitle(), "Google page");
//		searchBox.submit();
//		searchBox.sendKeys(Keys.ENTER);
//		softAssert.assertEquals(driver.getTitle(), "Selenium Tutorial - Google Search");
//		softAssert.assertAll();
//  }
  
  
//  @Test(priority=2)
  @Test
  public void seleniumSearchTest() {
//	  System.setProperty("webdriver.chrome.driver","E:\\WebDrivers\\chromedriver.exe");
//		WebDriver driver=new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get("https://www.google.com/");
	  	driver.get(temProp.getProperty("url"));
	  	WebElement searchBox =driver.findElement(By.name("q"));
		searchBox.sendKeys("Selenium Tutorial");
//		searchBox.submit();
		searchBox.sendKeys(Keys.ENTER);
		Assert.assertEquals(driver.getTitle(), "Selenium Tutorial - Google Search");
//		driver.close();
  }
  
  
//  @Test[priority=-1]
//  @Test(priority=3)
  @Test(enabled=false)
  public void cucumberSearchTest() {
//	  driver.get("https://www.google.com/");
	  	driver.get(temProp.getProperty("url"));
	  	WebElement searchBox =driver.findElement(By.name("q"));
		searchBox.sendKeys("Cucumber Tutorial");
		searchBox.sendKeys(Keys.ENTER);
		Assert.assertEquals(driver.getTitle(), "Cucumber Tutorial - Google Search");
  }
  
  
//  @AfterMethod
  @AfterTest
  public void tearDown() {
	  driver.close();
  }
  
}
