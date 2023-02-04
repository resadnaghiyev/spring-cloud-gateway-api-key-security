package com.rashad.cloudgateway.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class ApiKey {

    private String key;
    private List<String> services;
}
