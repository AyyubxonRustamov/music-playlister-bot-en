package ayyubxon.rustamov.springtelegrambottemplate.handler;

import ayyubxon.rustamov.springtelegrambottemplate.dto.ServiceResponse;
import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import ayyubxon.rustamov.springtelegrambottemplate.entity.User;
import ayyubxon.rustamov.springtelegrambottemplate.entity.enums.State;
import ayyubxon.rustamov.springtelegrambottemplate.sender.Sender;
import ayyubxon.rustamov.springtelegrambottemplate.service.AudioEntityService;
import ayyubxon.rustamov.springtelegrambottemplate.service.PlaylistService;
import ayyubxon.rustamov.springtelegrambottemplate.service.TelegramService;
import ayyubxon.rustamov.springtelegrambottemplate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler implements Handler<CallbackQuery> {

    final UserService userService;
    final PlaylistService playlistService;
    final AudioEntityService audioEntityService;
    final TelegramService telegramService;
    final Sender sender;

    @Override
    public void choose(CallbackQuery callbackQuery) {

        System.out.println("\nCallbackQuery: " + callbackQuery);
        org.telegram.telegrambots.meta.api.objects.User from = callbackQuery.getFrom();

//        CallbacData: BtnNum#AudioFileID, BtnName#start#end
        String[] callbackData = callbackQuery.getData().split("#");
        Message message = callbackQuery.getMessage();

        ServiceResponse<User> response = userService.getOne(message.getChatId());
        User user;
        if (response.isSuccess()) {
            user = response.getData();
        } else {
            User newUser = new User(from.getId(), from.getFirstName() + " " + from.getLastName(),
                    from.getUserName());
            user = userService.save(newUser).getData();
        }

        if (callbackData[0].equals("LANGUAGE")) {
            user.setUz(callbackData[1].equals("UZ"));
            user.setState(State.START);
            sender.send(telegramService.commands(callbackQuery, true, user.isUz()));
            sender.send(telegramService.commands(callbackQuery, false, user.isUz()));
            userService.save(user);
        } else if (callbackData[0].matches("[0-9]+")) {
            sender.send(telegramService.sendAudio(message, audioEntityService.getOne(
                    Long.parseLong(callbackData[1])).getData()));
        } else if (callbackData[0].equals("NEXT") | callbackData[0].equals("PREVIOUS")) {
            List<AudioEntity> audioEntities;
            if (callbackData[1].equals("ALLAUDIOS")) {
                audioEntities = audioEntityService.getAllByUser(user).getData();
            } else if (callbackData[1].equals("LIKED")) {
                audioEntities = audioEntityService.getAllLikedByUser(user).getData();
            } else {
                audioEntities = audioEntityService.getAllByUserAndPlaylist(user, playlistService.getByUserAndName(
                        user, callbackData[1]).getData()).getData();
            }

            sender.send(telegramService.audiosPage(callbackQuery, audioEntities, Integer.parseInt(callbackData[2]),
                    Integer.parseInt(callbackData[3]), callbackData[1], callbackData[0].equals("NEXT"), user.isUz()));
        } else if (callbackData[0].equals("DELETE")) {
            sender.send(telegramService.deleteMessage(message));
        } else if (callbackData[0].equals("LIKE")) {
            AudioEntity audio = audioEntityService.getOne(Long.parseLong(callbackData[1])).getData();
            audio.setLiked(!audio.isLiked());
            AudioEntity save = audioEntityService.save(audio).getData();
            sender.send(telegramService.sendAnswerIsLiked(callbackQuery, save.isLiked(), user.isUz()));
        }
    }
}
