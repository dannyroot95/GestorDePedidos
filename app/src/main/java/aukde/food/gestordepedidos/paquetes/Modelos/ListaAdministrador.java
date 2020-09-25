package aukde.food.gestordepedidos.paquetes.Modelos;

import java.io.Serializable;

public class ListaAdministrador implements Serializable {

    String id;
    String nombres;
    String apellidos;
    String dni;
    String telefono;
    String email;
    String foto;

    public ListaAdministrador(){

    }


    public ListaAdministrador(String id, String nombres, String apellidos, String dni, String email, String telefono, String foto) {

        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.foto = foto;
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
        this.nombres= nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
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

    public void setTelefono(String teléfono) {
        this.telefono = teléfono;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}