package it.website.backend.repository;

import it.website.backend.model.Giocatore;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GiocatoreRepository extends MongoRepository<Giocatore, String> {
    List<Giocatore> findByNomeContaining(String nome);

    List<Giocatore> findTop20ByOrderByNomeAsc();

    List<Giocatore> findByNomeContainingAndR(String nome, String r);
}