package logica;

import datos.ConfiguracionDAO;
import modelo.Configuracion;

public class ConfiguracionServicio {
    private ConfiguracionDAO configDAO = new ConfiguracionDAO();

    public Configuracion obtenerConfiguracion() {
        return configDAO.obtener();
    }

    public void guardarConfiguracion(Configuracion c) {
        configDAO.actualizar(c);
    }
}
