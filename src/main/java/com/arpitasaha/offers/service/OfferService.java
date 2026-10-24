package com.arpitasaha.offers.service;

import com.arpitasaha.offers.model.Offer;
import com.arpitasaha.offers.model.Supermarket;
import com.arpitasaha.offers.repository.OfferRepository;
import com.arpitasaha.offers.repository.SupermarketRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class OfferService {
    private final OfferRepository offers;
    private final SupermarketRepository supermarkets;
    public OfferService(OfferRepository offers, SupermarketRepository supermarkets) {
        this.offers = offers; this.supermarkets = supermarkets;
    }
    @Transactional
    public Offer create(Long supermarketId, Offer offer) {
        Supermarket market = supermarkets.findById(supermarketId)
            .orElseThrow(() -> new NotFoundException("Supermarket not found: " + supermarketId));
        if (offer.getOfferPrice() != null && offer.getOriginalPrice() != null
            && offer.getOfferPrice().compareTo(offer.getOriginalPrice()) > 0)
            throw new BadRequestException("Offer price cannot exceed original price");
        if (offer.getStartDate() != null && offer.getEndDate() != null
            && offer.getEndDate().isBefore(offer.getStartDate()))
            throw new BadRequestException("End date must be on or after start date");
        offer.setSupermarket(market);
        return offers.save(offer);
    }
    public Page<Offer> browse(String category, String city, Double minDiscount, String search, int page, int size, String sort) {
        if (minDiscount != null && (minDiscount < 0 || minDiscount > 100))
            throw new BadRequestException("minDiscount must be between 0 and 100");
        if (page < 0 || size < 1 || size > 100) throw new BadRequestException("page must be >= 0 and size must be 1..100");
        String sortField = switch (sort == null ? "endDate" : sort) {
            case "endDate" -> "endDate";
            case "offerPrice" -> "offerPrice";
            case "createdAt" -> "createdAt";
            default -> throw new BadRequestException("sort must be endDate, offerPrice, or createdAt");
        };
        return offers.browse(LocalDate.now(), blankToNull(category), blankToNull(city), minDiscount,
            blankToNull(search), PageRequest.of(page, size, Sort.by(sortField).ascending()));
    }
    public Offer getById(Long id) { return offers.findById(id).orElseThrow(() -> new NotFoundException("Offer not found: " + id)); }
    private String blankToNull(String value) { return value == null || value.isBlank() ? null : value.trim(); }
    public static class NotFoundException extends RuntimeException { public NotFoundException(String m) { super(m); } }
    public static class BadRequestException extends RuntimeException { public BadRequestException(String m) { super(m); } }
}
