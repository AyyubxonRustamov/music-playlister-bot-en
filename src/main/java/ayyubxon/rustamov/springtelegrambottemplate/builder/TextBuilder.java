package ayyubxon.rustamov.springtelegrambottemplate.builder;

import ayyubxon.rustamov.springtelegrambottemplate.checker.UsernameChecker;
import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class TextBuilder {

    private final static UsernameChecker usernameChecker = new UsernameChecker();

    public static String HOME = "\uD83C\uDFE0 Asosiy menyu";
    public static String NAMING_PLAYLIST = "Yangi playlist uchun nom bering?";
    public static String SELECT_PLAYLIST = "\uD83C\uDFA7 Qo'shiqni saqlash uchun playlistni tanlang yoki yangi playlist yarating!";
    public static String SELECT_PLAYLIST_EMPTY = "\uD83C\uDFA7 Sizda hozircha playlistlar yo'q! Iltimos yangi playlist yarating!";
    public static String AUDIO_SAVED = "✅ Qo'shiq playlistga qo'shildi!";
    public static String ALL_PLAYLISTS = "\uD83C\uDFA7 Sizning playlistlaringiz";
    public static String ALL_PLAYLISTS_EMPTY = "\uD83D\uDEAB Sizda hozircha hechqanday playlistlar yo'q!";
    public static String ALL_AUDIOS_EMPTY = "\uD83D\uDEAB Sizda hozircha hechqanday qo'shiqlar yo'q!";
    public static String ANSWER_FIRST_PAGE = "Siz allaqachon birinchi sahifadasiz!";
    public static String ANSWER_LAST_PAGE = "Siz allaqachon oxirgi sahifadasiz!";
    public static String ANSWER_LIKED = "❤️ Sizning playlistingizga qo'shildi (/liked)";
    public static String ANSWER_DISLIKED = "\uD83D\uDC94 Sizning playlistingizdan o'chirildi (/liked)";

    public static String startMessage(User from) {
        return "Assalomu alaykum " + usernameChecker.check(from) + ". Men musiqalaringizni turli " +
                "xil playlistlar bilan tartibga solishga yordam beraman. O'zingiz xohlagandek playlistlar " +
                "yarating va musiqadan zavqlaning!";
    }

    public static String playlistNameExist(String name) {
        return name + " nomli playlist sizda mavjud. Iltimos boshqa nom kiriting!";
    }

    public static String playlistEmpty(String name) {
        return name + " nomli playlist hozircha bo'sh!";
    }

    public static String playlistCreated(String name) {
        return "✅ " + name + " nomli playlist yaratildi!";
    }

    public static String allAudios(List<AudioEntity> audioEntities, int start, int end) {
        end = endChecker(audioEntities, end);
        String message = "Natijalar " + (start+1) + "-" + (end+1) + " " + audioEntities.size() + " dan\n";
        audioEntities = audioEntities.subList(start, end+1);
        for (int i = start; i < audioEntities.size(); i++) {
            AudioEntity audio = audioEntities.get(i);
            message = message.concat("\n*" + (i + 1) + ".* " + audio.getTitle() + " " +
                    secondsToHours(audio.getDuration()) + " " + byteCountToDisplaySize(audio.getFileSize()));
        }
        return message;
    }

    public static String secondsToHours(int duration) {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;

        if (hours <= 0) return String.format("%02d:%02d", minutes, seconds);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static String byteCountToDisplaySize(Long size) {
        if (size >= Math.pow(2, 30))
            return String.format("%3.1fGB", size / Math.pow(2, 30));
        else if (size >= Math.pow(2, 20))
            return String.format("%3.1fMB", size / Math.pow(2, 20));
        else if (size >= Math.pow(2, 10))
            return String.format("%3.1fKB", size / Math.pow(2, 10));
        else
            return (size + "B");
    }

    public static int endChecker(List<?> list, int end) {
        if (end >= list.size())
            return (list.size() % 10) - 1;
        return end;
    }

    public static int startChecker(List<?> list, int start) {
        if (start >= list.size() | start < 0)
            return -1;
        return start;
    }
}
