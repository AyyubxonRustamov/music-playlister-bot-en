package ayyubxon.rustamov.springtelegrambottemplate.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class AudioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileId;

    private String fileUniqueId;

    @ManyToOne
    private Playlist playlist;

    private Integer duration;

    private String mimeType;

    private Long fileSize;

    private String title;

    private String performer;

    private String fileName;

    private boolean liked = false;

    public AudioEntity(String fileId, String fileUniqueId, Integer duration, String mimeType, Long fileSize,
                       String title, String performer, String fileName) {
        this.fileId = fileId;
        this.fileUniqueId = fileUniqueId;
        this.duration = duration;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.title = title;
        this.performer = performer;
        this.fileName = fileName;
    }
}
