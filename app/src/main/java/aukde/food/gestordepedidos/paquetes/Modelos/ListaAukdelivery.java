package aukde.food.gestordepedidos.paquetes.Modelos;

import java.io.Serializable;

public class ListaAukdelivery implements Serializable {

    String id;
    String nombres;
    String apellidos;
    String username;
    String dni;
    String telefono;
    String marca_de_moto;
    String placa;
    String categoria_licencia;
    String numero_licencia;
    String email;
    String soat;
    String foto;

    public ListaAukdelivery() {

    }
    public ListaAukdelivery(String id, String nombres, String apellidos, String username, String dni, String telefono, String marca_de_moto, String placa, String categoria_licencia, String numero_licencia, String email, String soat, String foto) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.username = username;
        this.dni = dni;
        this.telefono = telefono;
        this.marca_de_moto = marca_de_moto;
        this.placa = placa;
        this.categoria_licencia = categoria_licencia;
        this.numero_licencia = numero_licencia;
        this.email = email;
        this.soat = soat;
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

    public String getMarca_de_moto() {
        return marca_de_moto;
    }

    public void setMarca_de_moto(String marca_de_moto) {
        this.marca_de_moto = marca_de_moto;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCategoria_licencia() {
        return categoria_licencia;
    }

    public void setCategoria_licencia(String categoria_licencia) {
        this.categoria_licencia = categoria_licencia;
    }

    public String getNumero_Licencia() {
        return numero_licencia;
    }

    public void setNumero_Licencia(String numero_licencia) {
        this.numero_licencia = numero_licencia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoat() {
        return soat;
    }

    public void setSoat(String soat) {
        this.soat = soat;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}