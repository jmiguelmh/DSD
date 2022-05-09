// # Fichero: donacion_replica.java
// # Implementa la interfaz remota


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.*;


public class Donaciones2 extends UnicastRemoteObject implements I_donacion,
    I_donacion_replica {
    
    private Map<String, Cliente> clientes;  // Mapa que contiene a los clientes. 
           //Está formado por un par <String, Cliente>
        // donde el String es el nombre del cliente y cliente es una clase donde guardamos
        // los datos del cliente
    
    private String replica; // Nombre del objeto servidor réplica de donaciones
    private String nombre; // Nombre en la red del objeto.

    public Donaciones2(String nombre, String replica) throws RemoteException {
        clientes = new HashMap();
        this.replica = replica;
        this.nombre = nombre;
    }

    // Realiza un registro en el servidor. Comprueba que el cliente no existe en ninguno de los 
    // dos servidores, y lo regisdtra en el que tiene menos clientes

    public int registro(String cliente, String password) throws RemoteException {

        int codigo = 1;
        
        boolean existe_cliente_aqui = this.existeCliente(cliente);
        
        if (!existe_cliente_aqui){
            I_donacion_replica la_replica = this.getReplica("localhost", this.replica);
            boolean existe_cliente_replica = la_replica.existeCliente(cliente);
            
            if (!existe_cliente_replica){
                int num_clientes_aqui = this.getNumeroClientes();
                int num_clientes_replica = la_replica.getNumeroClientes();
                
                if (num_clientes_aqui <= num_clientes_replica)
                    this.confirmarRegistro(cliente, password, this.nombre);
                else
                    la_replica.confirmarRegistro(cliente, password, la_replica.getNombre());
                
                codigo = 0;
            }
        }
        
        return codigo;
    }


    // Realiza el depósito de un cliente. 

    public void deposito(String cliente, float cantidad) throws RemoteException {
        Cliente client = clientes.get(cliente);
        this.incrementarSubtotal(cantidad);
        System.out.println("Donación de " +cantidad+ " del cliente "+cliente +".");
        client.incrementarTotalDonado(cantidad);
    } 
    
    // Consulta la cantidad donada a los servidores que ha hecho un cliente.  

    public float consultarTotal(String cliente) throws RemoteException {
        float valor;

        System.out.println("El cliente "+ cliente +" está consultando su cantidad donada");
        
        I_donacion_replica la_replica = this.getReplica("localhost", this.replica);

        if(la_replica.getSubtotal() > 0)
            valor = la_replica.getSubtotal();
        else
            valor = -1;

        return valor;
    }
    
    
    // Devuelve el nomnbre del servidor en el que se ha registrado el cliente

    public String identificarUsuario(String cliente, String password) throws RemoteException{
        String servidor = "";
        boolean identificado;
        boolean existe_cliente_aqui = this.existeCliente(cliente);
        
        if (!existe_cliente_aqui){
            I_donacion_replica la_replica = this.getReplica("localhost", this.replica);
            boolean existe_cliente_replica = la_replica.existeCliente(cliente); 
            
            if (existe_cliente_replica){
                identificado = la_replica.confirmarIdentificacion(cliente, password);
                if (identificado)
                    servidor = this.replica;
            }
        } else {
            identificado = this.confirmarIdentificacion(cliente, password);
            if (identificado)
                servidor = this.nombre;
        }
        return servidor;
    }

    // Obtiene el total donado por un cliente

    public float obtenerTotalDonadoDelCliente(String cliente) throws RemoteException{
        Cliente client = clientes.get(cliente);
        return client.getTotal_donado();
    }

    
    /****************************************************/
    
    // Realiza un registro en un servidor 

    public void confirmarRegistro(String cliente, String password,String nombre) throws RemoteException {
        
        clientes.put(cliente,new Cliente(cliente,password));
        System.out.println("Nuevo cliente: "+cliente);
    }
    
    // Consulta si existe el cliente

    public boolean existeCliente(String cliente) throws RemoteException{
        return clientes.containsKey(cliente);
    }
    
    // Devuelve el numero de clientes registrados en el servidor

    public int getNumeroClientes() throws RemoteException {
        return clientes.size();
    }

    // Obtiene el subtotal de donaciones del servidor

    public float getSubtotal() throws RemoteException {
        I_donacion_replica la_replica = this.getReplica("localhost", this.replica);
        return la_replica.getSubtotal();
    }
    
    // Obtiene la réplica del servidor

    public I_donacion_replica getReplica(String host, String nombre) throws RemoteException {
        I_donacion_replica la_replica = null;
        
        try {
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            la_replica = (I_donacion_replica)mireg.lookup(nombre);
        } catch(NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
        
        return la_replica;
    }
    
    // Consulta el nombre del servidor en el registry

    public String getNombre() throws RemoteException {
        return this.nombre;
    }
    
    // Aumenta el subtotal con la donación indicada

    public void incrementarSubtotal(float cantidad) throws RemoteException {

        I_donacion_replica la_replica = this.getReplica("localhost", this.replica);

        la_replica.incrementarSubtotal(cantidad);
    }
    
    
    // Comprueba que el cliente existe, para entre servidores

    public boolean confirmarIdentificacion(String cliente, String password) throws RemoteException{
        System.out.println("Intentando identificar al cliente " + cliente +".");
        String pass = clientes.get(cliente).getPassword();
        System.out.println("Resultado = " + pass.equals(password));
        return pass.equals(password);
    }
}