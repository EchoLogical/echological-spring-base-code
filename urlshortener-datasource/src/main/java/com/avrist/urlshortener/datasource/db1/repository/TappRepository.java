package com.avrist.urlshortener.datasource.db1.repository;

import com.avrist.urlshortener.datasource.db1.entity.TappEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TappRepository extends JpaRepository<TappEntity, Integer> {

    @Query(value = "FROM TappEntity t WHERE t.propertyValue = :propertyValue")
    Optional<TappEntity> findPropertyValue(@Param("propertyValue") String propertyValue);

}
