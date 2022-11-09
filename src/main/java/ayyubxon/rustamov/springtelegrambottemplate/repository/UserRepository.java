package ayyubxon.rustamov.springtelegrambottemplate.repository;

import ayyubxon.rustamov.springtelegrambottemplate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
