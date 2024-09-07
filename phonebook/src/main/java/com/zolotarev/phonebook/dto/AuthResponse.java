package com.zolotarev.phonebook.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    @Schema(description = "Аутентификационный токен, сгенерированный сервером", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpdmFuIiwiaWF0IjoxNzI1NzA5Mzg2LCJleHAiOjE3MjU3OTU3ODZ9.htoRVSP5WdCGcZ3hT1tLFdlCDU7wS8LaXmlWulfIMlY")
    private String token;
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;
}
