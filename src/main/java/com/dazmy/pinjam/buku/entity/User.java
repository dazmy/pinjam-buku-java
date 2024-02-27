package com.dazmy.pinjam.buku.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @SequenceGenerator(name = "users_id_seq", sequenceName = "users_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    private Long id;
    private String name;
    private LocalDate dob;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean active;

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "user")
    private Credential credential;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses;

    @OneToMany(mappedBy = "user")
    private List<UserBorrowBook> books;

    public User(String name, LocalDate dob, Role role, Boolean active) {
        super();
        this.name = name;
        this.dob = dob;
        this.role = role;
        this.active = active;
        this.age = calculateAge(dob);
    }

    private Integer calculateAge(LocalDate dob) {
        LocalDate now = LocalDate.now();
        Period periodFromBornToNow = dob.until(now);
        return periodFromBornToNow.normalized().getYears();
    }

}
