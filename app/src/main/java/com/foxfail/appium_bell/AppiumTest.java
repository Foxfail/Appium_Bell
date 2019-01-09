package com.foxfail.appium_bell;

import android.annotation.SuppressLint;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class AppiumTest {
    private static WebDriver driver;
    private static AndroidDriver androidDriver;
    String TAG = this.getClass().getSimpleName();

    @BeforeClass
    public static void setUp() throws MalformedURLException {
        System.out.println("setUp: ");
        // устанавливаем capabilities для Appium
        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setCapability("BROWSER_NAME", "Android");
//        capabilities.setCapability("VERSION", "4.4.2");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "Nexus_5X_API_26");

        // Имя of the package которую хотим запустить
        capabilities.setCapability("appPackage", "com.android.contacts");

        // Main Activity of the package которую запускаем
        capabilities.setCapability("appActivity", "com.android.contacts.activities.PeopleActivity");

        //Create RemoteWebDriver instance and connect to the Appium server
        //It will launch the Calculator App in Android Device using the configurations
        //specified in Desired Capabilities
        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver = androidDriver;
//        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);


    }

    @AfterClass
    public static void teardown() {
        System.out.println("teardown: ");
        //close the app
        try {
            driver.quit();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("Assert")
    @Test
    public void testNewContact() {
        System.out.println("testNewContact: start");
        try {
            // если просит зайти в аккаунт гугл
            WebElement skip_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.google.android.gsf.login:id/skip_button\"]"));
            if (skip_button != null) skip_button.click();
        } catch (Exception e) {
            System.out.println("Пропущен шаг: пропуск логина в гугл аккаунт");
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

//        // тест
//        try {
//        WebElement test_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/let_but\"]"));
//        if (test_button!=null)test_button.click();
//        } catch (Exception e) {
//            System.out.println("Тест когда нельзя найти кнопку но это не критично");
//        }

        try {
            //ожидаем появления сообщения
//            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

            //в интернете пишут что так ждать ещё лучше
            WebDriverWait waitDriver = new WebDriverWait(driver, 20);
            waitDriver.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/left_button\"]")));

            // сохранить локально
            WebElement savelocal_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/left_button\"]"));
            savelocal_button.click();
        } catch (Exception e) {
            System.out.println("Пропущен шаг: сохранить контакт локально(ещё один вход в гугл аккаунт)");
        }


        // ищем поле ввода по тексту в нем
        WebElement firstname_edittext = driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'First name')]"));
        firstname_edittext.clear();
        firstname_edittext.sendKeys("Vitaly");

        WebElement lastname_edittext = driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'Last name')]"));
        lastname_edittext.clear();
        lastname_edittext.sendKeys("Dorofeev");

        androidDriver.pressKeyCode(AndroidKeyCode.ENTER); // поле становится видимым и его можно найти
        WebElement phone_edittext = driver.findElement(By.xpath("//android.widget.EditText[contains(@text,'Phone')]"));
        phone_edittext.clear();
        phone_edittext.sendKeys("12345678");

        WebElement save_button = driver.findElement(By.xpath("//android.widget.Button[@resource-id=\"com.android.contacts:id/editor_menu_save_button\"]"));
        save_button.click();

        //код который можно удалить, но я буду в него подсматривать
//        driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.android.contacts:id/add_organization_button\"]")).click();
//        WebElement organization_layout = driver.findElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"com.android.contacts:id/sect_fields\"]"));
//        WebElement organization_edittext = organization_layout.findElements(By.xpath("//android.widget.LinearLayout[@resource-id=\"com.android.contacts:id/editors\"]")).get(0);
//        organization_edittext.sendKeys("Bell Integrator");


//        androidDriver.pressKeyCode(AndroidKeyCode.ENTER);
//        androidDriver.pressKeyCode(AndroidKeyCode.ENTER);
//        WebElement tel_edittext = driver.switchTo().activeElement();
//        androidDriver.getKeyboard().sendKeys(new String[]{"123456"});

//        WebElement tel_layout = organization_layout.findElement(By.xpath("//android.widget.LinearLayout")).findElement(By.xpath("//android.widget.LinearLayout[@resource-id=\"com.android.contacts:id/kind_editors\"]"));
//        WebElement tel_edittext = tel_layout.findElements(By.xpath("//android.widget.EditText")).get(0);


//        driver.findElement(By.id("com.android.contacts:id/create_contact_button")).click();

        System.out.println("testNewContact: stop");
    }

    @Test
    public void testDeleteContact() {

    }
}
