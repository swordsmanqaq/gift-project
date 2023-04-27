package com.heng.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file.alioss")
@Data
public class AliyunOssProperties {
   private String bucketName;
   private String accessKey;
   private String secretKey;
   private String endpoint;
}