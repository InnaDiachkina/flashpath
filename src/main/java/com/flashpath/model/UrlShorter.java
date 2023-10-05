package com.flashpath.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "url_mappings")
public class UrlShorter {
    @Id
    private String id;
    @Indexed(unique = true)
    private String flashUrl;
    private String originalUrl;
}
