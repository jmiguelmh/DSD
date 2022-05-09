// # FICHERO: idonacion.java
// # Define la interfaz remota

import java.rmi.Remote;
import java.rmi.RemoteException;



public interface I_donacion extends Remote {
   
    // Comprueba que el cliente no esté regisgrado en ningún servidor. En ese caso
    // lo registra en el servidor con menos clientes. Devuelve 0 si lo ha registrado y 
    // -1 si no se ha podido registrar

    int registro(String cliente, String password) throws RemoteException;


    // Un cliente realiza una donación. La cantidad a donar debe ser superior a 0. Se ayuda de indentificarUsuario

    void deposito(String cliente,float cantidad) throws RemoteException;
    

    // Consulta las donaciones totales de un cliente en el servidor en el que esté registrado. Se ayuda de identificarUsuario.
    // Devuelve -1 si no ha hecho donaciones

    float consultarTotal(String cliente) throws RemoteException;
    
    // Devuelve el nomnbre del servidor en el que se ha registrado

    String identificarUsuario(String cliente, String password) throws RemoteException;

    // Obtiene el total donado por un cliente concreto

    float obtenerTotalDonadoDelCliente(String cliente) throws RemoteException;
  
}