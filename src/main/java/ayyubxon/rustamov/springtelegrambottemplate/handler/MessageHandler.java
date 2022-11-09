package ayyubxon.rustamov.springtelegrambottemplate.handler;

import ayyubxon.rustamov.springtelegrambottemplate.dto.ServiceResponse;
import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import ayyubxon.rustamov.springtelegrambottemplate.entity.User;
import ayyubxon.rustamov.springtelegrambottemplate.entity.enums.State;
import ayyubxon.rustamov.springtelegrambottemplate.sender.Sender;
import ayyubxon.rustamov.springtelegrambottemplate.service.AudioEntityService;
import ayyubxon.rustamov.springtelegrambottemplate.service.PlaylistService;
import ayyubxon.rustamov.springtelegrambottemplate.service.TelegramService;
import ayyubxon.rustamov.springtelegrambottemplate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageHandler implements Handler<Message> {

    final UserService userService;
    final AudioEntityService audioEntityService;
    final PlaylistService playlistService;
    final TelegramService telegramService;
    final Sender sender;

    @Override
    public void choose(Message message) {
        System.out.println("\nMessage: " + message.getText());
        org.telegram.telegrambots.meta.api.objects.User from = message.getFrom();

        ServiceResponse<User> response = userService.getOne(message.getChatId());
        User user;
        if (response.isSuccess()) {
            user = response.getData();
        } else {
            User newUser = new User(from.getId(), from.getFirstName() + " " + from.getLastName(),
                    from.getUserName(), State.START);
            user = userService.save(newUser).getData();
            sender.send(telegramService.start(message));
        }
        System.out.println(user);

        List<Playlist> playlists = playlistService.getAllByUser(user).getData();

        if (message.hasAudio()) {
            Audio audio = message.getAudio();
            AudioEntity audioEntity = new AudioEntity(audio.getFileId(), audio.getFileUniqueId(),
                    audio.getDuration(), audio.getMimeType(), audio.getFileSize(), audio.getTitle(),
                    audio.getPerformer(), audio.getFileName());
            audioEntity = audioEntityService.save(audioEntity).getData();
            user.setTempAudioId(audioEntity.getId());
            user.setState(State.AUDIO_EXIST);
            userService.save(user);
            sender.send(telegramService.selectPlaylist(message, playlistService.getAllByUser(user).getData()));

        } else if (message.hasText()) {

            if (message.getText().equals("/start") & user.getState() != State.START) {
                sender.send(telegramService.start(message));
                user.setState(State.START);
                userService.save(user);

            }else if (message.getText().equals("\uD83C\uDFE0 Bosh menyu")){
                user.setTempAudioId(null);
                userService.save(user);
                sender.send(telegramService.home(message));

            } else if (message.getText().equals("\uD83C\uDFB6 Yangi playlist")) {
                user.setState(State.NAMING_PLAYLIST);
                sender.send(telegramService.namingPlaylist(message));
                userService.save(user);

            } else if (message.getText().equals("\uD83C\uDFB5 Playlistlar")) {
                sender.send(telegramService.allPlaylists(message, playlists));

            } else if (message.getText().equals("\uD83C\uDFB5 Barcha qo'shiqlar")) {
                List<AudioEntity> audioEntities = audioEntityService.getAllByUser(user).getData();
                sender.send(telegramService.allAudiosFirstPage(message, audioEntities));

            } else if (user.getState() == State.AUDIO_EXIST & !nameChecker(playlists, message.getText())) {
                AudioEntity audioEntity = audioEntityService.getOne(user.getTempAudioId()).getData();
                audioEntity.setPlaylist(playlistService.getByUserAndName(user, message.getText()).getData());
                audioEntityService.save(audioEntity);
                sender.send(telegramService.audioSaved(message));
                user.setTempAudioId(null);
                user.setState(State.MAIN_MENU);
                userService.save(user);

            } else if (!nameChecker(playlists, message.getText()) & user.getState() == State.MAIN_MENU) {
                System.out.println("Playlist: " + message.getText());
                List<AudioEntity> audioEntities = audioEntityService.getAllByUserAndPlaylist(user, playlistService.getByUserAndName(
                        user, message.getText()).getData()).getData();
                System.out.println(audioEntities);
                sender.send(telegramService.playlistAudiosFirstPage(message, audioEntities, message.getText()));

            } else if (user.getState() == State.NAMING_PLAYLIST) {
                Playlist newPlaylist = new Playlist(message.getText(), user);
                if (nameChecker(playlists, newPlaylist.getName())) {
                    playlistService.save(newPlaylist);
                    sender.send(telegramService.playlistCreated(message, newPlaylist.getName()));
                    if (user.getTempAudioId() != null) {
                        AudioEntity audioEntity = audioEntityService.getOne(user.getTempAudioId()).getData();
                        audioEntity.setPlaylist(newPlaylist);
                        audioEntityService.save(audioEntity);
                        sender.send(telegramService.audioSaved(message));
                        user.setTempAudioId(null);
                    }
                    user.setState(State.MAIN_MENU);
                    userService.save(user);
                } else {
                    sender.send(telegramService.playlistNameExist(message, newPlaylist.getName()));
                }
            }
        }
    }

    public boolean nameChecker(List<Playlist> playlists, String name) {
        if (playlists.isEmpty()) return true;
        for (Playlist playlist : playlists)
            if (playlist.getName().equals(name))
                return false;
        return true;
    }
}