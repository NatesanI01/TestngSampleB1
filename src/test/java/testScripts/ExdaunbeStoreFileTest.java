package testScripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ExdaunbeStoreFileTest {
	WebDriver driver;
	Properties temProp;
	ExtentReports reports;
	ExtentSparkReporter spark;
	ExtentTest extentTest;
	@BeforeClass
	public void setupExtent() {
	  reports=new ExtentReports();
	  spark=new ExtentSparkReporter("target\\DanubeStoreXMC.html");
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
//	  driver.get(temProp.getProperty("url"));
	  driver.get("https://danube-webshop.herokuapp.com/");
	}
  @Test(priority=1,dataProvider="registerPage")
  public void registerTest(String name,String sName,String email,String pwd, String company) throws InvalidFormatException, IOException {
	  extentTest=reports.createTest("Registration Page Test");
	  driver.findElement(By.xpath(readDataExcel("signup"))).click();
	  driver.findElement(By.xpath(readDataExcel("name"))).sendKeys(name);
	  driver.findElement(By.xpath(readDataExcel("surname"))).sendKeys(sName);
	  driver.findElement(By.xpath(readDataExcel("email"))).sendKeys(email);
	  driver.findElement(By.xpath(readDataExcel("pwd"))).sendKeys(pwd);
	  driver.findElement(By.xpath(readDataExcel("company"))).sendKeys(company);
	  driver.findElement(By.xpath(readDataExcel("radioBtn"))).click();
	  driver.findElement(By.xpath(readDataExcel("checkbox1"))).click();
	  driver.findElement(By.xpath(readDataExcel("checkbox2"))).click();
	  driver.findElement(By.xpath(readDataExcel("register"))).click();
	  Assert.assertEquals(driver.findElement(By.xpath(readDataExcel("successMsg"))).getText(), "Welcome back, abc01@gmail.com");
  }
  
  public String readDataExcel(String objname) throws InvalidFormatException, IOException {
	  String objPath="";
	  String path=System.getProperty("user.dir")+"//src//test//resources//testData//daunbeData.xlsx";
	  XSSFWorkbook workbook=new XSSFWorkbook(new File(path));
	  XSSFSheet sheet=workbook.getSheet("loginPage");
	  int numRow=sheet.getLastRowNum();
	  for(int i=1;i<=numRow;i++) {
		  XSSFRow row=sheet.getRow(i);
		  if(row.getCell(0).getStringCellValue().equalsIgnoreCase(objname)) {
			  objPath=row.getCell(1).getStringCellValue();
		  }
	  }
	  return objPath;
  }
  
  public String readXmlData(String tagName) throws SAXException, IOException, ParserConfigurationException {
	  String path=System.getProperty("user.dir")+"//src//test//resources//testData//daunbe.xml";
	  DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	  DocumentBuilder build=factory.newDocumentBuilder();
	  Document document=build.parse(new File(path));
	  NodeList node=document.getElementsByTagName("daunbeRep");
	  Node node1=node.item(0);
	  Element elem=(Element)node1;
	  return elem.getElementsByTagName(tagName).item(0).getTextContent();
	  
  }
  @DataProvider(name="registerPage")
  public Object[][] getData() throws CsvValidationException, IOException{
	  String path=System.getProperty("user.dir")+"//src//test//resources//testData//daunbe.csv";
	  String[] cols; 
	  CSVReader reader=new CSVReader(new FileReader(path));
	  ArrayList<Object> dataList=new ArrayList<Object>();
	  while((cols=reader.readNext())!=null) {
		  Object[] records= {cols[0],cols[1],cols[2],cols[3],cols[4]};
		  dataList.add(records);
	  }
	  return dataList.toArray(new Object[dataList.size()][]);
  }
  @Test(enabled=false)
  public void loginTest() {
	  extentTest=reports.createTest("LoginPage Test");
	  driver.get(temProp.getProperty("url"));
	  driver.findElement(By.id("login")).click();
	  driver.findElement(By.xpath("//input[@placeholder='Email']")).sendKeys(temProp.getProperty("email"));
	  driver.findElement(By.xpath("//input[@placeholder='Password']")).sendKeys(temProp.getProperty("password"));
	  driver.findElement(By.xpath("//button[contains(text(),'Sign In')]")).click();
  }
  @Test(priority=2)
  public void searchItemTest() throws SAXException, IOException, ParserConfigurationException {
	  extentTest=reports.createTest("Search Item Page Test");
	  driver.findElement(By.xpath(readXmlData("search"))).sendKeys(temProp.getProperty("search"));
	  driver.findElement(By.xpath(readXmlData("searchBtn"))).click();
	  Assert.assertEquals(driver.findElement(By.xpath(readXmlData("searchMsg"))).getText(),"Celsius 233");
  }
  @Test(priority=3)
  public void checkoutTest() throws SAXException, IOException, ParserConfigurationException {
	  extentTest=reports.createTest("CheckOut Item Page Test");
	  driver.findElement(By.xpath(readXmlData("searchMsg"))).click();
	  driver.findElement(By.xpath(readXmlData("add"))).click();
	  driver.findElement(By.xpath(readXmlData("checkout"))).click();
	  driver.findElement(By.xpath(readXmlData("name"))).sendKeys(temProp.getProperty("name"));
	  driver.findElement(By.xpath(readXmlData("sname"))).sendKeys(temProp.getProperty("surname"));
	  driver.findElement(By.xpath(readXmlData("address"))).sendKeys(temProp.getProperty("address"));
	  driver.findElement(By.xpath(readXmlData("zipcode"))).sendKeys(temProp.getProperty("zipcode"));
	  driver.findElement(By.xpath(readXmlData("city"))).sendKeys(temProp.getProperty("city"));
	  driver.findElement(By.xpath(readXmlData("company"))).sendKeys(temProp.getProperty("company"));
	  driver.findElement(By.xpath(readXmlData("checkBox"))).click();
	  driver.findElement(By.xpath(readXmlData("buyBtn"))).click();
	  Assert.assertEquals(driver.findElement(By.xpath(readXmlData("successMsg"))).getText(), "All good, order is on the way. Thank you!!");
  }
  @AfterMethod
  public void tearDown(ITestResult result) {
	  if(ITestResult.FAILURE==result.getStatus()) {
		  extentTest.log(Status.FAIL, result.getThrowable().getMessage());
	  }
  }
  @AfterTest
  public void tearDown() {
	  driver.close();
	  reports.flush();
  }
}
