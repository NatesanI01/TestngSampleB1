package testScripts;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
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

public class ExBankAccountTest {
  WebDriver driver;
  String accNo;
  ExtentReports reports;
  ExtentSparkReporter spark;
  ExtentTest extentTest;
  @BeforeClass
  public void setupExtent() {
	  reports=new ExtentReports();
	  spark=new ExtentSparkReporter("target\\ParaBank.html");
	  reports.attachReporter(spark);
	}
  @BeforeTest
  public void setup() {
	  WebDriverManager.chromedriver().setup();
	  driver=new ChromeDriver();
	  driver.manage().window().maximize();
  }
  @Test(enabled=true,priority=1)
  public void registerTest() throws InterruptedException {
	  extentTest=reports.createTest("Register Page Test");
	  driver.get("https://parabank.parasoft.com");
	  driver.findElement(By.xpath("//a[contains(text(),'Register')]")).click();
	  driver.findElement(By.id("customer.firstName")).sendKeys("Jhon");
	  driver.findElement(By.id("customer.lastName")).sendKeys("William");
	  driver.findElement(By.id("customer.address.street")).sendKeys("Street");
	  driver.findElement(By.id("customer.address.city")).sendKeys("Dubai");
	  driver.findElement(By.id("customer.address.state")).sendKeys("Dubai");
	  driver.findElement(By.id("customer.address.zipCode")).sendKeys("652645");
	  driver.findElement(By.id("customer.phoneNumber")).sendKeys("8014536780");
	  driver.findElement(By.id("customer.ssn")).sendKeys("1973");
	  driver.findElement(By.id("customer.username")).sendKeys("user9");
	  driver.findElement(By.id("customer.password")).sendKeys("password");
	  driver.findElement(By.id("repeatedPassword")).sendKeys("password");
	  driver.findElement(By.xpath("(//input[@type='submit'])[2]")).click();
	  Thread.sleep(3000);
	  Assert.assertEquals(driver.findElement(By.xpath("//div[@id='rightPanel']//p")).getText(), "Your account was created successfully. You are now logged in.");
  }
  @Test(enabled=false,priority=2)
  public void loginTest() {
	  extentTest=reports.createTest("Login Page Test");
	  driver.get("https://parabank.parasoft.com");
	  driver.findElement(By.xpath("//input[@name='username']")).sendKeys("user8");
	  driver.findElement(By.xpath("//input[@name='password']")).sendKeys("password");
	  driver.findElement(By.xpath("//input[@type='submit']")).submit();
  }
  @Test(priority=3)
  public void createAccountTest() throws InterruptedException {
		extentTest=reports.createTest("Create New Account page Test");
	  	driver.findElement(By.xpath("//a[contains(text(),'Open New')]")).click();
		Select account=new Select(driver.findElement(By.xpath("//select[@id='type']")));
		account.selectByIndex(1);
		Thread.sleep(500);
		driver.findElement(By.xpath("//input[@type='submit']")).click();
		Thread.sleep(3000);
		Assert.assertEquals(driver.findElement(By.xpath("(//div[@class='ng-scope'])[2]//h1")).getText(), "Account Opened!");
		accNo=driver.findElement(By.xpath("//a[@id='newAccountId']")).getText();
  }
  @Test(priority=4,dependsOnMethods="createAccountTest")
  public void accTransactionTest() throws InterruptedException {
	  	extentTest=reports.createTest("Account Transaction Page Test");
	  	driver.findElement(By.xpath("//a[contains(text(),'Transfer Funds')]")).click();
	  	Thread.sleep(3000);
	  	driver.findElement(By.xpath("//input[@name='input']")).sendKeys("200");
	  	Thread.sleep(3000);
	  	Select fromAcc=new Select(driver.findElement(By.id("fromAccountId")));
	  	fromAcc.selectByValue(accNo);
	  	Select toAcc=new Select(driver.findElement(By.id("toAccountId")));
	  	toAcc.selectByIndex(0);
	  	driver.findElement(By.xpath("//input[@type='submit']")).click();
	  	Thread.sleep(3000);
	  	Assert.assertEquals(driver.findElement(By.xpath("//h1")).getText(), "Transfer Complete!");
  }
  @Test(priority=5)
  public void accOverviewTest() throws InterruptedException {
	  extentTest=reports.createTest("Account Overview Page Test");
	  driver.findElement(By.xpath("//a[contains(text(),'Accounts Overview')]")).click();
	  Thread.sleep(3000);
	  List<WebElement> account=driver.findElements(By.xpath("//a[@class='ng-binding']"));
	  Assert.assertEquals(account.get(account.size()-1).getText(), accNo);
	  account.get(account.size()-1).click();
	  Thread.sleep(3000);
	  List<WebElement> debit=driver.findElements(By.xpath("//td[@class='ng-binding ng-scope']"));
	  Assert.assertEquals(debit.get(debit.size()-1).getText(), "$200.00");
	  
	  
  }
  @AfterMethod
  public void tearDown(ITestResult result) {
	  if(ITestResult.FAILURE==result.getStatus()) {
		  extentTest.log(Status.FAIL, result.getThrowable().getMessage());
	  }
  }
 
	
	@AfterTest
//    @AfterClass
	public void tearDown() {
		driver.close();
		reports.flush();
	}

}
