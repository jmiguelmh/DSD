// # FICHERO idonacion_replica.java
// # Define la interfaz remota para una réplica

import java.rmi.Remote;
import java.rmi.RemoteException;
 
public interface I_donacion_replica extends Remote {
  

    // Consulta si existe el cliente

    boolean existeCliente(String cliente) throws RemoteException;
    
    // Consulta el número de clientes de un servidor

    int getNumeroClientes() throws RemoteException;
    
    // Subtotal de donaciones del servidor

    float getSubtotal() throws RemoteException;
    
    // Realiza registro en el servidor

    public void confirmarRegistro(String cliente, String password, String nombre) throws RemoteException;

    // Obtiene la réplica del servidor

    public I_donacion_replica getReplica(String host, String nombre) throws RemoteException;

    // Consulta el nombre dle servidor en la red (en el registry)

    public String getNombre() throws RemoteException;
    
    // Aumenta el subtotal con la donación indicada

    public void incrementarSubtotal(float cantidad) throws RemoteException;

    // Comprueba que el cliente existe

    public boolean confirmarIdentificacion(String cliente, String password) throws RemoteException;
    
 }