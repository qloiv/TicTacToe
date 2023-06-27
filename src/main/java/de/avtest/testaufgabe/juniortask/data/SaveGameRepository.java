package de.avtest.testaufgabe.juniortask.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaveGameRepository extends CrudRepository<SaveGame, String> {
    SaveGame findByUuid(String uuid);
}

