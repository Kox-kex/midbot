package com.example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginPage {

    public static String URL_CAPTCHA;

    public WebDriver driver;

    public String pas = "j4W5Bb";

    @FindBy(xpath = "//*[@id=\"FormLogOn\"]/div/div[2]/div[2]/div[2]/select")
    public WebElement country;

    @FindBy(xpath = "//*[@id=\"Password\"]")
    public WebElement password;

    @FindBy(xpath = "//*[@id=\"FormLogOn\"]/div/div[2]/div[3]/div[2]/select")
    public WebElement city;

    @FindBy(xpath = "//*[@id=\"Captcha\"]")
    public WebElement captcha;

    @FindBy(xpath = "//*[@id=\"Email\"]")
    public WebElement mail;

    @FindBy(xpath = "//*[@id=\"FormLogOn\"]/div/div[14]/button")
    public WebElement continueClick;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        driver.manage().window().maximize();
    }


    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public static String getUrlCaptcha() {
        return URL_CAPTCHA;
    }

    public static void setUrlCaptcha(String urlCaptcha) {
        URL_CAPTCHA = urlCaptcha;
    }


    public void downloadsCaptcha(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        WebElement logo = driver.findElement(By.cssSelector("#imgCaptcha"));
        String logoSRC = logo.getAttribute("src");
        try {
            URL imageURL = new URL(logoSRC);
            BufferedImage saveImage = ImageIO.read(imageURL);
            ImageIO.write(saveImage, "png", new File(URL_CAPTCHA));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logIn(Map<String, String> captchaMap){
        System.out.println(captchaMap);
//        new Select(country).selectByValue("253ca433-0ff2-93c3-889f-6e7a0824b13e");//Казахстан
        new Select(country).selectByValue("8f019c84-30c2-1092-72e1-78909b678ae4");//Армения
//        new Select(city).selectByValue("674696fb-6dcd-0970-3320-aff714dab43d");//алматы
        new Select(city).selectByValue("5f73a3c8-98ed-4aa7-cff8-f67a9c8e7253");//Ереван
        mail.sendKeys("kox.kex.kex@gmail.com");
//        captcha.sendKeys(captchaMap.get("kex1991"));
        password.sendKeys(pas);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        js.executeScript("arguments[0].click();", continueClick);
    }

}
