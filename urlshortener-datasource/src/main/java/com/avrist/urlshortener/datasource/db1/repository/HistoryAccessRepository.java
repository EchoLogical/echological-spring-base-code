package com.avrist.urlshortener.datasource.db1.repository;

import com.avrist.urlshortener.datasource.db1.entity.HistoryAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryAccessRepository extends JpaRepository<HistoryAccessEntity, Long> {
}
