package com.hotel.view;

import com.hotel.model.Funcionario;
import com.hotel.model.Gerente;
import com.hotel.model.Hotel;
import com.hotel.model.Recepcionista;
import com.hotel.service.FuncionarioService;
import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class FuncionarioPanel extends JPanel {

    private final FuncionarioService service;
    private final HotelService hotelService;
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    public FuncionarioPanel(FuncionarioService service, HotelService hotelService) {
        this.service = service;
        this.hotelService = hotelService;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nome", "CPF", "Tipo", "Cargo", "Salário", "Hotel"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovo = new JButton("Novo");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        botoes.add(btnNovo);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);
        add(botoes, BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> abrirFormulario());
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarDados());

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        for (Funcionario f : service.listarTodos()) {
            tableModel.addRow(new Object[]{
                f.getId(), f.getNome(), f.getCpf(), f.getTipoCargo(),
                f.getCargo(), f.getSalario(), f.getHotel().getNome()
            });
        }
    }

    private void abrirFormulario() {
        List<Hotel> hoteis = hotelService.listarTodos();
        if (hoteis.isEmpty()) { JOptionPane.showMessageDialog(this, "Cadastre um hotel primeiro."); return; }

        JTextField nome = new JTextField();
        JTextField cpf = new JTextField();
        JTextField salario = new JTextField();
        JComboBox<String> tipo = new JComboBox<>(new String[]{"Recepcionista", "Gerente"});
        JComboBox<String> hotelBox = new JComboBox<>(hoteis.stream().map(Hotel::getNome).toArray(String[]::new));

        Object[] campos = {"Nome:", nome, "CPF:", cpf, "Salário (R$):", salario, "Tipo:", tipo, "Hotel:", hotelBox};
        int r = JOptionPane.showConfirmDialog(this, campos, "Novo Funcionário", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        try {
            Hotel hotel = hoteis.get(hotelBox.getSelectedIndex());
            Funcionario f;
            if (tipo.getSelectedIndex() == 0) {
                f = new Recepcionista(nome.getText().trim(), cpf.getText().trim(),
                        Double.parseDouble(salario.getText().trim()), LocalDate.now(), hotel);
            } else {
                f = new Gerente(nome.getText().trim(), cpf.getText().trim(),
                        Double.parseDouble(salario.getText().trim()), LocalDate.now(), hotel);
            }
            service.salvar(f);
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um funcionário."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir funcionário selecionado?");
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.deletar((String) tableModel.getValueAt(row, 0));
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
