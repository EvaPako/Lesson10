import mts.HomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class HomeTest {
    public static WebDriver driver;
    private static HomePage homePage;


    @BeforeSuite
    public static void setUp() {
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.mts.by/");
        homePage = new HomePage(driver);
        homePage.acceptCookies();

    }

    @AfterSuite
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
        if (homePage.driver != null){
            homePage.driver.quit();
        }
    }

    @Test(priority = 0)
    public void verifyText() {

        WebElement webElement = homePage.getWebElement(homePage.onlinePayBlock);
        Assert.assertEquals(webElement.getText(), "Онлайн пополнение\nбез комиссии");

    }

    @Test(priority = 1)
    public void verifyLogotype() {

        try {
            for (By logo : homePage.paymentLogos) {
            WebElement logoElement = homePage.getWebElement(logo);
            Assert.assertTrue(logoElement.isDisplayed(), "Логотип не найден: " + logo);
        }

        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
        }
    }

  @Test(priority = 2)
    public void checkServiceLink() {
        homePage.clickAboutService();
        String actualUrl = homePage.driver.getCurrentUrl();
        String expectedUrl = "https://www.mts.by/help/poryadok-oplaty-i-bezopasnost-internet-platezhey/";
        Assert.assertEquals(actualUrl, expectedUrl, "URL не соответствует ожидаемому");
        homePage.navigateBack();
    }

    @Test(priority = 3)
    public void Input () {
        homePage.enterPhone("297777777");
        homePage.enterSum("250");
        homePage.clickPay();
    }

    @Test(priority = 4)
    public void testPlaceHolders(){

        try {
            for (String[] element : homePage.elementsPayForm) {
                WebElement inputField = homePage.getWebElement(By.id(element[0]));
                String placeholderValue = inputField.getAttribute("placeholder");
                Assert.assertEquals(placeholderValue, element[1], "У элемента " + element[0] + " надпись незаполненого поля не соответствует ожидаемому");
            }

        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
        }
    }

    @Test(priority = 5)
    public void checkFrame() {

        String sum = "250.00";
        String phoneNumber = "297777777";

        //Выбираем Услуги связи
        homePage.enterPhone(phoneNumber);
        homePage.enterSum(sum);
        homePage.clickPay();

        //Ожидание открытия окна оплаты
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Переключаемся на фрейм
        WebElement frame1 = homePage.getWebElement(By.className("bepaid-iframe"));
        homePage.driver.switchTo().frame(frame1);

        //Проверяем отображение суммы, номера телефона и т.д.
        String actualSumFrame = homePage.getWebElement(homePage.sumFrame).getText();
        Assert.assertEquals(actualSumFrame, sum + " BYN", "В окне оплаты сумма отображается неверно");

        String actualButtonSumFrame = homePage.getWebElement(homePage.sumButtonFrame).getText();
        Assert.assertEquals(actualButtonSumFrame, "Оплатить " + sum + " BYN", "В окне оплаты сумма на кнопке отображается неверно");

        String actualPhoneNumberFrame = homePage.getWebElement(homePage.phoneNumberFrame).getText();
        Assert.assertEquals(actualPhoneNumberFrame, "Оплата: Услуги связи Номер:375" + phoneNumber, "В окне оплаты сумма отображается неверно");

        String actualСardNumberFrame = homePage.getWebElement(homePage.cardNumberFrame).getText();
        Assert.assertEquals(actualСardNumberFrame, "Номер карты", "Отображается неверно");

        String actualSrokDeistvia = homePage.getWebElement(homePage.srokDeistvia).getText();
        Assert.assertEquals(actualSrokDeistvia, "Срок действия", "Отображается неверно");

        // Вернуться к основному контенту страницы:
        homePage.driver.switchTo().defaultContent();
    }
}



