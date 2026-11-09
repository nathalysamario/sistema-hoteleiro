package com.hotel.view;

import com.hotel.model.Servico;
import com.hotel.model.TipoServico;
import com.hotel.service.ServicoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ServicoPanel extends JPanel {

    private final ServicoService service;
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    public ServicoPanel(ServicoService service) {
        this.service = service;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(
                new String[]{"ID", "Nome", "Tipo", "Preço (R$)", "Descrição"}, 0) {
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
        for (Servico s : service.listarTodos()) {
            tableModel.addRow(new Object[]{s.getId(), s.getNome(), s.getTipo(), s.getPreco(), s.getDescricao()});
        }
    }

    private void abrirFormulario() {
        JTextField nome = new JTextField();
        JTextField preco = new JTextField();
        JTextField descricao = new JTextField();
        JComboBox<TipoServico> tipo = new JComboBox<>(TipoServico.values());

        Object[] campos = {"Nome:", nome, "Tipo:", tipo, "Preço (R$):", preco, "Descrição:", descricao};
        int r = JOptionPane.showConfirmDialog(this, campos, "Novo Serviço", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        try {
            Servico s = new Servico(
                    nome.getText().trim(), descricao.getText().trim(),
                    Double.parseDouble(preco.getText().trim()),
                    (TipoServico) tipo.getSelectedItem()
            );
            service.salvar(s);
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um serviço."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir serviço selecionado?");
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            service.deletar((String) tableModel.getValueAt(row, 0));
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
