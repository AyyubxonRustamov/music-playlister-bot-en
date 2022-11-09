package ayyubxon.rustamov.springtelegrambottemplate.repository;

import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AudioEntityRepository extends JpaRepository<AudioEntity, Long> {

    @Query(value = "select * from audio_entity " +
            "where audio_entity.playlist_id = (select id from playlist where playlist.user_chat_id = :userId)", nativeQuery = true)
    List<AudioEntity> findAllByUser(@Param("userId") Long userId);

    @Query(value = "select * from audio_entity " +
            "where audio_entity.playlist_id = (select id from playlist where playlist.user_chat_id = ?1 " +
            "and playlist.name = ?2)", nativeQuery = true)
    List<AudioEntity> findAllByUserAndPlaylist(Long userId, String playlistName);
}
