package com.mhyusuf.listing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ListingRequestDto {
    @NotNull
    private Integer userId;

    @NotBlank
    @Size(max = 10)
    @Pattern(regexp = "rent|sale", message = "listingType must be 'rent' or 'sale'")
    private String listingType;

    @NotNull
    @Positive(message = "price must be above zero")
    private Integer price;
}
