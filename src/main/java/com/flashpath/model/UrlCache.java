package com.flashpath.model;

import java.io.Serializable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash("url_cache")
public class UrlCache implements Serializable {
    @Id
    private String flashUrl;
    private String originalUrl;
}
