package com.zolotarev.phonebook.dto;

import com.zolotarev.phonebook.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UserRequest {
    @Schema(description = "Имя пользователя", example = "ivan")
    private String username;
    @Schema(description = "Пароль пользователя", example = "111111")
    private String password;
    @Schema(description = "Права доступа пользователя", example = "ADMIN")
    private User.Role role;
}