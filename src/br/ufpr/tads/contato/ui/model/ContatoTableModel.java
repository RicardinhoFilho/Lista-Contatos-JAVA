package br.ufpr.tads.contato.ui.model;

import br.ufpr.tads.contato.modelo.Contato;
import br.ufpr.tads.contato.ui.datamanager.ControleDados;
import br.ufpr.tads.contato.ui.datamanager.EstadoControleDados;
import java.util.List;

/**
 *
 * @author Dieval Guizelini
 */
public class ContatoTableModel extends javax.swing.table.AbstractTableModel {

    private List<Contato> colecao = null;
    private ControleDados controle = null;

    public ContatoTableModel(List<Contato> lista, ControleDados controle) {
        this.colecao = lista;
        this.controle = controle;
    }

    @Override
    public int getRowCount() {
        return colecao.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Contato c = colecao.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return c.getId();
            case 1:
                return c.getNome();
            case 2:
                return c.getTelefone();
            case 3:
                return c.getEmail();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";
            case 1:
                return "Nome do curso";
            case 2:
                return "Telefone";
            case 3:
                return "E-mails";
        }
        return String.format("coluna %d", column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (controle != null) {
            if (controle.getEstado() == EstadoControleDados.EDICAO || controle.getEstado() == EstadoControleDados.INCLUSAO) {
                if (columnIndex > 0 && rowIndex > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Contato c = colecao.get(rowIndex);
        if( controle != null && controle.getSelected() != c ) {
            throw new RuntimeException("Falha inesperada, objetos em edição diferentes.");
        }
        switch (columnIndex) {
            case 0:
                return;
            case 1:
                c.setNome(aValue.toString());
                break;
            case 2:
                c.setTelefone(aValue.toString());
                break;
            case 3:
                c.setEmail(aValue.toString());
                break;
        }
    }
}
