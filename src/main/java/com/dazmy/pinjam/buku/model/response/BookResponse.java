package com.dazmy.pinjam.buku.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String writer;
    private String publisher;
    private Integer page;
    private String yop;
    private Boolean status;

}
