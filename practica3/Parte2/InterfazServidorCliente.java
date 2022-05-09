import java.rmi.Remote;
import java.rmi.RemoteException;

// Interfaz utilizada por los servidores de cara al cliente

public interface InterfazServidorCliente extends Remote {
    
    // Realiza registro de un cliente a partir de su nombre y password
    // Devuelve true si se registra el cliente (no existe), false si no se registra (ya existe)
    public boolean realizarRegistro(String nombreCliente, String passwordCliente) throws RemoteException;

    // Realiza una donacion de un cliente a partir de su nombre y la cantidad donada
    // Devuelve true si se realiza la donacion, false si no se realiza
    public boolean realizarDonacion(String nombreCliente, float donacion) throws RemoteException;

    // Consulta la cantidad donada de un cliente a partir de su nombre
    // Devuelve -1 si no se ha podido consultar (el cliente no existe)
    public float consultarTotalDonado(String nombreCliente) throws RemoteException;

    // Indentifica si un cliente esta registrado en un servidor a partir de su nombre y password
    // Devuelve el nombre del servidor donde esta registrado para seguir comunicandose con ese servidor 
    public String identificarCliente(String nombreCliente, String password) throws RemoteException;

    // Obtiene la cantidad total donada por un cliente a partir de su nombre
    public float obtenerTotalDonado(String nombreCliente) throws RemoteException;
}