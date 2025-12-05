package com.mhyusuf.listing.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ListingResponseDto {
    private Integer id;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("listing_type")
    private String listingType;

    private Integer price;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("updated_at")
    private Long updatedAt;
}
