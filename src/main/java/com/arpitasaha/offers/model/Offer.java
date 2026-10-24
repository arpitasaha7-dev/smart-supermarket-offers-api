package com.arpitasaha.offers.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "offers", indexes = {
    @Index(name = "idx_offer_dates", columnList = "startDate,endDate"),
    @Index(name = "idx_offer_category", columnList = "category")
})
public class Offer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "supermarket_id", nullable = false)
    private Supermarket supermarket;
    @NotBlank @Size(max = 140) @Column(nullable = false, length = 140)
    private String title;
    @NotBlank @Size(max = 600) @Column(nullable = false, length = 600)
    private String description;
    @NotBlank @Size(max = 60) @Column(nullable = false, length = 60)
    private String category;
    @NotNull @DecimalMin("0.01") @Digits(integer = 8, fraction = 2)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal originalPrice;
    @NotNull @DecimalMin("0.00") @Digits(integer = 8, fraction = 2)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal offerPrice;
    @NotNull @FutureOrPresent @Column(nullable = false)
    private LocalDate startDate;
    @NotNull @FutureOrPresent @Column(nullable = false)
    private LocalDate endDate;
    @Size(max = 40) @Column(length = 40)
    private String couponCode;
    @Min(1) private Integer usageLimit;
    @Column(nullable = false) private Integer redemptionCount = 0;
    @Column(nullable = false) private boolean active = true;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;

    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); if (redemptionCount == null) redemptionCount = 0; }
    @Transient public BigDecimal getDiscountPercentage() {
        if (originalPrice == null || originalPrice.signum() <= 0 || offerPrice == null) return BigDecimal.ZERO;
        return originalPrice.subtract(offerPrice).multiply(BigDecimal.valueOf(100))
            .divide(originalPrice, 2, java.math.RoundingMode.HALF_UP);
    }
    public boolean isCurrentlyAvailable() {
        LocalDate today = LocalDate.now();
        return active && startDate != null && endDate != null && !today.isBefore(startDate)
            && !today.isAfter(endDate) && (usageLimit == null || redemptionCount < usageLimit);
    }
    public Long getId() { return id; }
    public Supermarket getSupermarket() { return supermarket; }
    public void setSupermarket(Supermarket supermarket) { this.supermarket = supermarket; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getOriginalPrice() { return originalPrice; }
    public void setOriginalPrice(BigDecimal originalPrice) { this.originalPrice = originalPrice; }
    public BigDecimal getOfferPrice() { return offerPrice; }
    public void setOfferPrice(BigDecimal offerPrice) { this.offerPrice = offerPrice; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }
    public Integer getUsageLimit() { return usageLimit; }
    public void setUsageLimit(Integer usageLimit) { this.usageLimit = usageLimit; }
    public Integer getRedemptionCount() { return redemptionCount; }
    public void setRedemptionCount(Integer redemptionCount) { this.redemptionCount = redemptionCount; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
