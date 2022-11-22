package com.example.demo;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TelegramBot extends TelegramLongPollingBot {
    public String CHART_ID = "247429331";
    public volatile Map<String, String> mapCaptcha = new HashMap<>();
    public LoginPage seleniumBot;


    public Map<String, String> getMapCaptcha() {
        return mapCaptcha;
    }

    public void setMapCaptcha(Map<String, String> mapCaptcha) {
        this.mapCaptcha = mapCaptcha;
    }


    public LoginPage getSeleniumBot() {
        return seleniumBot;
    }

    public void setSeleniumBot(LoginPage seleniumBot) {
        this.seleniumBot = seleniumBot;
    }

    @Override
    public String getBotUsername() {
        return "Kex1888bot";
    }

    @Override
    public String getBotToken() {
        return "5759796405:AAG3QsID6PiIBtlNyIKCXy5Q-l6r5hLGvSU";
    }


    @Override
    public void onUpdateReceived(Update update) {
        String userName = update.getMessage().getFrom().getUserName();
        String text = update.getMessage().getText();
        mapCaptcha.put(userName, text);
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("CHART_ID: "+ CHART_ID);
            SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
            message.setChatId(update.getMessage().getChatId().toString());
            message.setText("What's up");

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendCaptchaCmd(String filePath, String chatId) {
        // Create send method
        SendPhoto sendPhotoRequest = new SendPhoto();
        // Set destination chat id
        sendPhotoRequest.setChatId(chatId);
        File file = new File(filePath);
        // Set the photo file as a new photo (You can also use InputStream with a constructor overload)
        sendPhotoRequest.setPhoto(new InputFile(file));
        try {
            // Execute the method
            execute(sendPhotoRequest);
            file.delete();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String textMessage, int repeat) {
        SendMessage message = new SendMessage(); // Create a SendMessage object with mandatory fields
        message.setChatId(CHART_ID);
        message.setText(textMessage);
        try {
            for (int i = 0; i < repeat; i++) {
                execute(message); // Call method to send the message
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
