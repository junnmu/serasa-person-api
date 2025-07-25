package com.serasa.personapi.infrastructure.exchange.request;

import com.serasa.personapi.infrastructure.validation.ValidPhone;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PersonRequest {
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    private Integer age;
    @NotBlank
    @ValidPhone
    private String phone;
    @NotBlank
    private String cep;
    @NotNull
    @Min(0) @Max(1000)
    private Integer score;
}
