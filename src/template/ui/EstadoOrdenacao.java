package template.ui;

/**
 * Classe EstadoOrdenacao para ser utilizada nas demais classes
 * 
 * @author Brenno Gaspar Pinto & Victor Altran Soares
 */
public class EstadoOrdenacao {
    
    // Atributos
    int[] a;
    private int i;
    private int j;
    private int especial;
    
    // Construtor 
    public EstadoOrdenacao( int[] a, int i, int j, int especial ) {
        this.a = a;
        this.i = i;
        this.j = j;
        this.especial = especial;
    }
    
    // Getters
    public int[] getA() {
        return a;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    public int getEspecial() {
        return especial;
    }
    
}
