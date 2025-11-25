package modelo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Compra {
    private int id;
    private int clienteId;
    private int usuarioId;
    private String nombreCliente;
    private String nombreUsuario;
    private BigDecimal pesoGramos;
    private BigDecimal kilate;
    private BigDecimal punto;
    private BigDecimal precioGramo;
    private BigDecimal total;
    private String rutaFoto;
    private Timestamp fecha;
    private String estado;

    // >>> NUEVO CAMPO PARA IMAGEN EN BLOB <<<
    private byte[] imagen;

    // Getters y setters existentes
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteId() { return clienteId; }
    public void setClienteId(int clienteId) { this.clienteId = clienteId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

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

    public String getRutaFoto() { return rutaFoto; }
    public void setRutaFoto(String rutaFoto) { this.rutaFoto = rutaFoto; }

    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // >>> NUEVOS GETTER Y SETTER PARA IMAGEN <<<
    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}