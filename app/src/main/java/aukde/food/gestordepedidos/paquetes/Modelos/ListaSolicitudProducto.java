package aukde.food.gestordepedidos.paquetes.Modelos;

import java.io.Serializable;

public class ListaSolicitudProducto implements Serializable {

    String IDProoveedor;
    String nombreProducto;
    String cantidad;
    String precioTotalPorProducto;
    String totalACobrar;
    String estado;
    String descripcion;
    String nombreSocio;

    public ListaSolicitudProducto(){}

    public ListaSolicitudProducto(String IDProoveedor, String nombreProducto, String cantidad,
                                  String precioTotalPorProducto, String totalACobrar, String estado,
                                  String descripcion, String nombreSocio) {
        this.IDProoveedor = IDProoveedor;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioTotalPorProducto = precioTotalPorProducto;
        this.totalACobrar = totalACobrar;
        this.estado = estado;
        this.descripcion = descripcion;
        this.nombreSocio = nombreSocio;
    }

    public String getIDProoveedor() {
        return IDProoveedor;
    }

    public void setIDProoveedor(String IDProoveedor) {
        this.IDProoveedor = IDProoveedor;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioTotalPorProducto() {
        return precioTotalPorProducto;
    }

    public void setPrecioTotalPorProducto(String precioTotalPorProducto) {
        this.precioTotalPorProducto = precioTotalPorProducto;
    }

    public String getTotalACobrar() {
        return totalACobrar;
    }

    public void setTotalACobrar(String totalACobrar) {
        this.totalACobrar = totalACobrar;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreSocio() {
        return nombreSocio;
    }

    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }
}
