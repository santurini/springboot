package it.website.backend.repository;

import it.website.backend.model.Asta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AstaRepository extends MongoRepository<Asta, String> {
    Asta findTopByPlayerOrderByPriceDesc(String player);
}