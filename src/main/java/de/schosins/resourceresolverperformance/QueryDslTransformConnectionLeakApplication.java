package de.schosins.resourceresolverperformance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QueryDslTransformConnectionLeakApplication {

	public static void main(String[] args) {
		SpringApplication.run(QueryDslTransformConnectionLeakApplication.class, args);
	}

}
