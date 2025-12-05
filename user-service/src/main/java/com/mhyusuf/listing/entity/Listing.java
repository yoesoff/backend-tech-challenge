package com.mhyusuf.listing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "listings")
public class Listing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "listing_type", nullable = false, length = 10)
    private String listingType;

    @Column(name = "price", nullable = false)
    @Positive(message = "price must be above zero")
    private Integer price;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    public Listing(@NotNull Integer userId, @NotBlank @Size(max = 10) String listingType, @NotNull @Positive Integer price) {
        this.userId = userId;
        this.listingType = listingType;
        this.price = price;
    }

    @PrePersist
    protected void onCreate() {
        long now = System.currentTimeMillis() * 1000;
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = System.currentTimeMillis() * 1000;
    }
}
