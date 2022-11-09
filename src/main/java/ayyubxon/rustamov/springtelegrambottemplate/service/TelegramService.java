package ayyubxon.rustamov.springtelegrambottemplate.service;

import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface TelegramService {

    SendMessage start(Message message);

    SendMessage home(Message message);

    SendMessage namingPlaylist(Message message);

    SendMessage playlistNameExist(Message message, String name);

    SendMessage playlistCreated(Message message, String name);

    SendMessage selectPlaylist(Message message, List<Playlist> playlists);

    SendMessage audioSaved(Message message);

    SendMessage allPlaylists(Message message, List<Playlist> playlists);

    SendAudio sendAudio(Message message, AudioEntity audioEntity);

    SendMessage allAudiosFirstPage(Message message, List<AudioEntity> audioEntities);

    SendMessage playlistAudiosFirstPage(Message message, List<AudioEntity> audioEntities, String playlistName);

    BotApiMethod<Message> audiosPage(CallbackQuery callbackQuery, List<AudioEntity> audioEntities, int start, int end,
                            String name, boolean next);

    DeleteMessage deleteMessage(Message message);

    AnswerCallbackQuery sendAnswerIsLiked(CallbackQuery callbackQuery, boolean isLiked);
}
