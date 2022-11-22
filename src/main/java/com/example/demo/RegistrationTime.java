package com.example.demo;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Optional;

public class RegistrationTime {

    public WebDriver driver;

    int countCheck = 0;

    @FindBy(xpath = "/html/body/div[2]/div/div[3]/div[5]/div/div[2]/div/div/div/a")
    public WebElement pasport;

    @FindBy(id = "availableSlotsCount")
    public WebElement availableSlotsCountElement;

    @FindBy(xpath = "//*[@id=\"CalendarHead\"]/tr[1]/td/div/div[2]/a")
    public WebElement nextMonth;

    @FindBy(className = "DateBox")
    public WebElement day;

    public TelegramBot telegramBot;

    public RegistrationTime(WebDriver driver, TelegramBot bot) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        telegramBot = bot;
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        driver.manage().window().maximize();
    }

    public void makeAction(WebElement element){
        new WebDriverWait(driver, Duration.ofNanos(100))
                .until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    private Optional<WebElement> findAvailableDate(){
        sleep(2000);//иногда не успевает подгружать элементы DateBox
        return driver.findElements(By.className("DateBox"))
                .stream()
                .filter(element -> {
                    try {
                        var text = element.findElement(By.tagName("span")).getText();
                        return Integer.parseInt(text.substring(0, text.indexOf("/"))) != 0;
                    } catch (Exception e) {
                        System.out.println("Not available day");
                        return false;
                    }
                })
                .findAny();
    }

    private void saveAll(){
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.className("saveAll")));
        telegramBot.sendMessage("***SUCCESSFUL_REGISTRATIONS***", 1);
    }

    private void chooseRegTime(WebElement element) {
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
        telegramBot.sendMessage("successful: chooseRegTime", 1);
//        sleep(1000);//для теста надо раздизейблить кнопку к оформлению
        confirmAction();
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", driver.findElement(By.id("warningChecbox")));
//        sleep(1000);//для теста надо раздизейблить кнопку saveAll
        saveAll();
    }

    private void chooseRegDay(WebElement element) {
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();", element);
        telegramBot.sendMessage("successful: chooseRegDay", 1);
        sleep(1000);
        driver.findElements(By.cssSelector(".confirmed"))
                .stream()
                .findAny()
                .ifPresent(this::chooseRegTime);
    }

    private void nextMth(){
        makeAction(nextMonth);
        sleep(2000);
    }

    private void registrationAction() {
        sendMessage();
        telegramBot.sendMessage("have available registration", 1);
        findAvailableDate().ifPresent(this::chooseRegDay);
    }

    public void registrationProcessing() {
            sleep(2000);
            System.out.println("checkAvailableSlots:" + ++countCheck);
            if (checkAvailableCount()) {
                registrationAction();
            } else {
                nextMth();
                if (checkAvailableCount()) {
                    registrationAction();
                } else {
                    nextMth();
                    if (checkAvailableCount()) {
                        registrationAction();
                    } else {
                        sleep(60000);
                        driver.navigate().refresh();
                        registrationProcessing();
                    }
                }
            }
    }

    public void passportAction(){
        sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", pasport);
    }

    public void confirmAction(){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].click();", driver.findElement(By.id("confirm")));
        } catch (Exception e){
            e.printStackTrace();
            confirmAction();
        }
    }

    private void sendMessage(){
        telegramBot.sendMessage("***REGISTRATION***", 10);
    }

    // TODO: 22.11.2022 тут правим для теста условие
    private boolean checkAvailableCount(){
        sleep(1000);
        try {
            return Integer.parseInt(availableSlotsCountElement.getText()) != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
