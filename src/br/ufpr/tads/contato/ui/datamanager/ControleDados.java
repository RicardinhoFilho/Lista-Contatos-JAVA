package br.ufpr.tads.contato.ui.datamanager;

import br.ufpr.tads.contato.modelo.Contato;
import java.util.List;

/**
 *
 * @author Dieval Guizelini
 */
public class ControleDados<E> {

    private List<E> colecao = null;
    private E emFoco = null;
    private int index = -1;
    //
    private EstadoControleDados estado = EstadoControleDados.INATIVO;
    //
    private List<ControleDadosObserver> listenersList = new java.util.ArrayList<ControleDadosObserver>();

    public ControleDados() {
    }

    public ControleDados(List<E> lista) {
        this.colecao = lista;
        checkStatus();
    }

    public void checkStatus() {
        if (this.colecao == null) {
            setEstado(EstadoControleDados.INATIVO);
        } else if (this.colecao != null) {
            if (this.colecao.size() == 0) {
                setEstado(EstadoControleDados.INCLUSAO);
            } else {
                setEstado(EstadoControleDados.CONSULTA);
            }
        }
    }

    public EstadoControleDados setEstado(EstadoControleDados value) {
        EstadoControleDados old = this.estado;
        boolean alterado = false;
        switch (estado) {
            case INATIVO:
                if (EstadoControleDados.INCLUSAO.equals(value)) {
                    changeToInsert(value);
                    alterado = true;
                } else if (EstadoControleDados.CONSULTA.equals(value)
                        || EstadoControleDados.CARREGANDO.equals(value)) {
                    this.estado = value;
                    alterado = true;
                }/* else {
                    throw new RuntimeException("Mudança de estada não permitida.");
                }*/
                break;
            case CONSULTA:
                if (EstadoControleDados.INCLUSAO.equals(value)) {
                    changeToInsert(value);
                    alterado = true;
                } else if (EstadoControleDados.EDICAO.equals(value)) {
                    if (notifyBeforeEdit(this.emFoco)) {
                        this.estado = value;
                        alterado = true;
                    }
                }
                break;
            case INCLUSAO:
                this.estado = value;
                alterado = true;
                break;
            case EDICAO:
                this.estado = value;
                alterado = true;
                break;
        }
        if (alterado) {
            notifyStateChange(old, value);
        }
        return old;
    }

    private void changeToInsert(EstadoControleDados novo) {
        if (EstadoControleDados.INCLUSAO.equals(novo)) {
            int oldIndex = index;
            E auxObj = emFoco;
            emFoco = null;
            // um dos observadores deverá criar uma nova instância
            // do objeto da coleção
            if (notifyBeforeInsert()) {
                if (emFoco != null) {
                    index = -1;
                    this.estado = EstadoControleDados.INCLUSAO;
                } else {
                    index = oldIndex;
                    emFoco = auxObj;
                    this.estado = EstadoControleDados.DESCONHECIDO;
                }
            }
        }
    }

    public void add(E value) {
        if (colecao != null) {
            colecao.add(value);
            int index = colecao.size() - 1;
            while (colecao.get(index) != value && index > -1) {
                index--;
            }
            if (index == -1) {
                // falha em localizar o objeto inserido na colecao...
                throw new RuntimeException("insert collection failed");
            }
            this.index = index;
            this.emFoco = value;
            notifyAfterInsert();
            setEstado(EstadoControleDados.CONSULTA);
            notifyDataCursorMoved(this.index, this.emFoco);
        }
    }

    //
    // Para tratar os listerners
    //  Padrão da api do java, utilizar os métodos fireXXX para notificar
    // os objetos ouvintes que algo ocorreu.
    // Na API podemos observar a presença dos métodos notifyXXX... e fireXXX
    //
    //
    private void notifyDataMoved(int idx, E obj) {
        for (ControleDadosObserver o : listenersList) {
            o.cursorMovidoControleDados(this, idx, obj);
        }
    }

    private void notifyStateChange(EstadoControleDados anterior, EstadoControleDados proximo) {
        for (ControleDadosObserver o : listenersList) {
            o.estadoAlteradoControleDados(this, anterior, proximo);
        }
    }

    private boolean notifyBeforeInsert() {
        boolean res = false;
        for (ControleDadosObserver o : listenersList) {
            // muito cuidado com esse CAST
            E aux = (E) o.antesInsercao(this);
            if (aux != null) {
                this.index = -1;
                this.emFoco = aux;
                res = true;
            }
        }
        return res;
    }

    private boolean notifyBeforeEdit(E obj) {
        boolean res = false;
        for (ControleDadosObserver o : listenersList) {
            if (o.antesEdicao(this, obj)) {
                res = true;
            }
        }
        return res;
    }

    private void notifyAfterInsert() {
        for (ControleDadosObserver o : listenersList) {
            o.aposInsercao(this, this.emFoco, this.index);
        }
    }

    private void notifyAfterEdit() {
        for (ControleDadosObserver o : listenersList) {
            o.aposEdicao(this, emFoco);
        }
    }

    private boolean notifyBeforeSave(E obj) {
        boolean res = false;
        for (ControleDadosObserver o : listenersList) {
            if (o.antesSalvar(this, obj)) {
                res = true;
            }
        }
        return res;
    }
    
    private boolean notifyBeforeDelete(E obj) {
        boolean res = false;
        for (ControleDadosObserver o : listenersList) {
            if (o.antesExcluir(this, obj)) {
                res = true;
            }
        }
        return res;
    }

    private void notifyAfterCancel(EstadoControleDados old, int oldIndex) {
        for (ControleDadosObserver o : listenersList) {
            o.aposCancelar(this, old, emFoco, oldIndex);
        }
    }
    
    private void notifyAfterDelete(EstadoControleDados estado, int ind) {
        for (ControleDadosObserver o : listenersList) {
            o.aposExcluir(this, ind);
        }
    }

    private void notifyDataCursorMoved(int index, E value) {
        for (ControleDadosObserver o : listenersList) {
            o.cursorMovidoControleDados(this, index, value);
        }
    }

    //
    // Interface para atender em especial o NavigatorToolBar
    //
    public boolean primeiroAction() {
        if (this.estado.equals(EstadoControleDados.CONSULTA)) {
            if (colecao != null && colecao.size() > 0 && index > 0) {
                index = 0;
                emFoco = colecao.get(index);
                notifyDataMoved(index, emFoco);
                return true;
            }
        }
        return false;
    }

    public boolean anteriorAction() {
        if (this.estado.equals(EstadoControleDados.CONSULTA)) {
            if (colecao != null && colecao.size() > 0 && index > 0) {
                index--;
                emFoco = colecao.get(index);
                notifyDataMoved(index, emFoco);
                return true;
            }
        }
        return false;
    }

    public boolean proximoAction() {
        if (this.estado.equals(EstadoControleDados.CONSULTA)) {
            if (colecao != null && index+1 < colecao.size() ) {
                index++;
                emFoco = colecao.get(index);
                notifyDataMoved(index, emFoco);
                return true;
            }
        }
        return false;
    }

    public boolean ultimoAction() {
        if (this.estado.equals(EstadoControleDados.CONSULTA)) {
            if (colecao != null && index+1 < colecao.size() ) {
                index = colecao.size()-1;
                emFoco = colecao.get(index);
                notifyDataMoved(index, emFoco);
                return true;
            }
        }
        return false;
    }

    public void novoAction() {
        setEstado(EstadoControleDados.INCLUSAO);
    }

    public void editarAction() {
        if( EstadoControleDados.CONSULTA.equals(estado) && emFoco != null ) {
            setEstado(EstadoControleDados.EDICAO);
        }
    }

    public void excluirAction() {
        if( EstadoControleDados.CONSULTA.equals(estado) && emFoco != null ) {
            if( notifyBeforeDelete(emFoco) ) {
                this.colecao.remove(this.emFoco);
                notifyAfterDelete(estado,this.index);
                if( this.colecao.size()>0 ) {
                    this.index = 0;
                    this.emFoco = this.colecao.get(index);
                    notifyDataCursorMoved(this.index, this.emFoco);
                } else {
                    setEstado(EstadoControleDados.INCLUSAO);
                }
            }
        }
    }

    public void cancelarAction() {
        if (EstadoControleDados.INCLUSAO.equals(this.estado)) {
            // cancelar a operação de inserção
            if (colecao.size() == 0) {
                // não cancela... coleção vazia
                return;
            }
            int oldIndex = index;
            index = 0;
            emFoco = colecao.get(index);
            setEstado(EstadoControleDados.CONSULTA);
            notifyAfterCancel(EstadoControleDados.INCLUSAO, oldIndex);
        } else {
            notifyAfterCancel(estado, index);
            setEstado(EstadoControleDados.CONSULTA);
        }
    }

    public void salvarAction() {
        if (this.emFoco != null && this.colecao != null) {
            if (notifyBeforeSave(this.emFoco)) {
                if (this.estado.equals(EstadoControleDados.INCLUSAO)) {
                    this.index = this.colecao.size();
                    this.colecao.add(emFoco);
                    notifyAfterInsert();
                    setEstado(EstadoControleDados.CONSULTA);
                    notifyDataCursorMoved(this.index, this.emFoco);
                } else {
                    if (colecao != null) {
                        if (colecao.size() > this.index && this.index > -1
                                && this.colecao.get(this.index) != this.emFoco) {
                            int index = colecao.size() - 1;
                            while (colecao.get(index) != emFoco && index > -1) {
                                index--;
                            }
                            if (index == -1) {
                                // falha em localizar o objeto inserido na colecao...
                                throw new RuntimeException("edit collection failed");
                            }
                            this.index = index;
                        }
                        notifyAfterEdit();
                        setEstado(EstadoControleDados.CONSULTA);
                        notifyDataCursorMoved(this.index, this.emFoco);
                    }
                }
            }
        }
    }

    public boolean canMoveFirst() {
        if (this.colecao != null && this.index > 0 && this.colecao.size() > 1
                && EstadoControleDados.CONSULTA.equals(this.estado)) {
            return true;
        }
        return false;
    }

    public void firstMoveAction() {
        if (canMoveFirst()) {
            if (this.colecao.size() > 0) {
                this.index = 0;
                this.emFoco = this.colecao.get(0);
                notifyDataCursorMoved(this.index, this.emFoco);
            }
        }
    }

    public boolean canMovePrior() {
        if (this.colecao != null && this.index > 0 && this.colecao.size() > 1
                && EstadoControleDados.CONSULTA.equals(this.estado)) {
            return true;
        }
        return false;
    }

    public void priorMoveAction() {
        if (canMovePrior()) {
            if (this.colecao.size() > 0) {
                this.index--;
                this.emFoco = this.colecao.get(0);
                notifyDataCursorMoved(this.index, this.emFoco);
            }
        }
    }

    public boolean canMoveNext() {
        if (this.colecao != null && (this.index + 1) < this.colecao.size()
                && this.colecao.size() > 1
                && EstadoControleDados.CONSULTA.equals(this.estado)) {
            return true;
        }
        return false;
    }

    public void nextMoveAction() {
        if (canMoveNext()) {
            this.index++;
            this.emFoco = this.colecao.get(this.index);
            notifyDataCursorMoved(this.index, this.emFoco);
        }
    }

    public boolean canMoveLast() {
        if (this.colecao != null && this.colecao.size() > 0
                && this.index != this.colecao.size() - 1
                && EstadoControleDados.CONSULTA.equals(this.estado)) {
            return true;
        }
        return false;
    }

    public void lastMoveAction() {
        if (canMoveLast()) {
            this.index = this.colecao.size() - 1;
            this.emFoco = this.colecao.get(this.index);
            notifyDataCursorMoved(this.index, this.emFoco);
        }
    }

    public void moveTo(int pos) {
        if (this.colecao != null && pos >= 0 && pos < this.colecao.size()
                && EstadoControleDados.CONSULTA.equals(this.estado)) {
            this.index = pos;
            this.emFoco = this.colecao.get(pos);
            notifyDataCursorMoved(this.index, this.emFoco);
        }
    }

    public E deleteAction(E value) {
        if (this.colecao.remove(value)) {
            if (this.colecao.size() > 0) {
                this.emFoco = this.colecao.get(0);
                this.index = 0;
                moveTo(index);
            } else {
                this.emFoco = null;
                this.index = -1;
            }
            return value;
        }
        return null;
    }

    public void editAction() {
        if (this.emFoco != null && EstadoControleDados.CONSULTA.equals(this.estado)) {
            setEstado(EstadoControleDados.EDICAO);
        }
    }

    //
    //  --- métodos gets/sets
    //
    public EstadoControleDados getEstado() {
        return this.estado;
    }

    public int getIndex() {
        return this.index;
    }

    public int size() {
        if (this.colecao == null) {
            return 0;
        }
        return this.colecao.size();
    }

    public E get(int index) {
        return this.colecao.get(index);
    }
    
    public E getSelected() {
        return this.emFoco;
    }

    public void addActionListeners(ControleDadosObserver value) {
        listenersList.add(value);
    }

    public void removeActionListeners(ControleDadosObserver value) {
        listenersList.remove(value);
    }

}
