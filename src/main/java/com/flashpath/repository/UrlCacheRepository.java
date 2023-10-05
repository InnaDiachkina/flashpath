package com.flashpath.repository;

import com.flashpath.model.UrlCache;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlCacheRepository extends CrudRepository<UrlCache, String> {
    Optional<UrlCache> findById(String flashUrl);
}
