package com.curty.muggle.common.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix="client")
public class ClientProperties {

    private List<String> reactUrls;
    private String nodeUrl;
}
