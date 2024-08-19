package it.website.backend.service;

import it.website.backend.model.Asta;
import it.website.backend.repository.AstaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AstaService {

    private static final Logger logger = Logger.getLogger(AstaService.class.getName());
    @Autowired
    private AstaRepository astaRepository;

    public Asta saveAsta(Asta asta) {
        logger.info("Saving Asta: " + asta);

        Asta existingAsta = astaRepository.findTopByPlayerOrderByPriceDesc(asta.getPlayer());

        if (existingAsta == null) {
            return astaRepository.save(asta);
        }

        if (asta.getPrice() > existingAsta.getPrice()) {
            astaRepository.delete(existingAsta);
            return astaRepository.save(asta);
        }

        if (asta.getPrice() == existingAsta.getPrice()) {
            List<String> users = existingAsta.getUsers();

            // Initialize the users list if it's null
            if (users == null) {
                users = new ArrayList<>();
            }

            // Add new user if not already present
            if (!users.contains(asta.getUsers().get(0))) {
                users.add(asta.getUsers().get(0));
            }

            existingAsta.setUsers(users);
            return astaRepository.save(existingAsta);
        }

        return existingAsta;
    }
}