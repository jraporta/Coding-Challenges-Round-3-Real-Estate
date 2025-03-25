package com.round3.realestate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 10, scale = 2)
    private BigDecimal currentHighestBid;

    private LocalDateTime endTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal minIncrement;

    private LocalDateTime startTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal startingPrice;

    private String status;

    @ManyToOne
    @JoinColumn(name = "property_id", referencedColumnName = "id", nullable = false)
    private Property property;
}