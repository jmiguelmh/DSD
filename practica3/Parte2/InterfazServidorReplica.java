import java.rmi.Remote;
import java.rmi.RemoteException;

// Interfaz utilizada por los servidores de cara al cliente

public interface InterfazServidorReplica extends Remote {

    // Comprueba si un cliente estar registrado en el servidor a partir de su nombre
    // Devuelve true si existe, false si no existe
    public boolean existeCliente(String nombreCliente) throws RemoteException;

    // Devuelve el numero de clientes registrados en el servidor
    // Necesario para hacer los registros en el servidor con menor numero de clientes
    public int obtenerNumeroClientesRegistrados() throws RemoteException;

    // Devuelve la cantidad de donaciones registradas solo en ese servidor
    public float obtenerSubtotalDonado() throws RemoteException;

    // Confirma el registro de un cliente en un servidor a partir de su nombre, password y nombre del servidor
    // Devuelve true si se registra, false si el registro falla
    public boolean confirmarRegistroCliente(String nombreCliente, String password, String nombreServidor) throws RemoteException;

    // Devuelve una referencia a la replica del servidor a partir del host y el nombre del servidor replica
    public InterfazServidorReplica obtenerReplica(String host, String nombreReplica) throws RemoteException;

    // Devuelve el nombre del servidor en el rmiregistry
    public String obtenerNombreServidor() throws RemoteException;

    // Incrementa el subtotal donado en el servidor
    // Devuelve true si la donacion tiene existo, false si falla
    public boolean incrementarSubtotalDonado(float donacion) throws RemoteException;

    // Confirma si un cliente se ha identificado correctamente entre servidores
    // Devuelve true si su nombre y password son correctas
    public boolean confirmarIdentificacionCliente(String nombreCliente, String password) throws RemoteException;
}