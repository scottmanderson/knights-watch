package com.agincourt.chessstat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChessStatApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChessStatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }

}
