package com.arpitasaha.offers.controller;

import com.arpitasaha.offers.model.Offer;
import com.arpitasaha.offers.service.OfferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OfferController {
    private final OfferService service;
    public OfferController(OfferService service) { this.service = service; }

    @PostMapping("/supermarkets/{supermarketId}/offers")
    @ResponseStatus(HttpStatus.CREATED)
    public Offer create(@PathVariable Long supermarketId, @Valid @RequestBody Offer offer) {
        return service.create(supermarketId, offer);
    }

    @GetMapping("/offers")
    public Page<Offer> browse(
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) Double minDiscount,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "endDate") String sort) {
        return service.browse(category, city, minDiscount, search, page, size, sort);
    }

    @GetMapping("/offers/{id}")
    public Offer get(@PathVariable Long id) {
        return service.getById(id);
    }
}
