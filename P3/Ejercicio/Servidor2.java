// # Fichero: servidor2.java
// # Segundo de los dos servidores replicados. 

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class Servidor2 {
    public static void main(String[] args) {
        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            String nombre_servidor = "Servidor2", nombre_replica = "Servidor1";
            Donaciones2 misDonaciones2 = new Donaciones2(nombre_servidor,nombre_replica);
            Naming.rebind(nombre_servidor, misDonaciones2);
            System.out.println("Servidor " +nombre_servidor+ " preparado.");
        } catch (RemoteException | MalformedURLException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }
}