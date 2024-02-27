package com.dazmy.pinjam.buku.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBookRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @NotBlank
    @Size(max = 255)
    private String writer;

    @NotBlank
    @Size(max = 255)
    private String publisher;

    @NotNull
    private Integer page;

    @NotBlank
    @Pattern(regexp = "^(19|[2-9][0-9])\\d{2}$", flags = Pattern.Flag.MULTILINE)
    private String yop;

}
