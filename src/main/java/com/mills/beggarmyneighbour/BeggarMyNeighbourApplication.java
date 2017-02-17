package com.mills.beggarmyneighbour;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BeggarMyNeighbourApplication {

    private static final Logger logger = LoggerFactory.getLogger(BeggarMyNeighbourApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BeggarMyNeighbourApplication.class);
    }


}
