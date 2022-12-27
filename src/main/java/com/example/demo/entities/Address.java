package com.example.demo.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "obj_address")
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAddress;

    @Column(name = "address", nullable = false, length = 512)
    private String address;

    @Column(name = "notes", length = 1024)
    private String notes;

    @ManyToOne
    @JoinTable(
            name = "n2o_address_user",
            joinColumns = @JoinColumn(name = "id_address"),
            inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    private User user;
}
