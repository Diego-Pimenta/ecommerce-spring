package com.compass.ecommerce_spring.entity;

import com.compass.ecommerce_spring.entity.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(of = "cpf")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_user")
public class User {
    @Id
    @Column(length = 11)
    private String cpf;
    @Column(nullable = false, length = 80)
    private String name;
    @NaturalId(mutable = true)
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    @Column(nullable = false, length = 200)
    private String password;
    @Column(nullable = false, name = "phone_number", length = 15)
    private String phoneNumber;
    @Column(nullable = false, length = 100)
    private String address;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role; // para ser ADMIN um user admin deve dar permissão
    @Column(nullable = false)
    private Boolean active;
    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private List<Sale> sales = new ArrayList<>();
}
