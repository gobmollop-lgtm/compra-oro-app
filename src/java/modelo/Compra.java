package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Compra {
    private int id;
    private int clienteId;
    private String nombreCliente;
    private BigDecimal pesoGramos;
    private BigDecimal kilate;
    private BigDecimal punto;
    private BigDecimal precioGramo; // = kilate * punto
    private BigDecimal total;       // = peso * kilate * punto
    private String observaciones;
    private Timestamp fecha;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }
    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }
    public BigDecimal getPesoGramos() { return pesoGramos; }
    public void setPesoGramos(BigDecimal pesoGramos) { this.pesoGramos = pesoGramos; }
    public BigDecimal getKilate() { return kilate; }
    public void setKilate(BigDecimal kilate) { this.kilate = kilate; }
    public BigDecimal getPunto() { return punto; }
    public void setPunto(BigDecimal punto) { this.punto = punto; }
    public BigDecimal getPrecioGramo() { return precioGramo; }
    public void setPrecioGramo(BigDecimal precioGramo) { this.precioGramo = precioGramo; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}