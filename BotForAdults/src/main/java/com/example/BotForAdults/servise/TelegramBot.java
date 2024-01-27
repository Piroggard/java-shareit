package com.example.BotForAdults.servise;

import com.example.BotForAdults.config.BotConfig;
import com.example.BotForAdults.model.Pose;
import com.example.BotForAdults.storage.PoseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Component
@Slf4j

public class TelegramBot extends TelegramLongPollingBot {
    private final PoseRepository poseRepository;

    final BotConfig config;

    public TelegramBot(PoseRepository poseRepository, BotConfig config) {
        this.poseRepository = poseRepository;
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String massageTest = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (massageTest) {
                case "/start":
                    try {
                        log.info("Ответ: " + update.getMessage().getChat().getFirstName());
                        String name = "<b>" + randomId().getName() + "</b>";
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(String.valueOf(chatId));
                        sendPhoto.setPhoto(new InputFile(new File(randomId().getPoseUrl())));
                        sendPhoto.setCaption(name + "\n\n" + randomId().getDescription());
                        sendPhoto.setParseMode("HTML");

                        execute(sendPhoto);
                    } catch (TelegramApiException e) {
                        log.error("Ошибка при отправке изображения и текста: " + e.getMessage());
                    }
                    break;
                default:
                    sendMassage(chatId, "Нет такой команды ((( ");
            }

        } else {

        }
    }

    private void sendMassage(long chatId, String testToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(testToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }

    private Pose randomId() {
        List<Pose> randomPose = poseRepository.findAll();
        Random random = new Random();
        int randomIndex = random.nextInt(randomPose.size());
        return randomPose.get(randomIndex);
    }


}
