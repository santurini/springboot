package it.website.backend.controller;

import it.website.backend.model.Giocatore;
import it.website.backend.model.GiocatoreByNomeAndRoleRequest;
import it.website.backend.model.GiocatoreByNomeRequest;
import it.website.backend.service.GiocatoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/giocatori")
public class GiocatoreController {
    private static final Logger logger = Logger.getLogger(GiocatoreController.class.getName());

    @Autowired
    private GiocatoreService giocatoreService;

    @GetMapping("/all")
    public List<Giocatore> getAllPlayers() {
        logger.info("Fetching all players");
        List<Giocatore> players = giocatoreService.findAll();
        logger.info("Players fetched: " + players);
        return players;
    }

    @PostMapping("/byNome")
    public List<Giocatore> getPlayersByNome(@RequestBody GiocatoreByNomeRequest requestBody) {
        logger.info("Request Body: " + requestBody.getNome());
        List<Giocatore> players = giocatoreService.findByNomeContaining(requestBody.getNome());
        logger.info("Players fetched: " + players);
        return players;
    }

    @PostMapping("/byNomeAndRole")
    public List<Giocatore> getPlayersByNome(@RequestBody GiocatoreByNomeAndRoleRequest requestBody) {
        logger.info("Request Body: " + requestBody.getNome() + ", Role: " + requestBody.getR());
        List<Giocatore> players = giocatoreService.findByNomeContainingAndR(requestBody.getNome(), requestBody.getR());
        logger.info("Players fetched: " + players);
        return players;
    }

    @GetMapping("/top20")
    public List<Giocatore> getFirst20Players() {
        logger.info("Fetching top 20 players");
        List<Giocatore> players = giocatoreService.findTop20ByOrderByNomeAsc();
        logger.info("Top 20 players fetched: " + players);
        return players;
    }
}