package com.txrd.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.txrd.common.vo.PermissionVo;
import com.txrd.common.vo.RoleVo;
import com.txrd.common.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenGenerator {

    private static final long EXPIRATION_TIME = 7*86400000;//7天

    @Autowired
    private ObjectMapper objectMapper;
    /**
     * 从本地文件读取 RSA 私钥，并生成 JWT Token
     * @param userDetails 用户信息（用户名、权限等）
     * @return 生成的 JWT 字符串
     */
    public String generateToken(UserVo userDetails) {
        try {
            // 1. 从 classpath 读取私钥文件
            Resource resource = new ClassPathResource("keys/private-key.pem");
            String privateKeyPem = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // 2. 去除 PEM 头尾和换行，提取 Base64 内容
            String privateKeyContent = privateKeyPem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");// 去除所有空白字符
            RSAPrivateKey privateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyContent)));

            // 3. 构建 JWT 声明（Claims）
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(userDetails.getId() + "") // 用户标识（必填）
                    .issuer("txrd") // 颁发者（可选）
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .claim("username", userDetails.getAccount())
                    .claim("roles", userDetails.getRoles().stream().map(RoleVo::getName).toList())
                    .claim("permissions", userDetails.getPermissions().stream().map(PermissionVo::getValue).toList())
                    .claim("userDetails", objectMapper.writeValueAsString(userDetails))
                    .build();

            // 4. 使用 RSA 私钥签名 JWT
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.RS256), claims);
            signedJWT.sign(new RSASSASigner(privateKey)); // 签名

            // 5. 返回序列化的 JWT 字符串
            return signedJWT.serialize();
        } catch (Exception e) {
            log.error("JWT generate error", e);
        }
        return null;
    }
}
