package ayyubxon.rustamov.springtelegrambottemplate.builder;

import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class KeyboardBuilder {

    public InlineKeyboardMarkup languageKeyboard() {
        return new InlineKeyboardMarkup(Collections.singletonList(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text("\uD83C\uDDFA\uD83C\uDDFF O'zbek")
                        .callbackData("LANGUAGE#UZ")
                        .build(),
                InlineKeyboardButton.builder()
                        .text("\uD83C\uDDEC\uD83C\uDDE7 English")
                        .callbackData("LANGUAGE#EN")
                        .build()
        )));
    }

    public ReplyKeyboardMarkup playlistKeyboard(List<Playlist> playlists, boolean uz) {
        List<KeyboardRow> rows = new ArrayList<>();
        for (Playlist playlist : playlists)
            rows.add(new KeyboardRow(Collections.singletonList(new KeyboardButton(playlist.getName()))));
        return new ReplyKeyboardMarkup(rows, true, true, true,
                (uz ? "Playlist tanlang" : "Choose a playlist"));
    }

    public ReplyKeyboardMarkup homeKeyboard(boolean uz) {
        return new ReplyKeyboardMarkup(
                Arrays.asList(
                        new KeyboardRow(Arrays.asList(
                                new KeyboardButton("\uD83C\uDFB5 " + (uz ? "Playlistlar" : "Playlists")),
                                new KeyboardButton("\uD83C\uDFB5 " + (uz ? "Barcha qo'shiqlar" : "All songs"))
                        )),
                        new KeyboardRow(Collections.singletonList(
                                new KeyboardButton("\uD83C\uDFB6 " + (uz ? "Yangi playlist" : "New playlist"))
                        ))
                ), true, true, true, (uz ? "Tanlang" : "Choose")
        );
    }

    public ReplyKeyboardMarkup playlistHomeKeyboard(List<Playlist> playlists, boolean uz) {
        ReplyKeyboardMarkup keyboardMarkup = playlistKeyboard(playlists, uz);
        keyboardMarkup.getKeyboard().add(new KeyboardRow(Collections.singletonList(
                new KeyboardButton("\uD83C\uDFE0 " + (uz ? "Bosh menyu" : "Main menu")))));
        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup playlistNewKeyboard(List<Playlist> playlists, boolean uz) {
        ReplyKeyboardMarkup keyboardMarkup = playlistKeyboard(playlists, uz);
        keyboardMarkup.getKeyboard().add(new KeyboardRow(Collections.singletonList(
                new KeyboardButton("\uD83C\uDFB6 " + (uz ? "Yangi playlist" : "New playlist")))));
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup allAudiosKeyboard(List<AudioEntity> audioEntities, int start, int end,
                                                  String playlistName) {
        int tempEnd = TextBuilder.endChecker(audioEntities, end);
        int btnQuantity = tempEnd - start + 1;
        System.out.println("BtnQuantity: " + btnQuantity);
        audioEntities = audioEntities.subList(start, tempEnd + 1);
        System.out.println("Start = " + start + "  End = " + tempEnd);
        System.out.println("KeyboardBuilder: " + audioEntities);
        List<List<InlineKeyboardButton>> rowButtonList = new ArrayList<>();
        if (btnQuantity <= 5) {
            List<InlineKeyboardButton> row = new ArrayList<>();
            for (int i = 0; i < btnQuantity; i++) {
                row.add(InlineKeyboardButton.builder()
                        .text(String.valueOf(i + 1))
//                        CallbackData: BtnNum#audioFileId
                        .callbackData((i + 1) + "#" + audioEntities.get(i).getId())
                        .build());
            }
            rowButtonList.add(row);
        } else {
            List<InlineKeyboardButton> row1 = new ArrayList<>();
            List<InlineKeyboardButton> row2 = new ArrayList<>();
            for (int i = 0; i < btnQuantity / 2; i++) {
                row1.add(InlineKeyboardButton.builder()
                        .text(String.valueOf(i + 1))
//                        CallbackData: BtnNum#audioFileId
                        .callbackData((i + 1) + "#" + audioEntities.get(i).getId())
                        .build());
                row2.add(InlineKeyboardButton.builder()
                        .text(String.valueOf(i + 1 + btnQuantity / 2))
//                        CallbackData: BtnNum#audioFileId
                        .callbackData((i + 1 + btnQuantity / 2) + "#" + audioEntities.get(i + btnQuantity / 2).getId())
                        .build());
            }
            if (btnQuantity % 2 == 1) {
                row2.add(InlineKeyboardButton.builder()
                        .text(String.valueOf(btnQuantity))
//                        CallbackData: BtnNum#audioFileId
                        .callbackData((btnQuantity) + "#" + audioEntities.get(btnQuantity - 1).getId())
                        .build());
            }
            rowButtonList.add(row1);
            rowButtonList.add(row2);
        }
        rowButtonList.add(Arrays.asList(
                InlineKeyboardButton.builder()
                        .text("⬅️")
//                            CallbackData: BtnName#playlistName#start#end
                        .callbackData("PREVIOUS#" + playlistName + "#" + (start - 10) + "#" + (end - 10))
                        .build(),
                InlineKeyboardButton.builder()
                        .text("❌")
                        .callbackData("DELETE")
                        .build(),
                InlineKeyboardButton.builder()
                        .text("➡️")
//                            CallbackData: BtnName#playlistName#start#end
                        .callbackData("NEXT#" + playlistName + "#" + (start + 10) + "#" + (end + 10))
                        .build()
        ));
        return new InlineKeyboardMarkup(rowButtonList);
    }

    public InlineKeyboardMarkup audioKeyboard(AudioEntity audioEntity) {
        return new InlineKeyboardMarkup(Collections.singletonList(Arrays.asList(
                        InlineKeyboardButton.builder()
                                .text("❤️/\uD83D\uDC94")
                                .callbackData("LIKE#" + audioEntity.getId())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("❌")
                                .callbackData("DELETE")
                                .build()
                )
        ));
    }
}
