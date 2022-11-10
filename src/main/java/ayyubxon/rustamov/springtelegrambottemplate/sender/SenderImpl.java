package ayyubxon.rustamov.springtelegrambottemplate.sender;

import ayyubxon.rustamov.springtelegrambottemplate.component.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Service
public class SenderImpl implements Sender {

    private final TelegramBot fesBot;

    @Override
    public <T extends Serializable> T send(BotApiMethod<T> botApiMethod) {
        T execute = null;
        try {
            execute = fesBot.execute(botApiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return execute;
    }

    @Override
    public Message send(SendAudio sendAudio) {
        Message execute = null;
        try {
            execute = fesBot.execute(sendAudio);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return execute;
    }


    @Autowired
    public SenderImpl(TelegramBot fesBot) {
        this.fesBot = fesBot;
    }
}
