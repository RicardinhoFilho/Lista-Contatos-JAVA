package br.ufpr.tads.contato.ui.datamanager;

/**
 *
 * @author Dieval Guizelini
 */
public interface ControleDadosObserver<E> {
    
    E antesInsercao(ControleDados controle);
    
    boolean aposInsercao(ControleDados controle, E item, int index);
    
    boolean antesEdicao(ControleDados controle, E item);
    
    boolean aposEdicao(ControleDados controle, E item);
    
    boolean antesSalvar(ControleDados controle, E item);
    
    boolean antesExcluir(ControleDados controle, E item);
    
    void aposExcluir(ControleDados controle, int index);
    
    void aposCancelar(ControleDados controle, EstadoControleDados estado, E item, int index);

    void cursorMovidoControleDados(ControleDados control,int idx,E obj);
    
    void estadoAlteradoControleDados(ControleDados control, EstadoControleDados anterior, EstadoControleDados atual);
    
}
