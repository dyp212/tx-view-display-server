package com.txrd.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NotBlank
public class CaptchaDto {

    private String key;       // 唯一标识，用于后续校验
    private String imageBase64; // Base64 编码的图片数据
}
