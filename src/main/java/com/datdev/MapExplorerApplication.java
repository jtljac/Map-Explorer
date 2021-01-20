package com.datdev;

import com.datdev.model.Map;
import com.datdev.repo.MapRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MapExplorerApplication {
	@Autowired
	MapRepo mapRepository;

	public static void main(String[] args) {
		SpringApplication.run(MapExplorerApplication.class, args);
	}
}
