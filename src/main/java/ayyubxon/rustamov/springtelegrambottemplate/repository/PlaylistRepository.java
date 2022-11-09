package ayyubxon.rustamov.springtelegrambottemplate.repository;

import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import ayyubxon.rustamov.springtelegrambottemplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findAllByUser(User user);

    Optional<Playlist> findByUserAndName(User user, String name);
}
