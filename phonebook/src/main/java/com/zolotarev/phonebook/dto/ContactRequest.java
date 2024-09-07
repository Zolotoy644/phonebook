package com.zolotarev.phonebook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    @Schema(description = "Имя", example = "Ivan")
    private String firstname;
    @Schema(description = "Фамилия", example = "Ivanov")
    private String lastname;
    @Schema(description = "Электронная почта", example = "zzz@ya.ru")
    private String email;
    @Schema(description = "Номер телефона", example = "+79265553417")
    private String phone;
    @Schema(description = "Телеграм", example = "@ivan")
    private String telegram;
}
