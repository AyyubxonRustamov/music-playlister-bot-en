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

    SendMessage selectLanguage(Message message);

    SendMessage commands(CallbackQuery callbackQuery, boolean start, boolean uz);

    SendMessage commands(Message message, boolean start, boolean uz);

    SendMessage home(Message message, boolean uz);

    SendMessage namingPlaylist(Message message, boolean uz);

    SendMessage playlistNameExist(Message message, String name, boolean uz);

    SendMessage playlistCreated(Message message, String name, boolean uz);

    SendMessage selectPlaylist(Message message, List<Playlist> playlists, boolean uz);

    SendMessage audioSaved(Message message, boolean uz);

    SendMessage allPlaylists(Message message, List<Playlist> playlists, boolean uz);

    SendAudio sendAudio(Message message, AudioEntity audioEntity);

    SendMessage allAudiosFirstPage(Message message, List<AudioEntity> audioEntities, boolean uz);

    SendMessage playlistAudiosFirstPage(Message message, List<AudioEntity> audioEntities, String playlistName, boolean uz);

    SendMessage likedAudiosFirstPage(Message message, List<AudioEntity> audioEntities, boolean uz);

    BotApiMethod audiosPage(CallbackQuery callbackQuery, List<AudioEntity> audioEntities, int start, int end,
                            String name, boolean next, boolean uz);

    DeleteMessage deleteMessage(Message message);

    AnswerCallbackQuery sendAnswerIsLiked(CallbackQuery callbackQuery, boolean isLiked, boolean uz);

    SendMessage deletePlaylist(Message message, List<Playlist> playlists, boolean uz);

    SendMessage playlistDeleted(Message message, boolean success, boolean uz);
}
