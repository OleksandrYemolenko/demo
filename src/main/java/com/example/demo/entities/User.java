package com.example.demo.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;



@Getter
@Setter
@Entity
@Table(name = "obj_user")
@EqualsAndHashCode
@ToString
public class User implements Serializable {

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Address> addressList;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "guid", nullable = false, unique = true)
    private UUID guid;

    @Column(name = "email", nullable = false, unique = true, length = 256)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 256)
    private String phoneNumber;

    @Column(name = "name", nullable = false, length = 256)
    private String name;
}
