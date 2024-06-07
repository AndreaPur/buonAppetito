package com.example.buonAppetito.services;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileService {

    public void writeToFile(String filename, String contenuto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(contenuto);
            writer.newLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
