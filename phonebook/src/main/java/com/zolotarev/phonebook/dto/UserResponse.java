package com.zolotarev.phonebook.dto;

import com.zolotarev.phonebook.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    @Schema(description = "Имя пользователя", example = "ivan")
    private String username;
    @Schema(description = "Права доступа пользователя", example = "ADMIN")
    private User.Role role;
    private List<String> contacts;
}
