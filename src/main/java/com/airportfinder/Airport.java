package com.airportfinder;

import com.opencsv.bean.CsvBindByPosition;

import java.lang.reflect.Field;

public class Airport {
    @CsvBindByPosition(position = 0)
    private String id;
    @CsvBindByPosition(position = 1)
    private String name;
    @CsvBindByPosition(position = 2)
    private String city;
    @CsvBindByPosition(position = 3)
    private String country;
    @CsvBindByPosition(position = 4)
    private String IATA;
    @CsvBindByPosition(position = 5)
    private String ICAO;
    @CsvBindByPosition(position = 6)
    private String latitude;
    @CsvBindByPosition(position = 7)
    private String longitude;
    @CsvBindByPosition(position = 8)
    private String altitude;
    @CsvBindByPosition(position = 9)
    private String timezone;
    @CsvBindByPosition(position = 10)
    private String DST;
    @CsvBindByPosition(position = 11)
    private String ianaTZ;
    @CsvBindByPosition(position = 12)
    private String type;
    @CsvBindByPosition(position = 13)
    private String source;

    public Airport() {
    }

    public Airport(
            String id, String name, String city, String country, String IATA, String ICAO,
            String latitude, String longitude, String altitude, String timezone, String DST,
            String ianaTZ, String type, String source) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.country = country;
        this.IATA = IATA;
        this.ICAO = ICAO;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.timezone = timezone;
        this.DST = DST;
        this.ianaTZ = ianaTZ;
        this.type = type;
        this.source = source;
    }

    String getFieldByName(String fieldName) {
        try {
            return (String) this.getClass().getDeclaredField(fieldName).get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                sb.append(field.get(this).toString()).append(" ");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
