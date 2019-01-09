package com.foxfail.appium_bell;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

// Запускаю тесты в определенном порядке
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppiumTest {
    private static WebDriver driver;
    private static AndroidDriver androidDriver;
    // данные контакта для тестов
    private final String CONTACT_FIRST_NAME = "Vitaly";
    private final String CONTACT_LAST_NAME = "Dorofeev";
    private final String CONTACT_PHONE_NUMBER = "12345678";
    String TAG = this.getClass().getSimpleName();

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        System.out.println("setUp: start");
        // устанавливаем capabilities для Appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("BROWSER_NAME", "Android");
//        capabilities.setCapability("VERSION", "4.4.2");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Nexus_5X_API_26");

        // Имя of the package которую запускаю запустить
        capabilities.setCapability("appPackage", "com.android.contacts");

        // Main Activity of the package которую запускаем
        capabilities.setCapability("appActivity", "com.android.contacts.activities.PeopleActivity");

        //Create RemoteWebDriver instance and connect to the Appium server
        //driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver = androidDriver;
        System.out.println("setUp: stop");
    }

    @AfterClass
    public static void teardown() {
        System.out.println("teardown: start");
        try {
            driver.quit();
        } catch (NullPointerException e) {
            System.out.println("\tWebDriver не получилось закрыть");
        }

        System.out.println("teardown: stop");
    }


    @Test
    public void test1NewContact() {
        System.out.println("testNewContact: start");
        try {
            // если просит зайти в аккаунт гугл
            WebElement skip_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.google.android.gsf.login:id/skip_button\"]"));
            if (skip_button != null) skip_button.click();
        } catch (Exception e) {
            System.out.println("\tПропущен шаг: пропуск логина в гугл аккаунт. Это нормально");
        }


        WebElement create_button;
        try {
            // ищем кнопку "создать новый контакт" по ид (версии андрой до появления fab'ов)
            create_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/create_contact_button\"]"));
        } catch (NoSuchElementException e) {
            // ищем fab "создать новый контакт" по ид
            create_button = driver.findElement(By.xpath("//android.widget.ImageButton[@resource-id=\"com.android.contacts:id/floating_action_button\"]"));
        }
        create_button.click();

//        // тест для себя
//        try {
//        WebElement test_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/let_but\"]"));
//        if (test_button!=null)test_button.click();
//        } catch (Exception e) {
//            System.out.println("Тест когда нельзя найти кнопку но это не критично");
//        }

        try {
            //ожидаем появления сообщения
            // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            //в интернете пишут что так ждать ещё лучше
            WebDriverWait waitDriver = new WebDriverWait(driver, 20);
            waitDriver.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/left_button\"]")));

            // сохранить локально
            WebElement savelocal_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/left_button\"]"));
            savelocal_button.click();
        } catch (Exception e) {
            System.out.println("\tПропущен шаг: сохранить контакт локально(ещё один вход в гугл аккаунт). Это нормально");
        }


        // ИМЯ
        WebElement firstname_edittext = driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'First name')]")); // ищем поле ввода по тексту в нем

        firstname_edittext.clear();
//        androidDriver.getKeyboard().sendKeys(CONTACT_FIRST_NAME);
        firstname_edittext.sendKeys(CONTACT_FIRST_NAME);

        // ФАМИЛИЯ
        WebElement lastname_edittext = driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'Last name')]"));
        lastname_edittext.clear();
        lastname_edittext.sendKeys(CONTACT_LAST_NAME);

        // ТЕЛЕФОН
        androidDriver.pressKeyCode(AndroidKeyCode.ENTER); // поле становится видимым и его можно найти
//        androidDriver.hideKeyboard(); // можно еще спрятать клавиатуру
        WebElement phone_edittext = driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'Phone')]"));
        phone_edittext.clear();
        phone_edittext.sendKeys(CONTACT_PHONE_NUMBER);

        // СОХРАНИТЬ
        WebElement save_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/editor_menu_save_button\"]"));
        save_button.click();
        System.out.println("testNewContact: stop");
    }

    @Test
    public void test2DeleteContact() {
        System.out.println("testDeleteContact: start");
        androidDriver.launchApp();

        // эти переменные будут использованы для сравнения имени
        // которое мы искали с найденным которое мы хотим удалить
        String actualContactName;
        String nameToSearch;

        driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.android.contacts:id/menu_search\"]")).click();

        WebElement search_edittext = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.android.contacts:id/search_view\"]"));
        search_edittext.clear();
        nameToSearch = CONTACT_FIRST_NAME + " " + CONTACT_LAST_NAME;
        search_edittext.sendKeys(nameToSearch);

        // list в котором все найденные контакты. предполагаю что нам нужен первый, если вообще он найден
        WebElement searchresults_list = driver.findElement(By.xpath("//android.widget.ListView[@resource-id=\"android:id/list\"]"));
        List<WebElement> searchresultsListElements = searchresults_list.findElements(By.xpath("//*"));
        // есть ли там вообще что то
        // выдаем ошибку что ожидалось найти хоть один элемент
        Assert.assertNotEquals(0, searchresultsListElements.size());
        // если пошло дальше значит есть хоть один элемент

        // щелкаем на первый элемент
        WebElement firstListEntry = searchresultsListElements.get(0).findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.android.contacts:id/cliv_name_textview\"]"));
        firstListEntry.click();

        //ждем появления элемента "меню"
        WebDriverWait waitDriver = new WebDriverWait(driver, 20);
        waitDriver.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.ImageButton[@content-desc='More options']")));

        // ПРОВЕРКА
        WebElement contact_name_textview = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.android.contacts:id/large_title\"]"));
        actualContactName = contact_name_textview.getText();
        Assert.assertEquals("Что хотим удалить должно совпадать с тем что удаляем", nameToSearch, actualContactName);
        // если проверка проходит то точно идем дальше

        // щелкаем на меню
        driver.findElement(By.xpath("//android.widget.ImageButton[@content-desc='More options']")).click();


        // УДАЛИТЬ
        driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Delete')]")).click();
        // подтверждение
        waitDriver.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.Button[@resource-id=\"android:id/button1\"]")));
        driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"android:id/button1\"]")).click();


        System.out.println("testDeleteContact: stop");
    }
}
