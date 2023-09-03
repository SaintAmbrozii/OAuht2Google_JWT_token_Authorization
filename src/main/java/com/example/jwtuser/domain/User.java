package com.example.jwtuser.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;
@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false)
    private String username;

    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "GENDER")
    private String gender;
    @Column(name = "LOCALE")
    private String locale;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ENABLED")
    private Integer enabled;

    @Column(name = "FULL_NAME")
    private String fullName;
    @Column(name = "AUTH_PROVIDER")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    @Column(name = "PROVIDER_ID")
    private String providerId;
    @Column(name = "PICTURE")
    private String picture;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "AUTHORITIES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    private Set<GrantedAuthority> authority = new HashSet<>();


}
