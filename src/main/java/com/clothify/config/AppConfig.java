package com.clothify.config;

import org.modelmapper.ModelMapper;

public class AppConfig {
    public static ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
