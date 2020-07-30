package com.airportfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        try (InputStream input = new FileInputStream("src\\main\\resources\\application.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            if (prop.getProperty("webVersion", "true").toLowerCase().equals("true")) {
                SpringApplication.run(Application.class, args);
                return;
            }
            int column = Integer.parseInt(prop.getProperty("searchingColumn", "2"));
            Scanner scanner = new Scanner(System.in);
            System.out.println("Input prefix:");
            String rawPrefix = scanner.nextLine();
            String databasePath = prop.getProperty("databasePath");
            Airport[] airports = new Controller().getAirportsByPrefix(column, rawPrefix, databasePath);
            System.out.println("Results:");
            for (Airport airport : airports) {
                System.out.println(airport);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

