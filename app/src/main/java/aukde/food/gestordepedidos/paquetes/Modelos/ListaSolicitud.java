package aukde.food.gestordepedidos.paquetes.Modelos;

import java.io.Serializable;

public class ListaSolicitud implements Serializable {

    String id;
    String numSolicitud;
    String proveedor;
    String cantidadProducto;
    String costoDelivery;
    String direccionCliente;
    String listaDeProductos;
    String precioTotalProductos;
    String precioUnitario;
    String referenciaCliente;
    String telefonoCliente;
    String nombreCliente;
    String totalNetoProducto;
    String photo;
    String estado;

    public ListaSolicitud(){

    }

    public ListaSolicitud(String id, String numSolicitud, String proveedor, String cantidadProducto,
                          String costoDelivery, String direccionCliente, String listaDeProductos,
                          String precioTotalProductos, String precioUnitario, String referenciaCliente,
                          String telefonoCliente, String nombreCliente, String totalNetoProducto,
                          String photo, String estado) {
        this.id = id;
        this.numSolicitud = numSolicitud;
        this.proveedor = proveedor;
        this.cantidadProducto = cantidadProducto;
        this.costoDelivery = costoDelivery;
        this.direccionCliente = direccionCliente;
        this.listaDeProductos = listaDeProductos;
        this.precioTotalProductos = precioTotalProductos;
        this.precioUnitario = precioUnitario;
        this.referenciaCliente = referenciaCliente;
        this.telefonoCliente = telefonoCliente;
        this.nombreCliente = nombreCliente;
        this.totalNetoProducto = totalNetoProducto;
        this.photo = photo;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumSolicitud() {
        return numSolicitud;
    }

    public void setNumSolicitud(String numSolicitud) {
        this.numSolicitud = numSolicitud;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(String cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public String getCostoDelivery() {
        return costoDelivery;
    }

    public void setCostoDelivery(String costoDelivery) {
        this.costoDelivery = costoDelivery;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getListaDeProductos() {
        return listaDeProductos;
    }

    public void setListaDeProductos(String listaDeProductos) {
        this.listaDeProductos = listaDeProductos;
    }

    public String getPrecioTotalProductos() {
        return precioTotalProductos;
    }

    public void setPrecioTotalProductos(String precioTotalProductos) {
        this.precioTotalProductos = precioTotalProductos;
    }

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getReferenciaCliente() {
        return referenciaCliente;
    }

    public void setReferenciaCliente(String referenciaCliente) {
        this.referenciaCliente = referenciaCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTotalNetoProducto() {
        return totalNetoProducto;
    }

    public void setTotalNetoProducto(String totalNetoProducto) {
        this.totalNetoProducto = totalNetoProducto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
