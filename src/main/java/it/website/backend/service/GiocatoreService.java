package it.website.backend.service;

import it.website.backend.model.Giocatore;
import it.website.backend.repository.GiocatoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class GiocatoreService {

    private static final Logger logger = Logger.getLogger(GiocatoreService.class.getName());
    @Autowired
    private GiocatoreRepository giocatoreRepository;

    public List<Giocatore> findAll() {
        logger.info("Fetching all players");
        List<Giocatore> giocatori = giocatoreRepository.findAll();
        logger.info("Found players: " + giocatori.size());
        return giocatori;
    }

    public List<Giocatore> findByNomeContainingAndR(String nome, String r) {
        logger.info("Fetching players by string contained and role");
        List<Giocatore> giocatori = giocatoreRepository.findByNomeContainingAndR(nome, r);
        logger.info("Found players: " + giocatori.size());
        return giocatori;
    }

    public List<Giocatore> findByNomeContaining(String nome) {
        logger.info("Fetching players by string contained");
        List<Giocatore> giocatori = giocatoreRepository.findByNomeContaining(nome);
        logger.info("Found players: " + giocatori.size());
        return giocatori;
    }

    public List<Giocatore> findTop20ByOrderByNomeAsc() {
        logger.info("Fetching top 20 players");
        List<Giocatore> giocatori = giocatoreRepository.findTop20ByOrderByNomeAsc();
        logger.info("Top 20 players: " + giocatori.size());
        return giocatori;
    }
}