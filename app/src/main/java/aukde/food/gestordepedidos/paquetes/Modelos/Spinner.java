package aukde.food.gestordepedidos.paquetes.Modelos;

public class Spinner {

    String id;
    String nombres;


    public Spinner(String id, String nombres) {
        this.id = id;
        this.nombres = nombres;

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

    @Override
    public String toString() {
        return nombres;
    }


}
