package ayyubxon.rustamov.springtelegrambottemplate.service;

import ayyubxon.rustamov.springtelegrambottemplate.builder.KeyboardBuilder;
import ayyubxon.rustamov.springtelegrambottemplate.builder.TextBuilder;
import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import lombok.RequiredArgsConstructor;
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

    private final TextBuilder textBuilder;
    private final KeyboardBuilder keyboardBuilder;

    @Override
    public SendMessage commands(CallbackQuery callbackQuery, boolean start, boolean uz) {
        return SendMessage.builder()
                .text(start ? textBuilder.startMessage(callbackQuery.getFrom(), uz) :
                        (uz ? textBuilder.getCommandsUz() : textBuilder.getCommandsEn()))
                .parseMode(ParseMode.HTML)
                .chatId(callbackQuery.getMessage().getChatId())
                .replyMarkup(keyboardBuilder.homeKeyboard(uz))
                .build();
    }

    @Override
    public SendMessage commands(Message message, boolean start, boolean uz) {
        return SendMessage.builder()
                .text(start ? textBuilder.startMessage(message.getFrom(), uz) :
                        (uz ? textBuilder.getCommandsUz() : textBuilder.getCommandsEn()))
                .parseMode(ParseMode.HTML)
                .chatId(message.getChatId())
                .replyMarkup(keyboardBuilder.homeKeyboard(uz))
                .build();
    }

    @Override
    public SendMessage selectLanguage(Message message) {
        return SendMessage.builder()
                .text(textBuilder.getSelectLanguage())
                .replyMarkup(keyboardBuilder.languageKeyboard())
                .chatId(message.getChatId())
                .build();
    }

    @Override
    public SendMessage home(Message message, boolean uz) {
        return SendMessage.builder()
                .text(uz ? textBuilder.getHomeUz() : textBuilder.getHomeEn())
                .chatId(message.getChatId())
                .replyMarkup(keyboardBuilder.homeKeyboard(uz))
                .build();
    }

    @Override
    public SendMessage namingPlaylist(Message message, boolean uz) {
        return SendMessage.builder()
                .text(uz ? textBuilder.getNamingPlaylistUz() : textBuilder.getNamingPlaylistEn())
                .chatId(message.getChatId())
                .replyMarkup(new ReplyKeyboardRemove(true))
                .build();
    }

    @Override
    public SendMessage playlistNameExist(Message message, String name, boolean uz) {
        return SendMessage.builder()
                .text(textBuilder.playlistNameExist(name, uz))
                .chatId(message.getChatId())
                .replyMarkup(new ReplyKeyboardRemove(true))
                .build();
    }

    @Override
    public SendMessage playlistCreated(Message message, String name, boolean uz) {
        return SendMessage.builder()
                .text(textBuilder.playlistCreated(name, uz))
                .chatId(message.getChatId())
                .replyMarkup(keyboardBuilder.homeKeyboard(uz))
                .build();
    }

    @Override
    public SendMessage selectPlaylist(Message message, List<Playlist> playlists, boolean uz) {

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(uz ? textBuilder.getSelectPlaylistUz() : textBuilder.getSelectPlaylistEn());
        sendMessage.setReplyMarkup(keyboardBuilder.playlistNewKeyboard(playlists, uz));
        sendMessage.setReplyToMessageId(message.getMessageId());
        if (playlists == null || playlists.isEmpty())
            sendMessage.setText(uz ? textBuilder.getSelectPlaylistEmptyUz() : textBuilder.getSelectPlaylistEmptyEn());

        return sendMessage;
    }

    @Override
    public SendMessage audioSaved(Message message, boolean uz) {
        return SendMessage.builder()
                .text(uz ? textBuilder.getAudioSavedUz() : textBuilder.getAudioSavedEn())
                .chatId(message.getChatId())
                .replyMarkup(keyboardBuilder.homeKeyboard(uz))
                .build();
    }

    @Override
    public SendMessage allPlaylists(Message message, List<Playlist> playlists, boolean uz) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText((playlists == null || playlists.isEmpty()) ?
                (uz ? textBuilder.getAllPlaylistsEmptyUz() : textBuilder.getAllPlaylistsEmptyEn()) :
                (uz ? textBuilder.getAllPlaylistsUz() : textBuilder.getAllPlaylistsEn()));
        sendMessage.setReplyMarkup(keyboardBuilder.playlistHomeKeyboard(playlists, uz));
        return sendMessage;
    }

    @Override
    public SendMessage allAudiosFirstPage(Message message, List<AudioEntity> audioEntities, boolean uz) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode(ParseMode.HTML);
        if (audioEntities == null || audioEntities.isEmpty()) {
            sendMessage.setText(uz ? textBuilder.getAllAudiosEmptyUz() : textBuilder.getAllAudiosEmptyEn());
            sendMessage.setReplyMarkup(keyboardBuilder.homeKeyboard(uz));
        } else {
            sendMessage.setText(textBuilder.allAudios(audioEntities, 0, 9, uz));
            sendMessage.setReplyMarkup(keyboardBuilder.allAudiosKeyboard(audioEntities, 0, 9, "ALLAUDIOS"));
        }
        return sendMessage;
    }

    @Override
    public SendMessage playlistAudiosFirstPage(Message message, List<AudioEntity> audioEntities, String playlistName, boolean uz) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        sendMessage.setParseMode(ParseMode.HTML);
        if (audioEntities == null || audioEntities.isEmpty()) {
            sendMessage.setText(textBuilder.playlistEmpty(playlistName, uz));
            sendMessage.setReplyMarkup(keyboardBuilder.homeKeyboard(uz));
        } else {
            sendMessage.setText(textBuilder.allAudios(audioEntities, 0, 9, uz));
            sendMessage.setReplyMarkup(keyboardBuilder.allAudiosKeyboard(audioEntities, 0, 9, playlistName));
        }
        return sendMessage;
    }

    @Override
    public SendMessage likedAudiosFirstPage(Message message, List<AudioEntity> audioEntities, boolean uz) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if (audioEntities == null || audioEntities.isEmpty()) {
            sendMessage.setText(uz ? textBuilder.getLikedPlaylistEmptyUz() : textBuilder.getLikedPlaylistEmptyEn());
            sendMessage.setReplyMarkup(keyboardBuilder.homeKeyboard(uz));
        } else {
            sendMessage.setParseMode(ParseMode.HTML);
            sendMessage.setText(textBuilder.allAudios(audioEntities, 0, 9, uz));
            sendMessage.setReplyMarkup(keyboardBuilder.allAudiosKeyboard(audioEntities, 0, 9, "LIKED"));
        }
        return sendMessage;
    }

    @Override
    public BotApiMethod audiosPage(CallbackQuery callbackQuery, List<AudioEntity> audioEntities,
                                   int start, int end, String name, boolean next, boolean uz) {
        start = TextBuilder.startChecker(audioEntities, start);
        if (start == -1) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
            answerCallbackQuery.setText((next ? (uz ? textBuilder.getAnswerLastPageUz() : textBuilder.getAnswerLastPageEn()) :
                    (uz ? textBuilder.getAnswerFirstPageUz() : textBuilder.getAnswerFirstPageEn())));
            answerCallbackQuery.setShowAlert(false);
            return answerCallbackQuery;
        } else {
            Message message = callbackQuery.getMessage();
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(message.getChatId());
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setParseMode(ParseMode.HTML);
            editMessageText.setText(textBuilder.allAudios(audioEntities, start, end, uz));
            editMessageText.setReplyMarkup(keyboardBuilder.allAudiosKeyboard(audioEntities, start, end, name));
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
        sendAudio.setReplyMarkup(keyboardBuilder.audioKeyboard(audio));
        return sendAudio;
    }

    @Override
    public AnswerCallbackQuery sendAnswerIsLiked(CallbackQuery callbackQuery, boolean isLiked, boolean uz) {
        return AnswerCallbackQuery.builder()
                .text(isLiked ? (uz ? textBuilder.getAnswerLikedUz() : textBuilder.getAnswerLikedEn()) :
                        (uz ? textBuilder.getAnswerDislikedUz() : textBuilder.getAnswerDislikedEn()))
                .showAlert(false)
                .callbackQueryId(callbackQuery.getId())
                .build();
    }

    @Override
    public SendMessage deletePlaylist(Message message, List<Playlist> playlists, boolean uz) {
        return SendMessage.builder()
                .text(uz ? textBuilder.getDeletePlaylistUz() : textBuilder.getDeletePlaylistEn())
                .chatId(message.getChatId())
                .replyMarkup(keyboardBuilder.playlistHomeKeyboard(playlists, uz))
                .build();
    }

    @Override
    public SendMessage playlistDeleted(Message message, boolean success, boolean uz) {
        return SendMessage.builder()
                .chatId(message.getChatId())
                .text(success ? (uz ? textBuilder.getPlaylistDeletedUz() : textBuilder.getPlaylistDeletedEn()) :
                        (uz ? textBuilder.getPlaylistNotFoundUz() : textBuilder.getPlaylistNotFoundEn()))
                .replyMarkup(keyboardBuilder.homeKeyboard(uz))
                .build();
    }
}
