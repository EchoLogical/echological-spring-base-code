package com.avrist.urlshortener.datasource.db1.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "thistory_access")
public class HistoryAccessEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "partner_code", nullable = false, length = 15)
    private String partnerCode;

    @Column(name = "idUrlShort", nullable = false)
    private Long idUrlShort;

    @Column(name = "messages", nullable = false, columnDefinition = "TEXT")
    private String messages;

    @Column(name = "access_date")
    private LocalDateTime accessDate;

    @Column(name = "access_by", length = 500)
    private String accessBy;

}
