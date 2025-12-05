package com.mhyusuf.listing.repository;

import com.mhyusuf.listing.entity.Listing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Integer> {
    Page<Listing> findAllByUserId(Integer userId, Pageable pageable);
    Page<Listing> findAll(Pageable pageable);
}
