package com.fairchain.batchservice;

import jakarta.persistence.*;

@Entity
@Table(name = "farmers")
public class Farmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String region;

    @Column(name = "preferred_language")
    private String preferredLanguage;

    protected Farmer() {
    }

    public Farmer(String name, String phone, String region, String preferredLanguage) {
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.preferredLanguage = preferredLanguage;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getRegion() { return region; }
    public String getPreferredLanguage() { return preferredLanguage; }
}
