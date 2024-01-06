package com.example.booking.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name="room_rate")
public class RoomRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_rate_id")
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "rate_plan_id")
    private RatePlan ratePlan;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    @Column(name = "date")
    private LocalDate date;
    @Column(name = "price")
    private BigDecimal price;
}
