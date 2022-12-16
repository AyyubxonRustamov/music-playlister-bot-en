package ayyubxon.rustamov.springtelegrambottemplate.entity;

import ayyubxon.rustamov.springtelegrambottemplate.entity.enums.State;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;


@NoArgsConstructor
@Data
@Entity(name = "users")
public class User {

    @Id
    private Long chatId;

    private String name;

    private String username;

    @Enumerated(EnumType.STRING)
    private State state = State.START;

    private Long tempAudioId = null;

    private boolean uz;

    public User(Long chatId, String name, String username) {
        this.chatId = chatId;
        this.name = name;
        this.username = username;
    }
}
