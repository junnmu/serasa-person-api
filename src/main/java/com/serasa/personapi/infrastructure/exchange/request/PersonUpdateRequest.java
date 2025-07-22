package com.serasa.personapi.infrastructure.exchange.request;

import com.serasa.personapi.infrastructure.validation.NotBlankIfNotNull;
import com.serasa.personapi.infrastructure.validation.ValidPhone;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonUpdateRequest {
    @NotBlankIfNotNull
    private String name;
    @Min(0)
    private Integer age;
    @NotBlankIfNotNull
    @ValidPhone
    private String phone;
    @NotBlankIfNotNull
    private String cep;
    @Min(0) @Max(1000)
    private Integer score;
}
