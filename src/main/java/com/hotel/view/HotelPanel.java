package com.hotel.view;

import com.hotel.model.Hotel;
import com.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HotelPanel extends JPanel {

    private final HotelService service;
    private final DefaultTableModel tableModel;
    private final JTable tabela;

    public HotelPanel(HotelService service) {
        this.service = service;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Endereço", "Telefone", "E-mail"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabela = new JTable(tableModel);
        add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnNovo = new JButton("Novo");
        JButton btnEditar = new JButton("Editar");
        JButton btnExcluir = new JButton("Excluir");
        JButton btnAtualizar = new JButton("Atualizar lista");
        botoes.add(btnNovo);
        botoes.add(btnEditar);
        botoes.add(btnExcluir);
        botoes.add(btnAtualizar);
        add(botoes, BorderLayout.SOUTH);

        btnNovo.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um hotel."); return; }
            String id = (String) tableModel.getValueAt(row, 0);
            abrirFormulario(service.buscarPorId(id));
        });
        btnExcluir.addActionListener(e -> excluir());
        btnAtualizar.addActionListener(e -> carregarDados());

        carregarDados();
    }

    private void carregarDados() {
        tableModel.setRowCount(0);
        List<Hotel> hoteis = service.listarTodos();
        for (Hotel h : hoteis) {
            tableModel.addRow(new Object[]{h.getId(), h.getNome(), h.getEndereco(), h.getTelefone(), h.getEmail()});
        }
    }

    private void abrirFormulario(Hotel hotel) {
        JTextField nome = new JTextField(hotel != null ? hotel.getNome() : "");
        JTextField endereco = new JTextField(hotel != null ? hotel.getEndereco() : "");
        JTextField telefone = new JTextField(hotel != null ? hotel.getTelefone() : "");
        JTextField email = new JTextField(hotel != null ? hotel.getEmail() : "");

        Object[] campos = {"Nome:", nome, "Endereço:", endereco, "Telefone:", telefone, "E-mail:", email};
        int r = JOptionPane.showConfirmDialog(this, campos, hotel != null ? "Editar Hotel" : "Novo Hotel", JOptionPane.OK_CANCEL_OPTION);
        if (r != JOptionPane.OK_OPTION) return;

        try {
            if (hotel == null) hotel = new Hotel();
            hotel.setNome(nome.getText().trim());
            hotel.setEndereco(endereco.getText().trim());
            hotel.setTelefone(telefone.getText().trim());
            hotel.setEmail(email.getText().trim());
            service.salvar(hotel);
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluir() {
        int row = tabela.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Selecione um hotel."); return; }
        int confirm = JOptionPane.showConfirmDialog(this, "Excluir hotel selecionado?");
        if (confirm != JOptionPane.YES_OPTION) return;
        try {
            String id = (String) tableModel.getValueAt(row, 0);
            service.deletar(id);
            carregarDados();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
