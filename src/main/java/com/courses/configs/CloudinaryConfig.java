package com.courses.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${CLOUD_NAME}")
    String cloudName;

    @Value("${API_KEY}")
    String apiKey;

    @Value("${API_SECRET}")
    String apiSecret;

    @Bean
    public Cloudinary cloudinaryConfiguration() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}
