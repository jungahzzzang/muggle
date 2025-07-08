package com.curty.muggle.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix="kopis.api")
public class KopisProperties {
	
	private String serviceKey;
	private String url;

}
