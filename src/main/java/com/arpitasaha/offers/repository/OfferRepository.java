package com.arpitasaha.offers.repository;

import com.arpitasaha.offers.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

public interface OfferRepository extends JpaRepository<Offer, Long> {
    @Query("select o from Offer o where o.active = true and o.startDate <= :today and o.endDate >= :today " +
           "and (o.usageLimit is null or o.redemptionCount < o.usageLimit) " +
           "and (:category is null or lower(o.category) = lower(:category)) " +
           "and (:city is null or lower(o.supermarket.city) = lower(:city)) " +
           "and (:minDiscount is null or ((o.originalPrice - o.offerPrice) * 100 / o.originalPrice) >= :minDiscount) " +
           "and (:search is null or lower(o.title) like lower(concat('%', :search, '%')) or lower(o.description) like lower(concat('%', :search, '%')))")
    Page<Offer> browse(@Param("today") LocalDate today, @Param("category") String category,
        @Param("city") String city, @Param("minDiscount") Double minDiscount,
        @Param("search") String search, Pageable pageable);
}
