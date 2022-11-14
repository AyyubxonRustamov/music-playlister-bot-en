package ayyubxon.rustamov.springtelegrambottemplate.checker;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class UsernameChecker {

    public String check(User user) {
        return "<a href=\"tg://user?id=123456789\">" + (user.getUserName() == null | user.getUserName().equals("null") ?
                user.getFirstName() : "@" + user.getUserName()) + "</a>";
    }

}
