package modelo;

public class Configuracion {
    private int id;
    private String monedaSimbolo;
    private String nombreNegocio;
    private String telefonoNegocio;
    private String direccionNegocio;
    private String logoUrl;
    private boolean compradorVeHistorialCompleto;
    private boolean compradorPuedeRegistrarCliente;
    private String ipServidor; // ← NUEVO

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMonedaSimbolo() { return monedaSimbolo; }
    public void setMonedaSimbolo(String monedaSimbolo) { this.monedaSimbolo = monedaSimbolo; }
    public String getNombreNegocio() { return nombreNegocio; }
    public void setNombreNegocio(String nombreNegocio) { this.nombreNegocio = nombreNegocio; }
    public String getTelefonoNegocio() { return telefonoNegocio; }
    public void setTelefonoNegocio(String telefonoNegocio) { this.telefonoNegocio = telefonoNegocio; }
    public String getDireccionNegocio() { return direccionNegocio; }
    public void setDireccionNegocio(String direccionNegocio) { this.direccionNegocio = direccionNegocio; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    public boolean isCompradorVeHistorialCompleto() { return compradorVeHistorialCompleto; }
    public void setCompradorVeHistorialCompleto(boolean compradorVeHistorialCompleto) { this.compradorVeHistorialCompleto = compradorVeHistorialCompleto; }
    public boolean isCompradorPuedeRegistrarCliente() { return compradorPuedeRegistrarCliente; }
    public void setCompradorPuedeRegistrarCliente(boolean compradorPuedeRegistrarCliente) { this.compradorPuedeRegistrarCliente = compradorPuedeRegistrarCliente; }
    
    // NUEVO: IP del servidor
    public String getIpServidor() { return ipServidor; }
    public void setIpServidor(String ipServidor) { this.ipServidor = ipServidor; }
}