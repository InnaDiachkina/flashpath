package com.flashpath.repository;

import com.flashpath.model.UrlShorter;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlShorterRepository extends MongoRepository<UrlShorter, String> {
    Optional<UrlShorter> findByOriginalUrl(String originalUrl);

    boolean existsByFlashUrl(String flashUrl);

}

