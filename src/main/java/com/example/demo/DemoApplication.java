package com.example.demo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class DemoApplication {

    public static String URL_CAPTCHA = "/Users/kox1888/Desktop/captcha.png";


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        ChromeOptions chromeOptions = new ChromeOptions().addArguments("--disable-dev-shm-usage");
        LoginPage loginPage = new LoginPage(new ChromeDriver(chromeOptions));
        loginPage.setUrlCaptcha(URL_CAPTCHA);
        TelegramBot telegramBot = new TelegramBot();
        telegramBot.setSeleniumBot(loginPage);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        WebDriver driver = loginPage.getDriver();
        driver.get("https://q.midpass.ru/ru/Home/Index");
        loginPage.logIn(telegramBot.getMapCaptcha());
        RegistrationTime registrationTime = new RegistrationTime(driver, telegramBot);
        try {
            registrationTime.passportAction();
            registrationTime.sleep(2000);
            registrationTime.registrationProcessing();
            telegramBot.sendMessage("finished", 1);
        } catch (Exception e) {
            e.printStackTrace();
            telegramBot.sendMessage("***Exception***", 1);

        }
    }
}
