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
@Table(name = "tapp")
public class TappEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "partner_code", length = 15, nullable = false)
    private String partnerCode;

    @Column(name = "property", length = 50, nullable = false)
    private String property;

    @Column(name = "property_value", length = 1000)
    private String propertyValue;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

}
