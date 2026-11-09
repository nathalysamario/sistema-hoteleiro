package com.hotel.view;

import com.hotel.service.*;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(ApplicationContext ctx) {
        setTitle("Sistema de Gerenciamento Hoteleiro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        HotelService hotelService = ctx.getBean(HotelService.class);
        QuartoService quartoService = ctx.getBean(QuartoService.class);
        FuncionarioService funcionarioService = ctx.getBean(FuncionarioService.class);
        HospedeService hospedeService = ctx.getBean(HospedeService.class);
        ReservaService reservaService = ctx.getBean(ReservaService.class);
        ServicoService servicoService = ctx.getBean(ServicoService.class);

        JTabbedPane abas = new JTabbedPane();
        abas.addTab("Hotéis", new HotelPanel(hotelService));
        abas.addTab("Quartos", new QuartoPanel(quartoService, hotelService));
        abas.addTab("Funcionários", new FuncionarioPanel(funcionarioService, hotelService));
        abas.addTab("Hóspedes", new HospedePanel(hospedeService));
        abas.addTab("Reservas", new ReservaPanel(reservaService, quartoService, hospedeService));
        abas.addTab("Serviços", new ServicoPanel(servicoService));

        add(abas, BorderLayout.CENTER);
        setVisible(true);
    }
}
