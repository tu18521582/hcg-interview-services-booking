package com.example.booking.model.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@Table(name="room_availability")
public class RoomAvailability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_availability_id")
    private Long availabilityId;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;
    @Column(name = "date")
    private LocalDate date;
    @Column(name = "number_of_rooms_available")
    private Integer numberOfRoomsAvailable;
}
