package logica;

import datos.FondoDAO;
import modelo.FondoComprador;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FondoServicio {
    private FondoDAO fondoDAO = new FondoDAO();

    public FondoComprador obtenerPorUsuario(int usuarioId) {
        return fondoDAO.obtenerPorUsuario(usuarioId);
    }

    public BigDecimal obtenerSaldoDisponible(int usuarioId) {
        FondoComprador fondo = fondoDAO.obtenerPorUsuario(usuarioId);
        if (fondo == null) {
            return BigDecimal.ZERO;
        }
        return fondo.getMontoAsignado().subtract(fondo.getMontoUsado());
    }

    public void recargarFondo(int usuarioId, BigDecimal montoRecarga) {
        FondoComprador fondo = obtenerPorUsuario(usuarioId);
        
        if (fondo == null) {
            fondo = new FondoComprador();
            fondo.setUsuarioId(usuarioId);
            fondo.setMontoAsignado(montoRecarga);
            fondo.setMontoUsado(BigDecimal.ZERO);
            fondo.setActivo(true);
            fondoDAO.crear(fondo);
        } else {
            BigDecimal nuevoMontoAsignado = fondo.getMontoAsignado().add(montoRecarga);
            fondoDAO.actualizarMontoAsignado(usuarioId, nuevoMontoAsignado);
        }
    }

    public void deducirMonto(int usuarioId, BigDecimal montoCompra) {
        fondoDAO.deducirMonto(usuarioId, montoCompra);
    }

    public static String formatearMonto(BigDecimal monto) {
        if (monto == null) monto = BigDecimal.ZERO;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("$#,##0.00", symbols);
        return df.format(monto.setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}