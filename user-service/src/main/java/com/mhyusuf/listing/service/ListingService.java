package com.mhyusuf.listing.service;

import com.mhyusuf.listing.dto.ListingRequestDto;
import com.mhyusuf.listing.dto.ListingResponseDto;
import com.mhyusuf.listing.entity.Listing;
import com.mhyusuf.listing.repository.ListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingService {

    private final ListingRepository listingRepository;

    public ListingResponseDto createListing(ListingRequestDto dto) {
        Listing listing = new Listing(dto.getUserId(), dto.getListingType(), dto.getPrice());
        Listing saved = listingRepository.save(listing);
        return toResponseDto(saved);
    }

    public ListingResponseDto getListing(Integer id) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        return toResponseDto(listing);
    }

    public List<ListingResponseDto> getAllListings(Optional<Integer> userId, int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(
                Math.max(pageNum - 1, 0),
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        Page<Listing> page = userId.isPresent()
                ? listingRepository.findAllByUserId(userId.get(), pageRequest)
                : listingRepository.findAll(pageRequest);
        return page.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public ListingResponseDto updateListing(Integer id, ListingRequestDto dto) {
        Listing listing = listingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Listing not found"));
        listing.setUserId(dto.getUserId());
        listing.setListingType(dto.getListingType());
        listing.setPrice(dto.getPrice());
        Listing updated = listingRepository.save(listing);
        return toResponseDto(updated);
    }

    public void deleteListing(Integer id) {
        if (!listingRepository.existsById(id)) {
            throw new IllegalArgumentException("Listing not found");
        }
        listingRepository.deleteById(id);
    }

    private ListingResponseDto toResponseDto(Listing listing) {
        ListingResponseDto dto = new ListingResponseDto();
        dto.setId(listing.getId());
        dto.setUserId(listing.getUserId());
        dto.setListingType(listing.getListingType());
        dto.setPrice(listing.getPrice());
        dto.setCreatedAt(listing.getCreatedAt());
        dto.setUpdatedAt(listing.getUpdatedAt());
        return dto;
    }
}
