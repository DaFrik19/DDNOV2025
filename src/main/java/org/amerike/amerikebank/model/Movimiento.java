package org.amerike.amerikebank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimiento {
    private int id;
    private int cuentaId;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal monto;
    private String descripcion;
    private LocalDateTime fechaMovimiento;
    private String cuentaRemitente;
    private String cuentaReceptora;

    public enum TipoMovimiento {
        DEPOSITO, RETIRO, TRANSFERENCIA
    }

    public Movimiento() {}

    public Movimiento(int cuentaId, TipoMovimiento tipoMovimiento, BigDecimal monto,
                      String descripcion, String cuentaRemitente, String cuentaReceptora) {
        this.cuentaId = cuentaId;
        this.tipoMovimiento = tipoMovimiento;
        this.monto = monto;
        this.descripcion = descripcion;
        this.cuentaRemitente = cuentaRemitente;
        this.cuentaReceptora = cuentaReceptora;
        this.fechaMovimiento = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCuentaId() { return cuentaId; }
    public void setCuentaId(int cuentaId) { this.cuentaId = cuentaId; }

    public TipoMovimiento getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFechaMovimiento() { return fechaMovimiento; }
    public void setFechaMovimiento(LocalDateTime fechaMovimiento) { this.fechaMovimiento = fechaMovimiento; }

    public String getCuentaRemitente() { return cuentaRemitente; }
    public void setCuentaRemitente(String cuentaRemitente) { this.cuentaRemitente = cuentaRemitente; }

    public String getCuentaReceptora() { return cuentaReceptora; }
    public void setCuentaReceptora(String cuentaReceptora) { this.cuentaReceptora = cuentaReceptora; }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", cuentaId=" + cuentaId +
                ", tipoMovimiento=" + tipoMovimiento +
                ", monto=" + monto +
                ", descripcion='" + descripcion + '\'' +
                ", fechaMovimiento=" + fechaMovimiento +
                ", cuentaRemitente='" + cuentaRemitente + '\'' +
                ", cuentaReceptora='" + cuentaReceptora + '\'' +
                '}';
    }
}