// Clase utilizada para representar a los clientes
// Sus atributos son nombre, password y donacion
public class Cliente {
    private String nombre;
    private String password;
    private float donacion;

    Cliente(String nombre, String password)
    {
        this.nombre = nombre;
        this.password = password;
        this.donacion = 0.0f;
    }

    public String obtenerNombre()
    {
        return nombre;
    }

    public String obtenerPassword()
    {
        return password;
    }

    public float obtenerDonacion()
    {
        return donacion;
    }

    public void cambiarNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public void cambiarPassword(String password)
    {
        this.password = password;
    }

    public void cambiarDonacion(float donacion)
    {
        if(donacion > this.donacion)
            this.donacion = donacion;
    }
}
