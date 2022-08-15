package cn.mina.cloud.config.controller;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Created by haoteng on 2022/7/26.
 */


@ConfigurationProperties(prefix = "user")
@Data
public class ExampleConfig {
    private String name;

    private Integer age;
}
