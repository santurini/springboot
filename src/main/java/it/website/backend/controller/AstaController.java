package it.website.backend.controller;

import it.website.backend.model.Asta;
import it.website.backend.model.AstaRequest;
import it.website.backend.service.AstaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/asta")
public class AstaController {

    private static final Logger logger = Logger.getLogger(AstaController.class.getName());
    @Autowired
    private AstaService astaService;

    @PostMapping
    public Asta addAsta(@RequestBody AstaRequest astaRequest) {
        logger.info("Received AstaRequest: " + astaRequest);

        // Ensure user is provided
        if (astaRequest.getUser() == null || astaRequest.getUser().isEmpty()) {
            logger.severe("User cannot be empty");
            throw new IllegalArgumentException("User cannot be empty");
        }

        Asta asta = new Asta();
        asta.setPlayer(astaRequest.getPlayer());
        asta.setPrice(astaRequest.getPrice());

        // Convert single user string to list
        List<String> users = new ArrayList<>();
        users.add(astaRequest.getUser());
        asta.setUsers(users);

        return astaService.saveAsta(asta);
    }
}
