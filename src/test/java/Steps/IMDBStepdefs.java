package Steps;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class IMDBStepdefs {

    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void loadBrowser() {
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        wait = new WebDriverWait(driver, 10);

    }

    @After
    public void close() {
        driver.quit();
    }


    @Given("I am in IMDB page")
    public void loadPage() {
        String url = "https://www.imdb.com/";
        driver.get(url);
        validateHomePage();
    }




    @And("I enter a {word}")
    public void enteramovie(String movie) {
        enteraMovie(movie);
    }

    @Then("I could see the result for the {word}")
    public void listResults(String movie) {
        displayResults(movie);
    }

    /*@Then("I could see a list of movies")
    public void listResults() {
        displayResults();
    }*/


    protected boolean waitElementvisible(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            Assert.fail("Element was not found \n");
            return false;
        }
    }

    protected void validateHomePage() {
        By logo = By.cssSelector("#home_img_holder");
        waitElementvisible(logo);

        By search = By.cssSelector("[name=\"q\"]");
        waitElementvisible(search);

        System.out.println("You are on IMDB page");
    }


    protected void clickElement(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }


    private void selectMenuoption(String option) {
        boolean found = false;

        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//span[@id = \"navbar-search-category-select-contents\"]//a")));
        //System.out.println("Options in the Menu :  " + options.size());
        for (WebElement element : options) {
            if (element.getText().equals(option.trim())) {
                found = true;
                element.click();
                break;
            }
        }
        assertTrue("The option '" + option + "' was not found in the menu", found);
     }


    protected void inputText(By locator, String texto) {
        waitElementvisible(locator);
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(texto);
    }

    protected void searchBy(String option) {
        By searchLabel = By.xpath("//form[@id= \"nav-search-form\"]//label");
        clickElement(searchLabel);

        selectMenuoption(option.trim());
        assertEquals("The search by " + option + " fails", option.trim(), driver.findElement(searchLabel).getText().trim());
        System.out.println("Search by : " + driver.findElement(searchLabel).getText());

    }

    protected void enteraMovie(String movie) {
        By searchText = By.xpath("//*[@id=\"suggestion-search\"]");
        inputText(searchText, movie);
        System.out.println("The movie '" + movie + "' was searched ");

    }


    protected void displayResults(String movie) {
        By searchButton = By.id("suggestion-search-button");
        clickElement(searchButton);

        List<WebElement> movies = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("td.result_text")));
        System.out.println("Displaying " + movies.size() + " results for : " + movie);
        for (WebElement element : movies) {
            System.out.println(element.getText());
        }
      }

    @When("I select {string}")
    public void selectanOption(String option) {
        searchBy(option);
    }
}
