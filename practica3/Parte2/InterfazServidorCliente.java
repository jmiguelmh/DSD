import java.rmi.Remote;
import java.rmi.RemoteException;

// Interfaz utilizada por los servidores de cara al cliente

public interface InterfazServidorCliente extends Remote {
    
    // Realiza registro de un cliente a partir de su nombre y password
    // Devuelve true si se registra el cliente (no existe), false si no se registra (ya existe)
    public boolean realizarRegistro(String nombreCliente, String password) throws RemoteException;

    // Realiza una donacion de un cliente a partir de su nombre y la cantidad donada
    public void realizarDonacion(String nombreCliente, float donacion) throws RemoteException;

    // Indentifica si un cliente esta registrado en un servidor a partir de su nombre y password
    // Devuelve el nombre del servidor donde esta registrado para seguir comunicandose con ese servidor 
    public String identificarCliente(String nombreCliente, String password) throws RemoteException;

    // Obtiene la cantidad total donada en un servidor
    public float obtenerSubtotalDonado() throws RemoteException;

    // Obtiene la cantidad total donada a un servidor por un cliente
    public float obtenerDonacionCliente(String nombreCliente) throws RemoteException;

    // Obtiene el numero de donaciones de un cliente
    public int obtenerNumeroDonacionesCliente(String nombreCliente) throws RemoteException;

    // Obtiene la donacion maxima de un servidor hecha por un cliente
    public float obtenerDonacionMaximaCliente(String nombreCliente) throws RemoteException;
}