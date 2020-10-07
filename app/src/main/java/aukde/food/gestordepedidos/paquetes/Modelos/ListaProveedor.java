package aukde.food.gestordepedidos.paquetes.Modelos;

import java.io.Serializable;
import java.util.List;

public class ListaProveedor implements Serializable {

    String id;
    String nombres;
    String apellidos;
    String username;
    String dni;
    String telefono;
    String direccion;
    String categoria;
    String nombre_empresa;
    String ruc;
    String email;
    String latitud;
    String longitud;
    String foto;

    public ListaProveedor(){

    }
    public ListaProveedor(String id, String nombres, String apellidos, String username, String dni, String telefono, String direccion, String categoria, String nombre_empresa, String ruc, String email, String latitud, String longitud, String foto) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.username = username;
        this.dni = dni;
        this.telefono = telefono;
        this.direccion = direccion;
        this.categoria = categoria;
        this.nombre_empresa = nombre_empresa;
        this.ruc = ruc;
        this.email = email;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto=foto;

    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNombre_Empresa() {
        return nombre_empresa;
    }

    public void setNombre_Empresa(String nombre_empresa) {
        this.nombre_empresa = nombre_empresa;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
