package com.hotel.view;

import com.hotel.model.Hospede;
import com.hotel.model.Quarto;
import com.hotel.model.Reserva;
import com.hotel.service.HospedeService;
import com.hotel.service.QuartoService;
import com.hotel.service.ReservaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class ReservaPanel extends JPanel {

    private final ReservaService service;
    private final QuartoService quartoService;
    private final HospedeService hospedeService;
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    public ReservaPanel(ReservaService service, QuartoService quartoService, HospedeService hospedeService) {
        this.service = service;
        this.quartoService = quartoService;
        this.hospedeService = hospedeService;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Hóspede", "Quarto", "Entrada", "Saída", "Situação", "Valor Total"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNova = new JButton("Nova Reserva");
        JButton btnCheckIn = new JButton("Check-in");
        JButton btnCheckOut = new JButton("Check-out");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAtualizar = new JButton("Atualizar lista");
        botoes.add(btnNova);
        botoes.add(btnCheckIn);
        botoes.add(btnCheckOut);
        botoes.add(btnCancelar);
        botoes.add(btnAtualizar);
        add(botoes, BorderLayout.SOUTH);

        btnNova.addActionListener(e -> abrirFormulario());
        btnCheckIn.addActionListener(e -> acaoReserva("checkin"));
        btnCheckOut.addActionListener(e -> acaoReserva("checkout"));
        btnCancelar.addActionListener(e -> acaoReserva("cancelar"));
        btnAtualizar.addActionListener(e -> carregarDados());

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (Reserva r : service.listarTodas()) {
            tableModel.addRow(new Object[]{
                r.getId(), r.getHospede().getNome(), "Q" + r.getQuarto().getNumero(),
                r.getDataEntrada(), r.getDataSaida(), r.getSituacao(), r.getValorTotal()
            });
        }
    }

    private void abrirFormulario() {
        List<Quarto> quartos = quartoService.listarTodos();
        List<Hospede> hospedes = hospedeService.listarTodos();
        if (quartos.isEmpty() || hospedes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cadastre quartos e hóspedes primeiro.");
            return;
        }

        JComboBox<String> quartoBox = new JComboBox<>(
                quartos.stream().map(q -> "Q" + q.getNumero() + " - " + q.getTipoQuarto() + " (" + q.getHotel().getNome() + ")").toArray(String[]::new));
        JComboBox<String> hospedeBox = new JComboBox<>(
                hospedes.stream().map(Hospede::getNome).toArray(String[]::new));
        JTextField entrada = new JTextField(LocalDate.now().toString());
        JTextField saida = new JTextField(LocalDate.now().plusDays(1).toString());

        Object[] campos = {"Quarto:", quartoBox, "Hóspede:", hospedeBox, "Entrada (AAAA-MM-DD):", entrada, "Saída (AAAA-MM-DD):", saida};
        int r = JOptionPane.showConfirmDialog(this, campos, "Nova Reserva", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        try {
            Quarto quarto = quartos.get(quartoBox.getSelectedIndex());
            Hospede hospede = hospedes.get(hospedeBox.getSelectedIndex());
            service.criar(quarto.getId(), hospede.getId(),
                    LocalDate.parse(entrada.getText().trim()), LocalDate.parse(saida.getText().trim()));
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void acaoReserva(String acao) {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione uma reserva."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            switch (acao) {
                case "checkin" -> service.checkIn(id);
                case "checkout" -> service.checkOut(id);
                case "cancelar" -> service.cancelar(id);
            }
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
