package com.dazmy.pinjam.buku.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoreResponse<T> {

    private int status;
    private T data;
    private String errors;

}
