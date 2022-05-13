// Clase utilizada para representar a los clientes
// Sus atributos son nombre, password y donacion
public class Cliente {
    private String nombre;
    private String password;
    private float donacion;
    private float donacionTotal;
    private int numeroDonaciones;
    private float donacionMaxima;

    Cliente(String nombre, String password)
    {
        this.nombre = nombre;
        this.password = password;
        this.donacion = 0.0f;
        this.donacionTotal = 0.0f;
        this.numeroDonaciones = 0;
        this.donacionMaxima = 0.0f;
    }

    public String obtenerNombre()
    {
        return nombre;
    }

    public String obtenerPassword()
    {
        return password;
    }

    public float obtenerUltimaDonacion()
    {
        return donacion;
    }

    public float obtenerDonacionTotal()
    {
        return donacionTotal;
    }

    public int obtenerNumeroDonaciones()
    {
        return numeroDonaciones;
    }

    public float obtenerDonacionMaxima()
    {
        return donacionMaxima;
    }

    public void cambiarNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public void cambiarPassword(String password)
    {
        this.password = password;
    }

    public void donar(float donacion)
    {
        this.donacion = donacion;
        this.donacionTotal += donacion;

        if(this.donacion > this.donacionMaxima)
            this.donacionMaxima = this.donacion;
        
            this.incrementarNumeroDonaciones();
    }

    public void incrementarNumeroDonaciones()
    {
        this.numeroDonaciones++;
    }
}
