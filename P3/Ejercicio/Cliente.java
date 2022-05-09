// # Fichero: cliente.java
// # Guarda la información de un cliente


public class Cliente {
    private String nombre;
    private String password;
    private float total_donado;


    Cliente(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
        this.total_donado = 0;
    }

    String getNombre() {
        return nombre;
    }

    String getPassword() {
        return password;
    }

    float getTotal_donado() {
        return total_donado;
    }

    void setTotal_donado(float total_donado) {
        this.total_donado = total_donado;
    }

    
    void incrementarTotalDonado(float cantidad) {
        this.total_donado+=cantidad;
    }
    
    
}