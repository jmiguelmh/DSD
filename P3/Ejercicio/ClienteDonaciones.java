// # Fichero: cliente_donacion.java
// # Código del cliente

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;


public class ClienteDonaciones {
    

    public static void main(String[] args) {

        String host = "", cliente = "", password, opcion1 = "", opcion2 = "", servidor;
        String servidor1 = "Servidor1";
        float cantidad = 0, total;
        int valor_retorno;
        String menu_inicial = 
            "\n------------------- Menú de login -------------------\n"
                + "1: Registrarse\n"
                + "2: Identificarse\n"
                + "3: Salir\n";
        
        String menu_usuario_identificado = 
            "\n------------------- Menú de donaciones -------------------\n"
            + "1: Donar\n"
            + "2: Consultar total donado\n"
            + "3: Consultar mi total donado\n"
            + "4: Cerrar sesión\n";
        
        // Comienza la funcionalidad 
        Scanner teclado = new Scanner (System.in);
        System.out.println ("Escriba el nombre o IP del servidor: ");
        host = teclado.nextLine();
        // Crea e instala el gestor de seguridad
        if (System.getSecurityManager() == null) 
            System.setSecurityManager(new SecurityManager());
        
        try {
            // Crea el stub para el cliente especificando la dirección del servidor
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            I_donacion misDonaciones = (I_donacion)mireg.lookup(servidor1);
            
            boolean identificado = false, seguir = true;
            
            while (seguir) {
                
                System.out.println(menu_inicial);
                
                System.out.println("Elija una opción: ");
                opcion1 = teclado.nextLine();
                opcion1 = opcion1.toUpperCase();
                
                System.out.println();
                
                switch (opcion1){
                    
                    case "1":
                        System.out.println("Introduzca el nombre: ");
                        cliente = teclado.nextLine();
                        System.out.println("Introduzca la contraseña: ");
                        password = teclado.nextLine();
                        valor_retorno = misDonaciones.registro(cliente,password);
                        
                        if (valor_retorno == 1)
                            System.out.println("Nombre de cliente ya en uso. No estás registrado.");
                        else
                            System.out.println("Registrado correctamente como " + cliente);
                        
                        break;
                    case "2":
                        System.out.println("Introduzca su nombre de cuenta: ");
                        cliente = teclado.nextLine();
                        System.out.println("Introduzca su contraseña: ");
                        password = teclado.nextLine();
                        
                        servidor = misDonaciones.identificarUsuario(cliente, password);
                        
                        if (!servidor.equals("")){
                                misDonaciones = (I_donacion)mireg.lookup(servidor);
                            identificado = true;
                            System.out.println("Estas identificado. Mostrando el menú de donaciones:");
                        } else
                            System.out.println("Tu cuenta o contraseña son incorrectas");
                        break;
                    case "3":
                        seguir = false;
                        break;
                    default:
                        System.out.println("La opción no existe.");
                        break;
                }
                
                while (identificado){
                    
                    System.out.println(menu_usuario_identificado);
                
                    System.out.println("Elija una opción: ");
                    opcion2 = teclado.nextLine();
                    opcion2 = opcion2.toUpperCase();

                    System.out.println();
                    switch (opcion2){
                        case "1":
                            do {
                                System.out.println("Introduzca la cantidad a donar (>0): ");
                                cantidad = Float.parseFloat(teclado.nextLine());
                            } while (cantidad <= 0);
                            misDonaciones.deposito(cliente, cantidad);
                            System.out.println("Donación realizada.");
                            break;
                        case "2":
                            total = misDonaciones.consultarTotal(cliente);

                            if (total == -1)
                                System.out.println("Usted no ha realizado ninguna donación. Done al menos una"
                                        + " vez para poder consultar el total donado.");
                            else
                                System.out.println("Total donado: "+total);
                            break;
                        case "3":
                            System.out.println("Tu cantidad total de donaciones es: "+misDonaciones.obtenerTotalDonadoDelCliente(cliente));
                            break;
                        case "4":
                            identificado = false;
                            cliente = "";
                            System.out.println("Sesión cerrada.");
                            break;
                        default:
                            System.out.println("La opción no existe.");
                            break;
                    }
                }
            }
        } catch(NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
        
        System.exit(0);
    }
}