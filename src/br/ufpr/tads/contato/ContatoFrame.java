package br.ufpr.tads.contato;

import br.ufpr.tads.contato.modelo.Contato;
import br.ufpr.tads.contato.ui.componentes.NavigatorToolBar;
import br.ufpr.tads.contato.ui.datamanager.ControleDados;
import br.ufpr.tads.contato.ui.datamanager.ControleDadosObserver;
import br.ufpr.tads.contato.ui.datamanager.EstadoControleDados;
import br.ufpr.tads.contato.ui.model.ContatoTableModel;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author Dieval Guizelini
 */
public class ContatoFrame extends javax.swing.JFrame implements ControleDadosObserver<Contato>,
        ListSelectionListener {

    private List<Contato> colecao = null;
    private ControleDados<Contato> controle = null;
    private ContatoTableModel tableModel = null;
    // eviar a atualização circular o jtable
    private boolean tableUpdating = false;
    private Connection conexao = null;

    private Connection conecta() {
        Connection conn = null;
        Contato[] contatos = null;
        try {
            String url = "jdbc:mysql://localhost:3306/java_contato?user=root&password=teste";
            conn = DriverManager.getConnection(url);

            //ResultSet rs =(st.executeQuery("select * from contato"));
            //System.out.println(rs.next());
            //System.out.println(rs.first());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        return conn;
    }

    private void carregaLista(Connection conn) {
        colecao = new java.util.ArrayList<Contato>();
        controle = new ControleDados<Contato>(this.colecao);
        try {
            Statement st = conn.createStatement();
            ResultSet rs = (st.executeQuery("select * from contato"));
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String telefone = rs.getString("telefone");

                Contato contato = new Contato(id, nome, telefone, email);

                colecao.add(contato);
                System.out.println(contato.getEmail());

            }

            st.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }

    /**
     * Creates new form ContatoFrame
     */
    public ContatoFrame() {
        try {
            javax.swing.UIManager.setLookAndFeel(
                    javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ContatoFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ContatoFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ContatoFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(ContatoFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        initComponents();

        conexao = conecta();

        carregaLista(conexao);

        //
        //
        // complementa os componentes da interface gráfica
        tableModel = new ContatoTableModel(colecao, controle);
        contatoTable.setModel(tableModel);

        NavigatorToolBar<Contato> nav = new NavigatorToolBar<Contato>();
        getContentPane().add(nav, java.awt.BorderLayout.NORTH);
        nav.setControleDados(controle);
        controle.addActionListeners(this);

        ListSelectionModel tableSel = contatoTable.getSelectionModel();
        tableSel.addListSelectionListener(this);

        //
        controle.checkStatus();

        setLocationRelativeTo(null);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        contatoTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        nomeTextField = new javax.swing.JTextField();
        codigoTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        emailTextField = new javax.swing.JTextField();
        telefoneTextField = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Contato - versão código espaguete");

        jPanel1.setLayout(new java.awt.BorderLayout());

        contatoTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Código", "Nome", "Title 3", "Email"
            }
        ));
        jScrollPane1.setViewportView(contatoTable);

        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        mainTabbedPane.addTab("Listagem", jPanel1);

        jLabel1.setText("Código");

        jLabel2.setText("Nome");

        jLabel3.setText("Telefone");

        nomeTextField.setText("nome do contato");

        codigoTextField.setEditable(false);
        codigoTextField.setText("47A");

        jLabel4.setText("E-mail");

        emailTextField.setText("jTextField1");

        try {
            telefoneTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##)#-####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nomeTextField))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailTextField)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(telefoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(codigoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 538, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nomeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(telefoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(emailTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(codigoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(269, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Edição", jPanel2);

        getContentPane().add(mainTabbedPane, java.awt.BorderLayout.CENTER);

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        statusLabel.setText("status");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(561, Short.MAX_VALUE)
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(statusLabel)
                .addContainerGap())
        );

        getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ContatoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ContatoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ContatoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ContatoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ContatoFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField codigoTextField;
    private javax.swing.JTable contatoTable;
    private javax.swing.JTextField emailTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JTextField nomeTextField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JFormattedTextField telefoneTextField;
    // End of variables declaration//GEN-END:variables

    private void updateFieldsFromObject(Contato obj) {
        if (obj != null) {
            nomeTextField.setText(obj.getNome() != null ? obj.getNome() : "");
            codigoTextField.setText(obj.getId() != null ? obj.getId().toString() : "");
            telefoneTextField.setText(obj.getTelefone() != null ? obj.getTelefone() : "");
            emailTextField.setText(obj.getEmail() != null ? obj.getEmail() : "");
        } else {
            nomeTextField.setText("");
            codigoTextField.setText("");
            telefoneTextField.setText("");
            emailTextField.setText("");
        }
    }

    private void updateObjectFromFields(Contato c) {
        if (c != null) {
            c.setNome(nomeTextField.getText().trim());
            c.setTelefone(telefoneTextField.getText().trim());
            c.setEmail(emailTextField.getText().trim());
        }
    }

    //
    // ----------------------------------------------------------------
    //    
    @Override
    public Contato antesInsercao(ControleDados controle) {

        mainTabbedPane.setSelectedIndex(1);
        Contato c = new Contato();
        c.setId(colecao.get(colecao.size() - 1).getId() + 1);
        updateFieldsFromObject(c);
        return c;
    }

    @Override
    public boolean aposInsercao(ControleDados controle, Contato item, int index) {
        int max = 0;
        for (Contato c : this.colecao) {
            if (c.getId() > max) {
                max = c.getId();
            }
        }

        //item.setId(max + 1);
        // precisa ver se é melhor retornar para a tabela ou continuar nos
        // campos de edição
        mainTabbedPane.setSelectedIndex(0);
        // fireTableRowsInserted causa a atualizaçao da linha para a posição 0
        tableUpdating = true;
        tableModel.fireTableRowsInserted(index, colecao.size());
        tableUpdating = false;
        //atualizaCursorTabela(index);
        return true;
    }

    @Override
    public boolean antesEdicao(ControleDados controle, Contato item) {
        mainTabbedPane.setSelectedIndex(1);
        updateFieldsFromObject(item);
        return true;
    }

    @Override
    public boolean aposEdicao(ControleDados controle, Contato item) {
        mainTabbedPane.setSelectedIndex(0);

        tableModel.fireTableDataChanged();

        int idx = controle.getIndex();
        if (idx > -1 && idx < colecao.size()) {
            atualizaCursorTabela(idx);
        }
        return true;
    }

    @Override
    public boolean antesSalvar(ControleDados controle, Contato item) {
        updateObjectFromFields(item);

        if (item.getNome().trim().length() < 1) {
            JOptionPane.showConfirmDialog(this, "O campo nome não pode estar em branco.", "Erro", JOptionPane.OK_OPTION,
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        Boolean existe = false;

        for (int i = 0; i < colecao.size(); i++) {
            if (colecao.get(i).getId() == item.getId()) {
                existe = true;
            }
        }
        if (existe) {
            try {
                PreparedStatement st = conexao.prepareStatement("update contato set nome = ?,  telefone = ?, email = ? where id=?");
                st.setInt(4, item.getId());
                st.setString(1, item.getNome());
                st.setString(2, item.getTelefone().replace("(", "").replace(")", "").replaceAll("-", ""));
                st.setString(3, item.getEmail());
                //System.out.println(item.getNome());
                st.execute();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        } else {
            try {
                PreparedStatement st = conexao.prepareStatement("Insert into contato(id, nome, telefone, email) values(?, ?, ?, ?)");
                st.setInt(1, item.getId());
                st.setString(2, item.getNome());
                st.setString(3, item.getTelefone().replace("(", "").replace(")", "").replaceAll("-", ""));
                st.setString(4, item.getEmail());
                //System.out.println(item.getNome());
                st.execute();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }

        return true;
    }

    @Override
    public boolean antesExcluir(ControleDados controle, Contato item) {
        int resultado = javax.swing.JOptionPane.showConfirmDialog(this,
                String.format("Deseja excluir %s?", item.getNome()), "Excluir?",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (resultado == javax.swing.JOptionPane.YES_OPTION) {
             try {
            PreparedStatement st = conexao.prepareStatement("DELETE FROM CONTATO WHERE ID = ?;");
            st.setInt(1, item.getId());
           
            //System.out.println(item.getNome());
            st.execute();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
            return true;
        }
        return false;
    }

    @Override
    public void aposExcluir(ControleDados controle, int index) {
        
        
       
        tableModel.fireTableDataChanged();
    }

    @Override
    public void aposCancelar(ControleDados controle, EstadoControleDados estado, Contato item, int index) {
        mainTabbedPane.setSelectedIndex(0);
        if (!tableUpdating) {
            if (index > -1) {
                atualizaCursorTabela(index);
                //contatoTable.setRowSelectionInterval(index, index);
            } else {
                atualizaCursorTabela(0);
                //contatoTable.setRowSelectionInterval(0, 0);
            }
        }
    }

    @Override
    public void cursorMovidoControleDados(ControleDados control, int idx, Contato obj) {
        updateStatus(control, idx);
        updateFieldsFromObject(obj);
        if (!tableUpdating) {
            if (idx > -1) {
                atualizaCursorTabela(idx);
                //contatoTable.setRowSelectionInterval(idx, idx);
            } else {
                atualizaCursorTabela(0);
                //contatoTable.setRowSelectionInterval(0, 0);
            }
        }
    }

    @Override
    public void estadoAlteradoControleDados(ControleDados control, EstadoControleDados anterior, EstadoControleDados atual) {
        updateStatus(control, control.getIndex());
    }

    private void updateStatus(ControleDados control, int idx) {
        String stat = "não identificado";
        EstadoControleDados estado = control.getEstado();
        switch (estado) {
            case INATIVO:
                stat = "inativo";
                break;
            case CARREGANDO:
                stat = "carregando";
                break;
            case CONSULTA:
                stat = "consulta";
                break;
            case INCLUSAO:
                stat = "inclusão";
                break;
            case EDICAO:
                stat = "edição";
                break;
            case DESCONHECIDO:
                stat = "desconhecido";
                break;
        }
        statusLabel.setText(String.format("%s (%d/%d)", stat, idx + 1, colecao.size()));
        statusLabel.updateUI();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (tableUpdating) {
            return;
        }
        // controle para eviar a recursividade
        tableUpdating = true;
        int linha = contatoTable.getSelectedRow();
        controle.moveTo(linha);
        tableUpdating = false;
    }

    private void atualizaCursorTabela(int row) {
        if (!tableUpdating) {
            tableUpdating = true;
            contatoTable.setRowSelectionInterval(row, row);
            tableUpdating = false;
        }
    }

}
