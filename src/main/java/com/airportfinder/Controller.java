package com.airportfinder;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

@org.springframework.stereotype.Controller
public class Controller {
    @Value("${databasePath}")
    private String databasePath;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/airports")
    public String searchAirports(
            @RequestParam("column") int column,
            @RequestParam("prefix") String rawPrefix,
            Model model) {
        try {
            model.addAttribute("airports", getAirportsByPrefix(column, rawPrefix.toLowerCase(), databasePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "airports";
    }

    public Airport[] getAirportsByPrefix(int column, String prefix, String pathToDatabase) throws IOException {
        String[] fields = Arrays.stream(Airport.class.getDeclaredFields()).map(Field::getName).toArray(String[]::new);
        String searchingField = fields[column - 1];

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(pathToDatabase), StandardCharsets.UTF_8))) {
            ColumnPositionMappingStrategy<Airport> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Airport.class);
            strategy.setColumnMapping(fields);

            CsvToBean<Airport> csvToBean = new CsvToBeanBuilder<Airport>(reader)
                    .withMappingStrategy(strategy)
                    .withType(Airport.class)
                    .withSeparator(',')
                    .withIgnoreQuotations(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            ArrayList<Airport> airports = new ArrayList<>();
            for (Airport airport : csvToBean) {
                if (airport.getFieldByName(searchingField).toLowerCase().startsWith(prefix)) {
                    airports.add(airport);
                }
            }

            Airport[] airportsArray = airports.toArray(Airport[]::new);
            Arrays.parallelSort(airportsArray, Comparator.comparing(a -> a.getFieldByName(searchingField)));
            return airportsArray;
        }
    }
}
