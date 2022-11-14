package ayyubxon.rustamov.springtelegrambottemplate.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Data
@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @ManyToOne
    private User user;

    public Playlist(String name, User user) {
        this.name = name;
        this.user = user;
    }
}
