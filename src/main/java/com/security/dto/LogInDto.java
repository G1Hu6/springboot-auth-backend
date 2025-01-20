package com.security.dto;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogInDto {

    private String email;
    private String password;
}
