// # Fichero: servidor1.java
// # Uno de los dos servidores replicados. Este se lanza primero debido a que crea el registro RMI


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Servidor1 {
    public static void main(String[] args) {
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            String nombre_servidor = "Servidor1", nombre_replica = "Servidor2";
            Registry reg=LocateRegistry.createRegistry(1099);
            Donaciones misDonaciones1 = new Donaciones(nombre_servidor,nombre_replica);
            Naming.rebind(nombre_servidor, misDonaciones1);
            System.out.println("" + nombre_servidor + " preparado.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}