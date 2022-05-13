import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class TestCliente {
    public static void main(String[] args) {
        String host = "localhost";
        String nombreServidor = "servidor";
        String cliente = "Pepe";
        String password = "1234";
        float cantidad = 100.0f;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            InterfazServidorCliente servidor = (InterfazServidorCliente) mireg.lookup(nombreServidor);

            // Pruebas del registro
            System.out.println("Pruebas de registro");
            // Cliente Pepe
            if (servidor.realizarRegistro(cliente, password))
                System.out.println("El cliente se ha registrado");
            else
                System.out.println("No se ha podido registrar el cliente");
            
            // Cliente Antonio
            cliente = "Antonio";
            if (servidor.realizarRegistro(cliente, password))
                System.out.println("El cliente se ha registrado");
            else
                System.out.println("No se ha podido registrar el cliente");
            
            // Otra vez con Antonio
            if (servidor.realizarRegistro(cliente, password))
                System.out.println("El cliente se ha registrado");
            else
                System.out.println("No se ha podido registrar el cliente");
            
            System.out.println();
            
            //Pruebas de la identificacion
            System.out.println("Pruebas de identificacion");
            // Cliente Pepe
            cliente = "Pepe";
            if (!servidor.identificarCliente(cliente, password).equals(""))
                System.out.println("El cliente se ha identificado");
            else
                System.out.println("No se ha podido identificar el cliente");

            // Mismo password, diferente nombre
            cliente = "Maria";
            if (!servidor.identificarCliente(cliente, password).equals(""))
                System.out.println("El cliente se ha identificado");
            else
                System.out.println("No se ha podido identificar el cliente");
            
            // Mismo nombre, diferente password
            cliente = "Pepe";
            password = "4321";
            if (!servidor.identificarCliente(cliente, password).equals(""))
                System.out.println("El cliente se ha identificado");
            else
                System.out.println("No se ha podido identificar el cliente");
            
            System.out.println();

            // Pruebas de donacion
            System.out.println("Cantidad donada total: " + servidor.obtenerSubtotalDonado());
            // Primera donacion (100)
            servidor.realizarDonacion(cliente, cantidad);
            System.out.println("Donacion: " + cantidad);
            System.out.println("Cantidad donada total: " + servidor.obtenerSubtotalDonado());

            // Segunda donacion (50)
            cantidad = 50.0f;
            servidor.realizarDonacion(cliente, cantidad);
            System.out.println("Donacion: " + cantidad);
            System.out.println("Cantidad donada total: " + servidor.obtenerSubtotalDonado());

            System.out.println();

            // Pruebas de consultar numero de donaciones
            // Numero de donaciones del cliente Pepe
            System.out.println("El cliente " + cliente + " ha donado " + servidor.obtenerNumeroDonacionesCliente(cliente) + " veces");
            
            // Numero de donaciones del cliente Antonio
            cliente = "Antonio";
            System.out.println("El cliente " + cliente + " ha donado " + servidor.obtenerNumeroDonacionesCliente(cliente) + " veces");
            
            // Pruebas de consultar donacion maxima
            // Donacion maxima del cliente Pepe
            cliente = "Pepe";
            System.out.println("La donacion maxima del cliente " + cliente + " es " + servidor.obtenerDonacionMaximaCliente(cliente));

            // Donacion maxima del cliente Antonio
            cliente = "Antonio";
            System.out.println("La donacion maxima del cliente " + cliente + " es " + servidor.obtenerDonacionMaximaCliente(cliente));

        } catch(NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
    }
}