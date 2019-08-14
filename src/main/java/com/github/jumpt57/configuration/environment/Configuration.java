package com.github.jumpt57.configuration.environment;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class Configuration {

    private List<String> authDomains;

    private List<String> clientIds;

}
