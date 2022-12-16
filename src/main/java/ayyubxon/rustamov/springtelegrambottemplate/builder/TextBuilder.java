package ayyubxon.rustamov.springtelegrambottemplate.builder;

import ayyubxon.rustamov.springtelegrambottemplate.checker.UsernameChecker;
import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Service
@RequiredArgsConstructor
@Data
public class TextBuilder {

    private final UsernameChecker usernameChecker;

    public final String selectLanguage = """
            🇺🇿 Iltimos, tilni tanlang:
            🇬🇧 Please, choose a language:
            """;
    public final String homeUz = "\uD83C\uDFE0 Asosiy menyu";
    public final String homeEn = "\uD83C\uDFE0 Main menu";
    public final String namingPlaylistUz = "Yangi playlist uchun nom bering?";
    public final String namingPlaylistEn = "Select a name for new playlist?";
    public final String selectPlaylistUz = "\uD83C\uDFA7 Qo'shiqni saqlash uchun playlistni tanlang yoki yangi playlist yarating!";
    public final String selectPlaylistEn = "\uD83C\uDFA7 Select a playlist to add the song or create new playlist!";
    public final String selectPlaylistEmptyUz = "\uD83C\uDFA7 Sizda hozircha playlistlar yo'q! Iltimos, yangi playlist yarating!";
    public final String selectPlaylistEmptyEn = "\uD83C\uDFA7 Currently you don't have playlists! Please, create new playlist!";
    public final String audioSavedUz = "✅ Qo'shiq playlistga qo'shildi!";
    public final String audioSavedEn = "✅ The song is added to the playlist!";
    public final String allPlaylistsUz = "\uD83C\uDFA7 Sizning playlistlaringiz";
    public final String allPlaylistsEn = "\uD83C\uDFA7 Your playlists";
    public final String allPlaylistsEmptyUz = "\uD83D\uDEAB Sizda hozircha hechqanday playlistlar yo'q!";
    public final String allPlaylistsEmptyEn = "\uD83D\uDEAB Currently you don't have any playlists!";
    public final String allAudiosEmptyUz = "\uD83D\uDEAB Sizda hozircha hechqanday qo'shiqlar yo'q!";
    public final String allAudiosEmptyEn = "\uD83D\uDEAB Currently you don't have any songs!";
    public final String answerFirstPageUz = "Siz allaqachon birinchi sahifadasiz!";
    public final String answerFirstPageEn = "You are already at the first page!";
    public final String answerLastPageUz = "Siz allaqachon oxirgi sahifadasiz!";
    public final String answerLastPageEn = "You are already at the last page!";
    public final String answerLikedUz = "❤️ Sevimlilar ro'yxatiga qo'shildi (/liked)";
    public final String answerLikedEn = "❤️ The song is added to liked playlist (/liked)";
    public final String answerDislikedUz = "\uD83D\uDC94 Sevimlilar ro'yxatidan o'chirildi (/liked)";
    public final String answerDislikedEn = "\uD83D\uDC94 The song is removed from liked playlist (/liked)";
    public final String deletePlaylistUz = "\uD83D\uDDD1 O'chirish uchun playlistni tanlang. Playlistdagi mavjud " +
            "qo'shiqlar playlist bilan birga o'chiriladi va qayta tiklab bo'lmaydi!";
    public final String deletePlaylistEn = "\uD83D\uDDD1 Select the playlist to delete. The songs in the playlist are deleted " +
            "within playlist. It's impossible to restore them later!";
    public final String playlistDeletedUz = "Playlist o'chirildi!";
    public final String playlistDeletedEn = "Playlist is deleted!";
    public final String playlistNotFoundUz = "\uD83C\uDFA7 Playlist topilmadi!";
    public final String playlistNotFoundEn = "\uD83C\uDFA7 Playlist not found!";
    public final String commandsUz = """
            Buyruqlar:
            /start - Botni ishga tushirish
            /main_menu - Bosh menyu
            /liked - Sevimli qo'shiqlar
            /del_playlist - Playlistni o'chirish
            /contact - Bot administratori bilan bog'lanish""";
    public final String commandsEn = """
            Commands:
            /start - Start the bot
            /main_menu - Main menu
            /liked - Liked songs
            /del_playlist - Delete a playlist
            /contact - Contact with the bot admins""";
    public final String likedPlaylistEmptyUz = "Sevimlilar ro'yxati hozircha bo'sh!";
    public final String likedPlaylistEmptyEn = "Liked playlist is empty!";

    public String startMessage(User from, boolean uz) {
        if (uz)
            return "Assalomu alaykum " + usernameChecker.check(from) + ". Men musiqalaringizni turli " +
                    "xil playlistlar bilan tartibga solishga yordam beraman. O'zingiz xohlagandek playlistlar " +
                    "yarating va musiqadan zavqlaning!";
        return "Hello " + usernameChecker.check(from) + ". I can help you to sort your songs with different " +
                "playlists. Create playlists as you like and enjoy listening to songs!";
    }

    public String playlistNameExist(String name, boolean uz) {
        if (uz)
            return name + " nomli playlist sizda mavjud. Iltimos boshqa nom kiriting!";
        return "You already have a playlist named " + name + ". Please choose another name!";
    }

    public String playlistEmpty(String name, boolean uz) {
        if (uz)
            return name + " nomli playlist hozircha bo'sh!";
        return "Playlist named " + name + " is currently empty!";
    }

    public String playlistCreated(String name, boolean uz) {
        if (uz)
            return "✅ " + name + " nomli playlist yaratildi!";
        return "✅ Playlist named " + name + " is created!";
    }

    public String allAudios(List<AudioEntity> audioEntities, int start, int end, boolean uz) {
        end = endChecker(audioEntities, end);
        String message = "<b>" + (uz ? "Natijalar: " : "Results: ") + "</b>" + (start + 1) + "-" + (end + 1) + " " + audioEntities.size() + " dan\n";
        audioEntities = audioEntities.subList(start, end + 1);
        for (int i = 0; i < audioEntities.size(); i++) {
            AudioEntity audio = audioEntities.get(i);
            message = message.concat("\n<b>" + (i + 1) + ".</b> " + audio.getPerformer() + " - " + audio.getTitle() + " " +
                    secondsToHours(audio.getDuration()) + " " + byteCountToDisplaySize(audio.getFileSize()));
        }
        return message;
    }

    public String secondsToHours(int duration) {
        int hours = duration / 3600;
        int minutes = (duration % 3600) / 60;
        int seconds = duration % 60;

        if (hours <= 0) return String.format("%02d:%02d", minutes, seconds);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public String byteCountToDisplaySize(Long size) {
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
            return list.size() - 1;
        return end;
    }

    public static int startChecker(List<?> list, int start) {
        if (start >= list.size() | start < 0)
            return -1;
        return start;
    }
}
