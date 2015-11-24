package com.sami.amazonTest;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class StepDefinitions {
	
	private static ChromeDriverService service;
	private WebDriver driver;
	
	/*************************************************************************************
	* Starting chromedriver as a service. This saves a lot of time in larger test suites *
	**************************************************************************************/
	public static void createAndStartService() {
		service = new ChromeDriverService.Builder()
	        .usingDriverExecutable(new File("C://Users//Sami//Downloads//chromedriver//chromedriver.exe"))
	        .usingAnyFreePort()
	        .build();
	    try {
			service.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createDriver() {
		driver = new RemoteWebDriver(service.getUrl(),
	    DesiredCapabilities.chrome());
	}

	public static void createAndStopService() {
		service.stop();
	}

	public void quitDriver() {
		driver.quit();
	}
	
	/****************************************************************************************
	* Method for ensuring that page has fully loaded before trying to search items from it  *
	*****************************************************************************************/
	protected WebElement waitForElementVisible(By by) {
	    WebDriverWait wait = new WebDriverWait(driver,6);
	    WebElement element = null;

	    element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	    return element;
	}
	
		
	@Given("^I open web browser and navigate to 'www\\.amazon\\.com'$")
	public void shouldNavigateTo_www_amazon_com() {
		createAndStartService();
		createDriver();
		driver.get("http://amazon.com");
		
	    //Ensure you are on right web page
	    Assert.assertTrue("That is not Amazon.com web page", 
	    		driver.getTitle().startsWith("Amazon.com: Online Shopping for Electronics"));
	}

	@When("^I search for Nikon$")
	public void shouldSearchForNikon() {
		//wait for element to become visible
		waitForElementVisible(By.id("twotabsearchtextbox"));
		//Enter text to be searched
		WebElement textbox = driver.findElement(By.id("twotabsearchtextbox"));
		textbox.sendKeys("Nikon");
				
		//press search button or enter if searchButton is not found
		if (driver.findElements(By.className("nav-search-submit")).size() != 0) {
			WebElement button = driver.findElement(By.className("nav-search-submit"));
			button.click();
		}else{
			WebElement webElement = driver.findElement(By.id("twotabsearchtextbox"));
			webElement.sendKeys(Keys.ENTER); // Enter 
		}
	}

	@Then("^I should be able to sort results by price high-to-low$")
	public void shouldBeAbleToSortResultsByPriceHighToLow() {
		//wait for element to become visible
		waitForElementVisible(By.id("sort"));
		//Change sorting to high-to-low
		Select dropdown = new Select(driver.findElement(By.id("sort")));
		dropdown.selectByVisibleText("Price: High to Low");
	}

	@And("^I should be able to select second camera from the list$")
	public void shouldBeAbleToSelectSecondCameraFromTheList() {
		//wait for element to become visible
		waitForElementVisible(By.id("result_1"));
		
		//Find second item from the list
		WebElement secondItem = driver.findElement(By.id("result_1"));
		secondItem.click();
	}

	@And("^I should see 'Nikon D(\\d+)X' in the product description topic$")
	public void shouldSeeNikonDX3_InTheProductDescriptionTopic(int arg1) {
		//wait for element to become visible
		waitForElementVisible(By.id("productTitle"));
		
		WebElement itemDescriptionText = driver.findElement(By.id("productTitle"));
		String descriptionString = itemDescriptionText.getText();
		Assert.assertTrue("Item description did not contain 'Nikon D3X' ", 
				descriptionString.contains("Nikon D3X"));
	}
	
	@Then("^I close the browser$")
	public void closeTheBrowser() {
		quitDriver();
		createAndStopService(); 
		
	}
	
}