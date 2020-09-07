package aukde.food.gestordepedidos.paquetes.Modelos;

public class ProductoDefault {

    String nombreProducto;
    String descripcionProducto;
    String contenidoProducto;
    String stock;
    String tarifaConfidencial;
    String tarifaPublicada;
    String codigoINEA;
    String embalaje;
    String urlPhoto;

    public ProductoDefault(){
    }

    public ProductoDefault(String nombreProducto, String descripcionProducto, String contenidoProducto,
                           String stock, String tarifaConfidencial, String tarifaPublicada, String codigoINEA,
                           String embalaje, String urlPhoto) {
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.contenidoProducto = contenidoProducto;
        this.stock = stock;
        this.tarifaConfidencial = tarifaConfidencial;
        this.tarifaPublicada = tarifaPublicada;
        this.codigoINEA = codigoINEA;
        this.embalaje = embalaje;
        this.urlPhoto = urlPhoto;
    }


    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getContenidoProducto() {
        return contenidoProducto;
    }

    public void setContenidoProducto(String contenidoProducto) {
        this.contenidoProducto = contenidoProducto;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTarifaConfidencial() {
        return tarifaConfidencial;
    }

    public void setTarifaConfidencial(String tarifaConfidencial) {
        this.tarifaConfidencial = tarifaConfidencial;
    }

    public String getTarifaPublicada() {
        return tarifaPublicada;
    }

    public void setTarifaPublicada(String tarifaPublicada) {
        this.tarifaPublicada = tarifaPublicada;
    }

    public String getCodigoINEA() {
        return codigoINEA;
    }

    public void setCodigoINEA(String codigoINEA) {
        this.codigoINEA = codigoINEA;
    }

    public String getEmbalaje() {
        return embalaje;
    }

    public void setEmbalaje(String embalaje) {
        this.embalaje = embalaje;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
