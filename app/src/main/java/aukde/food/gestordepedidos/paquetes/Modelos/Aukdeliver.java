package aukde.food.gestordepedidos.paquetes.Modelos;

public class Aukdeliver {

    String id;
    String nombres;
    String apellidos;
    String nombreUsuario;
    String dni;
    String telefono;
    String marcaDeMoto;
    String placa;
    String categoriaLic;
    String numLicencia;
    String email;
    String soat;

    public Aukdeliver(String id, String nombres, String apellidos, String nombreUsuario, String dni, String telefono, String marcaDeMoto, String placa, String categoriaLic, String numLicencia, String email, String soat) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.nombreUsuario = nombreUsuario;
        this.dni = dni;
        this.telefono = telefono;
        this.marcaDeMoto = marcaDeMoto;
        this.placa = placa;
        this.categoriaLic = categoriaLic;
        this.numLicencia = numLicencia;
        this.email = email;
        this.soat = soat;
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

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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

    public String getMarcaDeMoto() {
        return marcaDeMoto;
    }

    public void setMarcaDeMoto(String marcaDeMoto) {
        this.marcaDeMoto = marcaDeMoto;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getCategoriaLic() {
        return categoriaLic;
    }

    public void setCategoriaLic(String categoriaLic) {
        this.categoriaLic = categoriaLic;
    }

    public String getNumLicencia() {
        return numLicencia;
    }

    public void setNumLicencia(String numLicencia) {
        this.numLicencia = numLicencia;
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
}
