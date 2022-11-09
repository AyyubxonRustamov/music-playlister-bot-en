package ayyubxon.rustamov.springtelegrambottemplate.service;

import ayyubxon.rustamov.springtelegrambottemplate.dto.ServiceResponse;
import ayyubxon.rustamov.springtelegrambottemplate.entity.AudioEntity;
import ayyubxon.rustamov.springtelegrambottemplate.entity.Playlist;
import ayyubxon.rustamov.springtelegrambottemplate.entity.User;
import ayyubxon.rustamov.springtelegrambottemplate.repository.AudioEntityRepository;
import ayyubxon.rustamov.springtelegrambottemplate.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AudioEntityService {

    final AudioEntityRepository audioEntityRepository;
    final PlaylistRepository playlistRepository;

    public ServiceResponse<AudioEntity> save(AudioEntity audioEntity) {
        AudioEntity saved;
        if (audioEntity.getPlaylist() != null) {
            Optional<Playlist> optionalAudioEntity = playlistRepository.findById(audioEntity.getPlaylist().getId());
            if (optionalAudioEntity.isEmpty()) return new ServiceResponse<>("Playlist Not Found!", false);
        }
        saved = audioEntityRepository.save(audioEntity);
        return new ServiceResponse<>("Success!", true, saved);
    }

    public ServiceResponse<AudioEntity> getOne(Long id) {
        Optional<AudioEntity> optionalAudioEntity = audioEntityRepository.findById(id);
        return optionalAudioEntity.map(audioEntity -> new ServiceResponse<>("Success!", true, audioEntity))
                .orElseGet(() -> new ServiceResponse<>("AudioEntity Not Found!", false));
    }

    public ServiceResponse<List<AudioEntity>> getAllByUser(User user) {
        List<AudioEntity> audioEntities = audioEntityRepository.findAllByUser(user.getChatId());
        if (audioEntities.isEmpty()) return new ServiceResponse<>("AudioEntities Not Found!", false);
        return new ServiceResponse<>("Success!", true, audioEntities);
    }

    public ServiceResponse<List<AudioEntity>> getAllByUserAndPlaylist(User user, Playlist playlist){
        List<AudioEntity> audioEntities = audioEntityRepository.findAllByUserAndPlaylist(
                user.getChatId(), playlist.getName());
        if (audioEntities.isEmpty()) return new ServiceResponse<>("AudioEntities Not Found!", false);
        return new ServiceResponse<>("Success!", true, audioEntities);
    }
}
