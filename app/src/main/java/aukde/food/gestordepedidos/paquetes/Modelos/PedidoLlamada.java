package aukde.food.gestordepedidos.paquetes.Modelos;

import java.io.Serializable;

public class PedidoLlamada implements Serializable {

    String id;
    String horaPedido;
    String fechaPedido;
    String horaEntrega;
    String fechaEntrega;
    String proveedores;
    String productos;
    String descripcion;
    String precio1;
    String precio2;
    String precio3;
    String delivery1;
    String delivery2;
    String delivery3;
    String totalPagoProducto;
    String nombreCliente;
    String telefono;
    String conCuantoVaAPagar;
    String totalCobro;
    String vuelto;
    String direccion;
    String numPedido;
    String encargado;
    String estado;
    String latitud;
    String longitud;

    public PedidoLlamada() {
    }

    public PedidoLlamada(String id, String horaPedido, String fechaPedido, String horaEntrega, String fechaEntrega, String proveedores, String productos, String descripcion, String precio1, String precio2, String precio3, String delivery1, String delivery2, String delivery3, String totalPagoProducto, String nombreCliente, String telefono, String conCuantoVaAPagar, String totalCobro, String vuelto, String direccion, String numPedido, String encargado, String estado, String latitud, String longitud) {
        this.id = id;
        this.horaPedido = horaPedido;
        this.fechaPedido = fechaPedido;
        this.horaEntrega = horaEntrega;
        this.fechaEntrega = fechaEntrega;
        this.proveedores = proveedores;
        this.productos = productos;
        this.descripcion = descripcion;
        this.precio1 = precio1;
        this.precio2 = precio2;
        this.precio3 = precio3;
        this.delivery1 = delivery1;
        this.delivery2 = delivery2;
        this.delivery3 = delivery3;
        this.totalPagoProducto = totalPagoProducto;
        this.nombreCliente = nombreCliente;
        this.telefono = telefono;
        this.conCuantoVaAPagar = conCuantoVaAPagar;
        this.totalCobro = totalCobro;
        this.vuelto = vuelto;
        this.direccion = direccion;
        this.numPedido = numPedido;
        this.encargado = encargado;
        this.estado = estado;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(String horaPedido) {
        this.horaPedido = horaPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(String horaEntrega) {
        this.horaEntrega = horaEntrega;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(String fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getProveedores() {
        return proveedores;
    }

    public void setProveedores(String proveedores) {
        this.proveedores = proveedores;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio1() {
        return precio1;
    }

    public void setPrecio1(String precio1) {
        this.precio1 = precio1;
    }

    public String getPrecio2() {
        return precio2;
    }

    public void setPrecio2(String precio2) {
        this.precio2 = precio2;
    }

    public String getPrecio3() {
        return precio3;
    }

    public void setPrecio3(String precio3) {
        this.precio3 = precio3;
    }

    public String getDelivery1() {
        return delivery1;
    }

    public void setDelivery1(String delivery1) {
        this.delivery1 = delivery1;
    }

    public String getDelivery2() {
        return delivery2;
    }

    public void setDelivery2(String delivery2) {
        this.delivery2 = delivery2;
    }

    public String getDelivery3() {
        return delivery3;
    }

    public void setDelivery3(String delivery3) {
        this.delivery3 = delivery3;
    }

    public String getTotalPagoProducto() {
        return totalPagoProducto;
    }

    public void setTotalPagoProducto(String totalPagoProducto) {
        this.totalPagoProducto = totalPagoProducto;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getConCuantoVaAPagar() {
        return conCuantoVaAPagar;
    }

    public void setConCuantoVaAPagar(String conCuantoVaAPagar) {
        this.conCuantoVaAPagar = conCuantoVaAPagar;
    }

    public String getTotalCobro() {
        return totalCobro;
    }

    public void setTotalCobro(String totalCobro) {
        this.totalCobro = totalCobro;
    }

    public String getVuelto() {
        return vuelto;
    }

    public void setVuelto(String vuelto) {
        this.vuelto = vuelto;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }
}
