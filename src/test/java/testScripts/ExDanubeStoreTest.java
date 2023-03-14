package testScripts;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExDanubeStoreTest {
	WebDriver driver;
	Properties temProp;
	ExtentReports reports;
	ExtentSparkReporter spark;
	ExtentTest extentTest;
	@BeforeClass
	public void setupExtent() {
	  reports=new ExtentReports();
	  spark=new ExtentSparkReporter("target\\DanubeStore.html");
	  reports.attachReporter(spark);
	}
	@BeforeTest
	public void setup() throws IOException {
	  WebDriverManager.chromedriver().setup();
	  driver=new ChromeDriver();
	  driver.manage().window().maximize();
	  driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	  temProp=new Properties();
	  String path=System.getProperty("user.dir")+"//src//test//resources//configFiles//danube.properties";
	  FileInputStream obtained=new FileInputStream(path);
	  temProp.load(obtained);	
	}
  @Test(priority=1)
  public void registerTest() throws InvalidFormatException, IOException {
	  extentTest=reports.createTest("Registration Test");
	  driver.get(temProp.getProperty("url"));
	  driver.findElement(By.xpath("//button[contains(text(),'Sign up')]")).click();
	  driver.findElement(By.xpath("//input[@id='s-name']")).sendKeys(temProp.getProperty("name"));
	  driver.findElement(By.xpath("//input[@id='s-surname']")).sendKeys(temProp.getProperty("surname"));
	  driver.findElement(By.xpath("//input[@id='s-email']")).sendKeys(temProp.getProperty("email"));
	  driver.findElement(By.xpath("//input[@id='s-password2']")).sendKeys(temProp.getProperty("password"));
	  driver.findElement(By.xpath("//input[@id='s-company']")).sendKeys(temProp.getProperty("company"));
	  driver.findElement(By.xpath("//label[contains(text(),'Myself')]")).click();
	  driver.findElement(By.xpath("//label[contains(text(),'I would')]")).click();
	  driver.findElement(By.xpath("//label[contains(text(),'I have')]")).click();
	  driver.findElement(By.xpath("//button[contains(text(),'Register')]")).click();
	  Assert.assertEquals(driver.findElement(By.xpath("//div[@id='login-message']")).getText(), "Welcome back, abc01@gmail.com");
  }
  
  @Test(enabled=false)
  public void loginTest() {
	  extentTest=reports.createTest("Login Test");
	  driver.get(temProp.getProperty("url"));
	  driver.findElement(By.id("login")).click();
	  driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(temProp.getProperty("email"));
	  driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(temProp.getProperty("password"));
	  driver.findElement(By.xpath("//button[contains(text(),'Sign In')]")).click();
  }
  @Test(priority=2)
  public void searchItemTest() {
	  extentTest=reports.createTest("Search Item Test");
	  driver.findElement(By.xpath("//input[@name='searchbar']")).sendKeys(temProp.getProperty("search"));
	  driver.findElement(By.xpath("//button[@id='button-search']")).click();
	  Assert.assertEquals(driver.findElement(By.xpath("//div[@class='preview-title']")).getText(),"Celsius 233");
  }
  @Test(priority=3)
  public void checkoutTest() {
	  extentTest=reports.createTest("CheckOut Item Test");
	  driver.findElement(By.xpath("//div[@class='preview-title']")).click();
	  driver.findElement(By.xpath("//button[contains(text(),'Add')]")).click();
	  driver.findElement(By.xpath("//button[contains(text(),'Checkout')]")).click();
	  driver.findElement(By.xpath("//input[@id='s-name']")).sendKeys(temProp.getProperty("name"));
	  driver.findElement(By.xpath("//input[@id='s-surname']")).sendKeys(temProp.getProperty("surname"));
	  driver.findElement(By.xpath("//input[@id='s-address']")).sendKeys(temProp.getProperty("address"));
	  driver.findElement(By.xpath("//input[@id='s-zipcode']")).sendKeys(temProp.getProperty("zipcode"));
	  driver.findElement(By.xpath("//input[@id='s-city']")).sendKeys(temProp.getProperty("city"));
	  driver.findElement(By.xpath("//input[@id='s-company']")).sendKeys(temProp.getProperty("company"));
	  driver.findElement(By.xpath("//label[contains(text(),'as soon')]")).click();
	  driver.findElement(By.xpath("//button[contains(text(),'Buy')]")).click();
	  Assert.assertEquals(driver.findElement(By.xpath("//div[@class='recap']//p")).getText(), "All good, order is on the way. Thank you!");
  }
  @AfterMethod
  public void tearDown(ITestResult result) {
	  if(ITestResult.FAILURE==result.getStatus()) {
		  extentTest.log(Status.FAIL, result.getThrowable().getMessage());
	  }
  }
  @AfterTest
  public void closeTest() {
	  driver.close();
	  reports.flush();
  }
}

