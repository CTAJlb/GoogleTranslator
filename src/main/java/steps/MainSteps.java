package steps;

import core.Browser;
import core.BrowserService;
import org.jbehave.core.annotations.*;
import org.jbehave.core.steps.Steps;
import org.openqa.selenium.By;
import org.testng.Assert;

/**
 * Created by CTAJlb on 12.01.2016.
 */
public class MainSteps extends Steps {

    private static final By inputTextBox = By.xpath(".//*[@id='source']");
    private static final By translateBtn = By.xpath(".//*[@id='gt-submit']");
    private static final By resultTextBox = By.xpath("//span[@id='result_box']/span");
    private static final By allInputLanguagesDropList = By.xpath(".//*[@id='gt-sl-gms']");
    private static final By allOutputLanguagesDropList = By.xpath(".//*[@id='gt-tl-gms']");
    private static final By menuLanguageInput = By.xpath(".//*[@id='gt-sl-gms-menu']");
    private static final By menuLanguageOutput = By.xpath(".//*[@id='gt-tl-gms-menu']");
    // локаторы на изменение языка ввода
    private static final By russianInputBtn = By.xpath(".//*[@id='gt-sl-gms-menu']//./div[text()='русский']");
    // локаторы на изменение языка выхода
    private static final By germanOutputBtn = By.xpath(".//*[@id='gt-tl-gms-menu']//../div[text()='немецкий']");



    private Browser browser;
    private MyContext context;

    @Given("Start page on Google Translator")
    public void openHomePage() {
        browser.openPage("https://translate.google.com.ua/");
    }

    @When("User put word $value in textBox")
    public void inputWord(String value) {
        browser.typeText(inputTextBox, value);
    }

    @When("User click translate button")
    public void clickTranslateBtn() {
        browser.click(translateBtn);
    }

    @When("User click DropList languages input")
    public void clickInputDropListBtn() {
        browser.click(allInputLanguagesDropList);
    }


    @When("User change input language to $languageType")
    public void changeInputLanguage(String languageType) {
        browser.click(allInputLanguagesDropList);
        browser.waitForElement(menuLanguageInput);
        switch (languageType) {
            case "Русский" :
                browser.click(russianInputBtn);
                break;
            default: throw new  AssertionError("Unsupported type language Input");
        }

    }

    @When("User change output language to $languageType")
    public void changeOutPutLanguage(String languageType) {
        browser.click(allOutputLanguagesDropList);
        browser.waitForElement(menuLanguageOutput);
        switch (languageType) {
            case "Немецкий" :
                browser.click(germanOutputBtn);
                break;
            default: throw new AssertionError("Unsupported type language Output");
        }

    }


    @Then("The result of translate will be $value")
    public void checkTranslateResult(String value) {
                Assert.assertEquals(browser.getText(resultTextBox), value);

    }
    @BeforeScenario
    public void beforeScenario() {
        this.browser = BrowserService.openNewBrowser();
        this.context = new MyContext();
        System.out.println("browser opened");
    }

    @AfterScenario
    public void afterScenario() {
        browser.close();
        context.variables.clear();
    }
}
