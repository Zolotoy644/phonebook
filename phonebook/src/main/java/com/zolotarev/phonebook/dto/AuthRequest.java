package com.zolotarev.phonebook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    @Schema(description = "Имя пользователя", example = "ivan")
    private String username;
    @Schema(description = "Пароль пользователя", example = "111111")
    private String password;
}
