package ayyubxon.rustamov.springtelegrambottemplate.component;

import ayyubxon.rustamov.springtelegrambottemplate.processors.Processor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.username}")
    private String username;

    private Processor processor;

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        processor.process(update);
    }

    @Autowired
    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public <T extends Serializable> T execute(PartialBotApiMethod<T> botApiMethod) {
        return null;
    }
}
