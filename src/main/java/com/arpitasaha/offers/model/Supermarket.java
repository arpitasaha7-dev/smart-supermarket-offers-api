package com.arpitasaha.offers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "supermarkets")
public class Supermarket {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String name;
    @NotBlank @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String city;
    @NotBlank @Email @Column(nullable = false, unique = true, length = 180)
    private String contactEmail;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
