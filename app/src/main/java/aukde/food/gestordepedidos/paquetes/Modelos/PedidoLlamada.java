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
    String precioUnitario;
    String cantidad;
    String precioTotalXProducto;
    String comision;
    String totalDelivery;
    String gananciaDelivery;
    String gananciaComision;
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
    String referencia;

    public PedidoLlamada() {
    }

    public PedidoLlamada(String id, String horaPedido, String fechaPedido, String horaEntrega,
                         String fechaEntrega, String proveedores, String productos, String descripcion,
                         String precioUnitario, String cantidad, String precioTotalXProducto, String comision,
                         String totalDelivery, String gananciaDelivery, String gananciaComision, String totalPagoProducto,
                         String nombreCliente, String telefono, String conCuantoVaAPagar, String totalCobro, String vuelto,
                         String direccion, String numPedido, String encargado, String estado, String latitud, String longitud,
                         String referencia) {

        this.id = id;
        this.horaPedido = horaPedido;
        this.fechaPedido = fechaPedido;
        this.horaEntrega = horaEntrega;
        this.fechaEntrega = fechaEntrega;
        this.proveedores = proveedores;
        this.productos = productos;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
        this.precioTotalXProducto = precioTotalXProducto;
        this.comision = comision;
        this.totalDelivery = totalDelivery;
        this.gananciaDelivery = gananciaDelivery;
        this.gananciaComision = gananciaComision;
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
        this.referencia = referencia;
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

    public String getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(String precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioTotalXProducto() {
        return precioTotalXProducto;
    }

    public void setPrecioTotalXProducto(String precioTotalXProducto) {
        this.precioTotalXProducto = precioTotalXProducto;
    }

    public String getComision() {
        return comision;
    }

    public void setComision(String comision) {
        this.comision = comision;
    }

    public String getTotalDelivery() {
        return totalDelivery;
    }

    public void setTotalDelivery(String totalDelivery) {
        this.totalDelivery = totalDelivery;
    }

    public String getGananciaDelivery() {
        return gananciaDelivery;
    }

    public void setGananciaDelivery(String gananciaDelivery) {
        this.gananciaDelivery = gananciaDelivery;
    }

    public String getGananciaComision() {
        return gananciaComision;
    }

    public void setGananciaComision(String gananciaComision) {
        this.gananciaComision = gananciaComision;
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}