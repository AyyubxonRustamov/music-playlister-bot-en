package ayyubxon.rustamov.springtelegrambottemplate.service;

import ayyubxon.rustamov.springtelegrambottemplate.builder.KeyboardBuilder;
import ayyubxon.rustamov.springtelegrambottemplate.builder.TextBuilder;
import ayyubxon.rustamov.springtelegrambottemplate.checker.UsernameChecker;
import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    @Value("${bot.username}")
    private String username;

    final UsernameChecker usernameChecker;

    @Override
    public SendMessage start(Message message) {

        return SendMessage.builder()
                .text(TextBuilder.startMessage(message.getFrom()))
                .chatId(message.getChatId())
                .parseMode(ParseMode.MARKDOWN)
                .replyMarkup(KeyboardBuilder.homeKeyboard())
                .build();
    }

    @Override
    public SendMessage home(Message message) {
        return SendMessage.builder()
                .text(TextBuilder.HOME)
                .chatId(message.getChatId())
                .replyMarkup(KeyboardBuilder.homeKeyboard())
                .build();
    }

    @Override
    public SendMessage namingPlaylist(Message message) {
        return SendMessage.builder()
                .text(TextBuilder.NAMING_PLAYLIST)
                .chatId(message.getChatId())
                .replyMarkup(new ReplyKeyboardRemove(true))
                .build();
    }

    @Override
    public SendMessage playlistNameExist(Message message, String name) {
        return SendMessage.builder()
                .text(TextBuilder.playlistNameExist(name))
                .chatId(message.getChatId())
                .replyMarkup(new ReplyKeyboardRemove(true))
                .build();
    }

    @Override
    public SendMessage playlistCreated(Message message, String name) {
        return SendMessage.builder()
                .text(TextBuilder.playlistCreated(name))
                .chatId(message.getChatId())
                .replyMarkup(KeyboardBuilder.homeKeyboard())
                .build();
    }

    @Override
    public SendMessage selectPlaylist(Message message, List<Playlist> playlists) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(TextBuilder.SELECT_PLAYLIST);
        sendMessage.setReplyMarkup(KeyboardBuilder.playlistNewKeyboard(playlists));
        sendMessage.setReplyToMessageId(message.getMessageId());
        if (playlists == null || playlists.isEmpty())
            sendMessage.setText(TextBuilder.SELECT_PLAYLIST_EMPTY);

        return sendMessage;
    }

    @Override
    public SendMessage audioSaved(Message message) {
        return SendMessage.builder()
                .text(TextBuilder.AUDIO_SAVED)
                .chatId(message.getChatId())
                .replyMarkup(KeyboardBuilder.homeKeyboard())
                .build();
    }

    @Override
    public SendMessage allPlaylists(Message message, List<Playlist> playlists) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText((playlists == null || playlists.isEmpty())?
                TextBuilder.ALL_PLAYLISTS_EMPTY : TextBuilder.ALL_PLAYLISTS);
        sendMessage.setReplyMarkup(KeyboardBuilder.playlistHomeKeyboard(playlists));
        return sendMessage;
    }

    @Override
    public SendMessage allAudiosFirstPage(Message message, List<AudioEntity> audioEntities) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        if (audioEntities == null || audioEntities.isEmpty()) {
            sendMessage.setText(TextBuilder.ALL_AUDIOS_EMPTY);
            sendMessage.setReplyMarkup(KeyboardBuilder.homeKeyboard());
        } else {
            sendMessage.setText(TextBuilder.allAudios(audioEntities, 0, 9));
            sendMessage.setReplyMarkup(KeyboardBuilder.allAudiosKeyboard(audioEntities, 0, 9, "ALLAUDIOS"));
        }
        return sendMessage;
    }

    @Override
    public SendMessage playlistAudiosFirstPage(Message message, List<AudioEntity> audioEntities, String playlistName) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        if (audioEntities == null || audioEntities.isEmpty()) {
            sendMessage.setText(TextBuilder.playlistEmpty(playlistName));
            sendMessage.setReplyMarkup(KeyboardBuilder.homeKeyboard());
        } else {
            sendMessage.setText(TextBuilder.allAudios(audioEntities, 0, 9));
            sendMessage.setReplyMarkup(KeyboardBuilder.allAudiosKeyboard(audioEntities, 0, 9, playlistName));
        }
        return sendMessage;
    }

    @Override
    public BotApiMethod audiosPage(CallbackQuery callbackQuery, List<AudioEntity> audioEntities,
                                   int start, int end, String name, boolean next) {
        start = TextBuilder.startChecker(audioEntities, start);
        if (start == -1) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
            answerCallbackQuery.setText((next? TextBuilder.ANSWER_LAST_PAGE : TextBuilder.ANSWER_FIRST_PAGE));
            answerCallbackQuery.setShowAlert(false);
            return answerCallbackQuery;
        } else {
            Message message = callbackQuery.getMessage();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(message.getChatId());
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setText(TextBuilder.allAudios(audioEntities, start, end));
            editMessageText.setReplyMarkup(KeyboardBuilder.allAudiosKeyboard(audioEntities, start, end, name));
            return editMessageText;
        }
    }

    @Override
    public DeleteMessage deleteMessage(Message message) {
        return DeleteMessage.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .build();
    }

    @Override
    public SendAudio sendAudio(Message message, AudioEntity audio) {
        SendAudio sendAudio = new SendAudio();
        sendAudio.setAudio(new InputFile(audio.getFileId()));
        sendAudio.setChatId(message.getChatId());
        sendAudio.setCaption("\uD83D\uDD25 @" + username + " \uD83D\uDD25");
        sendAudio.setReplyMarkup(KeyboardBuilder.audioKeyboard(audio));
        return sendAudio;
    }

    @Override
    public AnswerCallbackQuery sendAnswerIsLiked(CallbackQuery callbackQuery, boolean isLiked) {
        return AnswerCallbackQuery.builder()
                .text((isLiked? TextBuilder.ANSWER_LIKED : TextBuilder.ANSWER_DISLIKED))
                .showAlert(false)
                .callbackQueryId(callbackQuery.getId())
                .build();
    }

    @Override
    public SendMessage deletePlaylist(Message message, List<Playlist> playlists) {
        return SendMessage.builder()
                .text(TextBuilder.DELETE_PLAYLIST)
                .chatId(message.getChatId())
                .replyMarkup(KeyboardBuilder.playlistHomeKeyboard(playlists))
                .build();
    }

    @Override
    public SendMessage playlistDeleted(Message message, boolean success) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(success? TextBuilder.PLAYLIST_DELETED : TextBuilder.PLAYLIST_NOT_FOUND)
                .replyMarkup(KeyboardBuilder.homeKeyboard())
                .build();
    }
}
