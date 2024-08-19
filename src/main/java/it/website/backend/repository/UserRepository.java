package it.website.backend.repository;

import it.website.backend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByTelephone(String telephone);
}
