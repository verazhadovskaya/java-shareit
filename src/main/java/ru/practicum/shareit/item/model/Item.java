package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "request_id")
    private Long requestId;


}