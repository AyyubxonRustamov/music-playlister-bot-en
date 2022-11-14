package ayyubxon.rustamov.springtelegrambottemplate.sender;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;

public interface Sender {

    <T extends Serializable> T send(BotApiMethod<T> botApiMethod);

    Message send(SendAudio sendAudio);

}
