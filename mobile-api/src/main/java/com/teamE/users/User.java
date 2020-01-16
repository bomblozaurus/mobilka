package com.teamE.users;

import com.teamE.commonAddsEvents.Scope;
import lombok.*;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

@AllArgsConstructor
@Data
@Builder(access = AccessLevel.PUBLIC)
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Singular
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    Set<UserRole> userRoles;
    Integer indexNumber;
    Scope scope;
    @Enumerated(EnumType.STRING)
    StudentHouse studentHouse;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NonNull
    private String password;
    @Email
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;

    public User() {
    }
}



