package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class FondoComprador {
    private int id;
    private int usuarioId;
    private BigDecimal montoAsignado;
    private BigDecimal montoUsado;
    private Timestamp fechaAsignacion;
    private boolean activo;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public BigDecimal getMontoAsignado() { return montoAsignado; }
    public void setMontoAsignado(BigDecimal montoAsignado) { this.montoAsignado = montoAsignado; }

    public BigDecimal getMontoUsado() { return montoUsado; }
    public void setMontoUsado(BigDecimal montoUsado) { this.montoUsado = montoUsado; }

    public Timestamp getFechaAsignacion() { return fechaAsignacion; }
    public void setFechaAsignacion(Timestamp fechaAsignacion) { this.fechaAsignacion = fechaAsignacion; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    // Métodos auxiliares
    public BigDecimal getSaldoDisponible() {
        return montoAsignado.subtract(montoUsado);
    }
}