package com.hotel.view;

import com.hotel.model.Hospede;
import com.hotel.service.HospedeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class HospedePanel extends JPanel {

    private final HospedeService service;
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    public HospedePanel(HospedeService service) {
        this.service = service;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nome", "CPF", "Nascimento", "Telefone", "E-mail"}, 0) {
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
        for (Hospede h : service.listarTodos()) {
            tableModel.addRow(new Object[]{
                h.getId(), h.getNome(), h.getCpf(), h.getDataNascimento(), h.getTelefone(), h.getEmail()
            });
        }
    }

    private void abrirFormulario() {
        JTextField nome = new JTextField();
        JTextField cpf = new JTextField();
        JTextField nascimento = new JTextField("AAAA-MM-DD");
        JTextField telefone = new JTextField();
        JTextField email = new JTextField();

        Object[] campos = {"Nome:", nome, "CPF:", cpf, "Nascimento (AAAA-MM-DD):", nascimento, "Telefone:", telefone, "E-mail:", email};
        int r = JOptionPane.showConfirmDialog(this, campos, "Novo Hóspede", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        try {
            Hospede h = new Hospede(
                    nome.getText().trim(), cpf.getText().trim(),
                    LocalDate.parse(nascimento.getText().trim()),
                    telefone.getText().trim(), email.getText().trim()
            );
            service.salvar(h);
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um hóspede."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir hóspede selecionado?");
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.deletar((String) tableModel.getValueAt(row, 0));
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
