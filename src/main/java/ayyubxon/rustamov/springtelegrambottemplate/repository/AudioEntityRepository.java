package ayyubxon.rustamov.springtelegrambottemplate.repository;

import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AudioEntityRepository extends JpaRepository<AudioEntity, Long> {

    @Query(value = """
            SELECT *
            FROM audio_entity ae
            WHERE ae.playlist_id
            IN (
                SELECT id
                FROM playlist p
                WHERE p.user_chat_id = ?1)""", nativeQuery = true)
    List<AudioEntity> findAllByUser(Long userId);

    @Query(value = """
            SELECT *
            FROM audio_entity ae
            WHERE ae.playlist_id = (
                SELECT id
                FROM playlist p
                WHERE p.user_chat_id = ?1
                AND p.name = ?2)""", nativeQuery = true)
    List<AudioEntity> findAllByUserAndPlaylist(Long userId, String playlistName);

    @Query(value = """
            SELECT *
            FROM audio_entity ae
            WHERE ae.playlist_id
            IN (
                SELECT id
                FROM playlist p
                WHERE p.user_chat_id = ?1
                )
            AND ae.liked = TRUE""", nativeQuery = true)
    List<AudioEntity> findAllLikedByUser(Long chatId);
}