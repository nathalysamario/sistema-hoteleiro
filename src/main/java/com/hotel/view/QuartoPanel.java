package com.hotel.view;

import com.hotel.model.*;
import com.hotel.service.HotelService;
import com.hotel.service.QuartoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuartoPanel extends JPanel {

    private final QuartoService service;
    private final HotelService hotelService;
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    public QuartoPanel(QuartoService service, HotelService hotelService) {
        this.service = service;
        this.hotelService = hotelService;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Número", "Tipo", "Capacidade", "Diária (R$)", "Situação", "Hotel"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSimples = new JButton("Novo Simples");
        JButton btnSuite = new JButton("Nova Suíte");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        botoes.add(btnSimples);
        botoes.add(btnSuite);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);
        add(botoes, BorderLayout.SOUTH);

        btnSimples.addActionListener(e -> abrirFormulario(false));
        btnSuite.addActionListener(e -> abrirFormulario(true));
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarDados());

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (Quarto q : service.listarTodos()) {
            tableModel.addRow(new Object[]{
                q.getId(), q.getNumero(), q.getTipoQuarto(), q.getCapacidade(),
                q.getValorDiaria(), q.getSituacao(), q.getHotel().getNome()
            });
        }
    }

    private void abrirFormulario(boolean suite) {
        List<Hotel> hoteis = hotelService.listarTodos();
        if (hoteis.isEmpty()) { JOptionPane.showMessageDialog(this, "Cadastre um hotel primeiro."); return; }

        JTextField numero = new JTextField();
        JTextField capacidade = new JTextField();
        JTextField diaria = new JTextField();
        JComboBox<String> hotelBox = new JComboBox<>(hoteis.stream().map(Hotel::getNome).toArray(String[]::new));
        JTextField taxaLuxo = new JTextField("0.3");

        Object[] campos = suite
                ? new Object[]{"Número:", numero, "Capacidade:", capacidade, "Diária (R$):", diaria, "Taxa Luxo (ex: 0.3):", taxaLuxo, "Hotel:", hotelBox}
                : new Object[]{"Número:", numero, "Capacidade:", capacidade, "Diária (R$):", diaria, "Hotel:", hotelBox};

        int r = JOptionPane.showConfirmDialog(this, campos, suite ? "Nova Suíte" : "Novo Quarto Simples", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        try {
            Hotel hotel = hoteis.get(hotelBox.getSelectedIndex());
            if (suite) {
                QuartoSuite q = new QuartoSuite(
                        Integer.parseInt(numero.getText().trim()),
                        Integer.parseInt(capacidade.getText().trim()),
                        Double.parseDouble(diaria.getText().trim()),
                        Double.parseDouble(taxaLuxo.getText().trim()),
                        hotel
                );
                service.salvar(q);
            } else {
                QuartoSimples q = new QuartoSimples(
                        Integer.parseInt(numero.getText().trim()),
                        Integer.parseInt(capacidade.getText().trim()),
                        Double.parseDouble(diaria.getText().trim()),
                        hotel
                );
                service.salvar(q);
            }
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um quarto."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir quarto selecionado?");
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.deletar((String) tableModel.getValueAt(row, 0));
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
