package com.github.echological.akint.datasource.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UrlShortener")
public class UrlShortenerEntity implements Serializable {

    private static final long serialVersionUID = -4697895247548590837L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;
    @Column(name = "long_url", unique = true)
    private String longUrl;
    @Column(name = "short_url", unique = true)
    private String shortUrl;
    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;
    @Column(name = "CreatedBy")
    private String createdBy;
}
