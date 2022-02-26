package br.ufpr.tads.contato.ui.componentes;

import br.ufpr.tads.contato.ui.datamanager.ControleDados;
import br.ufpr.tads.contato.ui.datamanager.ControleDadosObserver;
import br.ufpr.tads.contato.ui.datamanager.EstadoControleDados;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dieval Guizelini
 */
public class NavigatorToolBar<E> extends javax.swing.JToolBar 
        implements ActionListener,ControleDadosObserver<E> {

    private ControleDados controle = null;
    public static final String PRIMEIRO = "primeiroButton";
    public static final String ANTERIOR = "anteriorButton";
    public static final String PROXIMO = "proximoButton";
    public static final String ULTIMO = "ultimoButton";
    public static final String NOVO = "novoButton";
    public static final String EDITAR = "editarButton";
    public static final String EXCLUIR = "excluirButton";
    public static final String CANCELAR = "cancelarButton";
    public static final String SALVAR = "salvarButton";

    private javax.swing.JButton primeiroButton;
    private javax.swing.JButton anteriorButton;
    private javax.swing.JButton proximoButton;
    private javax.swing.JButton ultimoButton;

    private javax.swing.JButton novoButton;
    private javax.swing.JButton editarButton;
    private javax.swing.JButton excluirButton;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JButton salvarButton;
    //
    private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    public NavigatorToolBar() {
        super();

        initComponents();
        setEstado(EstadoControleDados.DESCONHECIDO);
    }

    private void initComponents() {
        setRollover(true);
        //setLayout(new FlowLayout(FlowLayout.LEFT));
        add(primeiroButton = novoBotao("|<", PRIMEIRO));
        add(anteriorButton = novoBotao("<", ANTERIOR));
        add(proximoButton = novoBotao(">", PROXIMO));
        add(ultimoButton = novoBotao(">|", ULTIMO));
        
        //javax.swing.JSeparator sep = new javax.swing.JSeparator(SwingConstants.VERTICAL);
        add(new javax.swing.JToolBar.Separator());
        add(novoButton = novoBotao("Novo", NOVO));
        add(editarButton = novoBotao("Editar", EDITAR));
        add(excluirButton = novoBotao("Excluir", EXCLUIR));
        add(cancelarButton = novoBotao("Cancelar", CANCELAR));
        add(salvarButton = novoBotao("Salvar", SALVAR));
    }

    private javax.swing.JButton novoBotao(String titulo, String action) {
        javax.swing.JButton novo = new javax.swing.JButton(titulo);
        novo.setText(titulo);
        novo.setFocusable(false);
        novo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        novo.setMinimumSize(new java.awt.Dimension(50, 25));
        novo.setPreferredSize(new java.awt.Dimension(75, 28));
        novo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        novo.setActionCommand(action);
        novo.addActionListener(this);
        return novo;
    }

    public void addActionListener(ActionListener listners) {
        this.actionListeners.add(listners);
    }
    
    public void setEstado(EstadoControleDados value) {
        checkMoveButtons();
        switch (value) {
            case DESCONHECIDO:
                setInativo();
                break;
            case CONSULTA:
                setNavegable();
                break;
            case EDICAO:
            case INCLUSAO:
                setEditing();
                break;
            default:
                setInativo();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //System.out.println(e.getActionCommand());
        String cmd = e.getActionCommand();
        if (controle != null) {
            if (PRIMEIRO.equals(cmd)) {
                controle.primeiroAction();
            } else if (ANTERIOR.equals(cmd)) {
                controle.anteriorAction();
            } else if (PROXIMO.equals(cmd)) {
                controle.proximoAction();
            } else if (ULTIMO.equals(cmd)) {
                controle.ultimoAction();
            } else if (NOVO.equals(cmd)) {
                controle.novoAction();
            } else if (EDITAR.equals(cmd)) {
                controle.editarAction();
            } else if (EXCLUIR.equals(cmd)) {
                controle.excluirAction();
            } else if (CANCELAR.equals(cmd)) {
                controle.cancelarAction();
            } else if (SALVAR.equals(cmd)) {
                controle.salvarAction();
            }
        }
        for (ActionListener l : actionListeners) {
            l.actionPerformed(e);
        }
    }

    public void setControleDados(ControleDados value) {
        this.controle = value;
        this.controle.addActionListeners(this);
    }
    
    
    private void checkMoveButtons() {
        if (this.controle != null) {
            primeiroButton.setEnabled(controle.canMoveFirst());
            anteriorButton.setEnabled(controle.canMovePrior());
            proximoButton.setEnabled(controle.canMoveNext());
            ultimoButton.setEnabled(controle.canMoveLast());
        } else {
            primeiroButton.setEnabled(false);
            anteriorButton.setEnabled(false);
            proximoButton.setEnabled(false);
            ultimoButton.setEnabled(false);
        }
    }

    private void setNavegable() {
        checkMoveButtons();
        novoButton.setEnabled(true);
        editarButton.setEnabled(true);
        excluirButton.setEnabled(true);
        cancelarButton.setEnabled(false);
        salvarButton.setEnabled(false);
    }

    private void setEditing() {
        setInativo();
        cancelarButton.setEnabled(true);
        salvarButton.setEnabled(true);
    }

    private void setInativo() {
        primeiroButton.setEnabled(false);
        anteriorButton.setEnabled(false);
        proximoButton.setEnabled(false);
        ultimoButton.setEnabled(false);
        novoButton.setEnabled(false);
        editarButton.setEnabled(false);
        excluirButton.setEnabled(false);
        cancelarButton.setEnabled(false);
        salvarButton.setEnabled(false);
    }    
    
    //
    //  observer
    //
    //
    // -- métodos previstos na interface ControleDadosObserver<E-Curso>
    //
    @Override
    public E antesInsercao(ControleDados controle) {
        System.out.printf("NAVIGATOR antes insercao\n");
        return null;
    }
    
    @Override
    public boolean aposInsercao(ControleDados controle, E item, int index) {
        System.out.printf("NAVIGATOR apos insercao\n");
        return false;
    }
    
    @Override
    public boolean antesEdicao(ControleDados controle, E item) {
        System.out.printf("NAVIGATOR antes edicao\n");
        return false;
    }
    
    @Override
    public boolean aposEdicao(ControleDados controle, E item) {
        System.out.printf("NAVIGATOR Apos edicao\n");
        return true;
    }
    
    @Override
    public boolean antesSalvar(ControleDados controle, E item) {
        System.out.printf("NAVIGATOR antes de salvar\n");
        return true;
    }
    
    @Override
    public void aposCancelar(ControleDados controle, EstadoControleDados estado, 
            E item, int index) {
        System.out.printf("NAVIGATOR Apos cancelar \n",index);        
    }
    
    @Override
    public boolean antesExcluir(ControleDados controle, E item) {
        System.out.printf("NAVIGATOR antes de excluir\n");
        return false;
    }
    
    @Override
    public void aposExcluir(ControleDados controle,int index) {
        System.out.printf("NAVIGATOR Apos excluir\n");        
    }
    
    @Override
    public void cursorMovidoControleDados(ControleDados control,int idx,E obj) {
        checkMoveButtons();
    }
    
    @Override
    public void estadoAlteradoControleDados(ControleDados control, 
            EstadoControleDados anterior, EstadoControleDados atual) {
        setEstado(atual);
    }

}