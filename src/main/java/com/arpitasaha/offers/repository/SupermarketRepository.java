package com.arpitasaha.offers.repository;
import com.arpitasaha.offers.model.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {
    boolean existsByContactEmailIgnoreCase(String contactEmail);
}
