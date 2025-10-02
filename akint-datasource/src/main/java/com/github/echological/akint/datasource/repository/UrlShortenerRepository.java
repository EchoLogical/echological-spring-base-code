package com.github.echological.akint.datasource.repository;

import com.github.echological.akint.datasource.entity.UrlShortenerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlShortenerRepository extends JpaRepository<UrlShortenerEntity, Long> {

    @Query(value = "FROM UrlShortenerEntity u WHERE u.shortUrl = :shortUrl")
    Optional<UrlShortenerEntity> findExistingCode(@Param("shortUrl") String shortUrl);

    @Query(value = "FROM UrlShortenerEntity u WHERE u.longUrl = :longUrl")
    Optional<UrlShortenerEntity> findExistingLongUrl(@Param("longUrl") String longUrl);

}
