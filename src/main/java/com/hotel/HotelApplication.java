package com.hotel;

import com.hotel.view.MainFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;

@SpringBootApplication
public class HotelApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ApplicationContext ctx = SpringApplication.run(HotelApplication.class, args);
        SwingUtilities.invokeLater(() -> new MainFrame(ctx));
    }
}
