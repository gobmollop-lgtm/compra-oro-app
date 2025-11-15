// modelo/CompraResumen.java
package modelo;

import java.math.BigDecimal;

public class CompraResumen {
    private int totalCompras;
    private BigDecimal totalInvertido;

    public CompraResumen(int totalCompras, BigDecimal totalInvertido) {
        this.totalCompras = totalCompras;
        this.totalInvertido = totalInvertido;
    }

    public int getTotalCompras() { return totalCompras; }
    public BigDecimal getTotalInvertido() { return totalInvertido; }
}