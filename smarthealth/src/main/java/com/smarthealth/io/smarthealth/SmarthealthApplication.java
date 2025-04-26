package com.smarthealth.io.smarthealth;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SmarthealthApplication {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(SmarthealthApplication.class, args);
    
	}
 
}
