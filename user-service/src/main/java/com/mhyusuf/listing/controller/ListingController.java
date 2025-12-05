package com.mhyusuf.listing.controller;

import com.mhyusuf.listing.dto.ListingRequestDto;
import com.mhyusuf.listing.dto.ListingResponseDto;
import com.mhyusuf.listing.service.ListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/listings")
@RequiredArgsConstructor
public class ListingController {

    private final ListingService listingService;

    @PostMapping
    public ResponseEntity<ListingResponseDto> create(@RequestBody ListingRequestDto dto) {
        return ResponseEntity.ok(listingService.createListing(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListingResponseDto> get(@PathVariable Integer id) {
        return ResponseEntity.ok(listingService.getListing(id));
    }

    @GetMapping
    public ResponseEntity<List<ListingResponseDto>> getAll(
            @RequestParam(value = "user_id", required = false) Integer userId,
            @RequestParam(value = "page_num", defaultValue = "1") int pageNum,
            @RequestParam(value = "page_size", defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(
                listingService.getAllListings(Optional.ofNullable(userId), pageNum, pageSize)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListingResponseDto> update(@PathVariable Integer id, @RequestBody ListingRequestDto dto) {
        return ResponseEntity.ok(listingService.updateListing(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        listingService.deleteListing(id);
        return ResponseEntity.noContent().build();
    }
}
