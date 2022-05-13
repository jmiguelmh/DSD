import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DonacionesServidor extends UnicastRemoteObject implements InterfazServidorCliente, InterfazServidorReplica {
    private String servidor;
    private String replica;
    private float subtotalDonado;

    private Map<String, Cliente> clientesRegistrados;

    // Constructor
    public DonacionesServidor(String servidor, String replica) throws RemoteException {
        this.servidor = servidor;
        this.replica = replica;
        this.subtotalDonado = 0.0f;
        clientesRegistrados = new HashMap<>();
    }

    // Realiza el registro de un cliente a partir de su nombre y password
    // El registro se realiza en el servidor donde haya menos clientes registrados
    // Devuelve true si el cliente se registra en uno de los servidores, false si no
    // se registra
    @Override
    public boolean realizarRegistro(String nombreCliente, String password) throws RemoteException {
        // Se comprueba si existe en este servidor
        boolean registrado = this.existeCliente(nombreCliente);
        boolean resultado = false;

        if (!registrado) {
            // Se comprueba si existe en la replica
            InterfazServidorReplica servidorReplica = this.obtenerReplica("localhost", this.replica);
            boolean registradoEnReplica = servidorReplica.existeCliente(nombreCliente);

            if (!registradoEnReplica) {
                int clientesEnServidor = this.obtenerNumeroClientesRegistrados();
                int clientesEnReplica = this.obtenerNumeroClientesRegistrados();

                if (clientesEnServidor < clientesEnReplica)
                    this.confirmarRegistroCliente(nombreCliente, password, this.servidor);
                else
                    this.confirmarRegistroCliente(nombreCliente, password, this.replica);

                resultado = true;
            }
        }

        return resultado;
    }

    // Realiza una donacion por parte de un cliente a partir de su nombre
    @Override
    public void realizarDonacion(String nombreCliente, float donacion) throws RemoteException {
        Cliente cliente = clientesRegistrados.get(nombreCliente);
        this.incrementarSubtotalDonado(donacion);
        cliente.donar(donacion);
        System.out.println("El cliente con el nombre " + nombreCliente + " ha donado " + donacion);
    }

    // Devuelve el nombre en el que se ha registrado el cliente
    @Override
    public String identificarCliente(String nombreCliente, String password) throws RemoteException {
        String nombreServidor = "";
        boolean registrado = this.existeCliente(nombreCliente);
        boolean identificado;

        if (!registrado) {
            InterfazServidorReplica servidorReplica = this.obtenerReplica("localhost", this.replica);
            boolean registradoEnReplica = servidorReplica.existeCliente(nombreCliente);

            if (registradoEnReplica) {
                identificado = servidorReplica.confirmarIdentificacionCliente(nombreCliente, password);

                if(identificado)
                    nombreServidor = this.replica;
            }
        } else {
            identificado = this.confirmarIdentificacionCliente(nombreCliente, password);
            if (identificado)
                nombreServidor = this.servidor;
        }

        return nombreServidor;
    }

    // Comprueba si existe el cliente en clientesRegistrados
    // Devuelve true si existe, false si no existe
    @Override
    public boolean existeCliente(String nombreCliente) throws RemoteException {
        return clientesRegistrados.containsKey(nombreCliente);
    }

    // Devuelve el numero de clientes registrados
    @Override
    public int obtenerNumeroClientesRegistrados() throws RemoteException {
        return clientesRegistrados.size();
    }

    // Devuelve el subtotal donado al servidor
    @Override
    public float obtenerSubtotalDonado() throws RemoteException {
        return this.subtotalDonado;
    }

    // Introduce a un cliente en clientesRegistrados
    @Override
    public void confirmarRegistroCliente(String nombreCliente, String password, String nombreServidor) throws RemoteException {
        clientesRegistrados.put(nombreCliente, new Cliente(nombreCliente, password));
        System.out.println("Se acaba de registrar el cliente " + nombreCliente);
    }

    // Devuelve una referencia a la replica del servidor, es una referencia a null si no se encuentra la replica
    @Override
    public InterfazServidorReplica obtenerReplica(String host, String nombreReplica) throws RemoteException {
        InterfazServidorReplica servidorReplica = null;

        try {
            Registry registroRMI = LocateRegistry.getRegistry(host, 1099);
            servidorReplica = (InterfazServidorReplica) registroRMI.lookup(nombreReplica);
        } catch (NotBoundException | RemoteException e) {
            System.out.println("No se encuentra el servidor replica con el nombre " + nombreReplica);
        }

        return servidorReplica;
    }

    // Devuelve el nombre del servidor
    @Override
    public String obtenerNombreServidor() throws RemoteException {
        return this.servidor;
    }

    // Incrementa el subtotal donado al servidor en una cantidad
    @Override
    public void incrementarSubtotalDonado(float donacion) throws RemoteException {
        this.subtotalDonado += donacion;
    }

    // Comprueba si el nombre y password de un cliente son correctos entre servidores
    @Override
    public boolean confirmarIdentificacionCliente(String nombreCliente, String password) throws RemoteException {
        String passwordCliente = clientesRegistrados.get(nombreCliente).obtenerPassword();

        return password.equals(passwordCliente);
    }

    // Obtiene la cantidad total donada por un cliente
    @Override
    public float obtenerDonacionCliente(String nombreCliente) throws RemoteException {
        Cliente cliente = clientesRegistrados.get(nombreCliente);
        return cliente.obtenerDonacionMaxima();
    }

    // Obtiene el numero de donaciones que ha hecho un cliente
    @Override
    public int obtenerNumeroDonacionesCliente(String nombreCliente) throws RemoteException {
        Cliente cliente = clientesRegistrados.get(nombreCliente);
        return cliente.obtenerNumeroDonaciones();
    }

    // Obtiene la donacion maxima hecha por un cliente al servidor
    @Override
    public float obtenerDonacionMaximaCliente(String nombreCliente) throws RemoteException {
        Cliente cliente = clientesRegistrados.get(nombreCliente);
        return cliente.obtenerDonacionMaxima();
    }
}