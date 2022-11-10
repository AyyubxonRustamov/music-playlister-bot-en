package ayyubxon.rustamov.springtelegrambottemplate.service;

import ayyubxon.rustamov.springtelegrambottemplate.dto.ServiceResponse;
import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import ayyubxon.rustamov.springtelegrambottemplate.entity.User;
import ayyubxon.rustamov.springtelegrambottemplate.repository.PlaylistRepository;
import ayyubxon.rustamov.springtelegrambottemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    final PlaylistRepository playlistRepository;
    final UserRepository userRepository;

    public ServiceResponse<Playlist> save(Playlist playlist) {
        Optional<User> optionalUser = userRepository.findById(playlist.getUser().getChatId());
        if (optionalUser.isEmpty()) return new ServiceResponse<>("User Not Found!", false);
        Playlist save = playlistRepository.save(playlist);
        return new ServiceResponse<>("Success!", true, save);
    }

    public ServiceResponse<List<Playlist>> getAllByUser(User user){
        List<Playlist> playlists = playlistRepository.findAllByUser(user);
        if (playlists.isEmpty()) new ServiceResponse<>("Playlists Not Found!", false);
        return new ServiceResponse<>("Success!", true, playlists);
    }

    public ServiceResponse<Playlist> getByUserAndName(User user, String name){
        Optional<Playlist> optionalPlaylist = playlistRepository.findByUserAndName(user, name);
        return optionalPlaylist.map(playlist -> new ServiceResponse<>("Success!", true, playlist)).orElseGet(() ->
                new ServiceResponse<>("Playlist not found!", false));
    }

    public ServiceResponse<Playlist> getOne(Long id) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        return optionalPlaylist.map(playlist -> new ServiceResponse<>("Success!", true, playlist)).orElseGet(() ->
                new ServiceResponse<>("Playlist not found!", false));
    }

    public ServiceResponse<List<Playlist>> getAll() {
        List<Playlist> all = playlistRepository.findAll();
        return new ServiceResponse<>("Success!", true, all);
    }

    public  ServiceResponse<?> deleteByUserAndName(User user, String name){
        Optional<Playlist> optionalPlaylist = playlistRepository.findByUserAndName(user, name);
        if (optionalPlaylist.isEmpty())
            return new ServiceResponse<>("Playlist Not Found!", false);
        playlistRepository.delete(optionalPlaylist.get());
        return new ServiceResponse<>("Success!", true);
    }

}
