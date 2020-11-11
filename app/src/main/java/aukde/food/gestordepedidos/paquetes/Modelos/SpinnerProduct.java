package aukde.food.gestordepedidos.paquetes.Modelos;

public class SpinnerProduct {

    String id;
    String nombreProducto;

    public SpinnerProduct(String id, String nombreProducto) {
        this.id = id;
        this.nombreProducto = nombreProducto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    @Override
    public String toString() {
        return nombreProducto;
    }

}
