import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.Scanner;
import java.util.ArrayList;

public class ProgramaCliente {
    public static void imprimirMenuPrincipal() {
        System.out.println("1. Registrarse");
        System.out.println("2. Iniciar sesión");
        System.out.println("3. Salir");
    }

    public static void imprimirMenuOperaciones() {
        System.out.println("1. Realizar una donación");
        System.out.println("2. Consultar el total donado al servidor");
        System.out.println("3. Consultar el total donado por el cliente");
        System.out.println("4. Consultar la donación más grande del cliente");
        System.out.println("5. Consultar el numero de donaciones del cliente");
        System.out.println("6. Consultar el historial de donaciones del cliente");
    }

    public static void imprimirHistorialDonaciones(String nombreCliente, ArrayList<Float> historial) {
        
        if(historial.isEmpty()) {
            System.out.println("El cliente " + nombreCliente + " no ha realizado ninguna donacion");
        }
        else {
            System.out.println("Historial de donaciones del cliente " + nombreCliente + ":");
            
            for(int i = 0; i < historial.size(); i++) {
                System.out.println("Donacion " + (i+1) + ": " + historial.get(i));
            }
        }
    }

    public static void main(String[] args) {
        String host = "localhost";
        String nombreServidor = "servidor";
        String cliente = "";
        String password = "1234";
        float cantidad = 0.0f;
        boolean sesionIniciada = false;

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            Registry mireg = LocateRegistry.getRegistry(host, 1099);
            InterfazServidorCliente servidor = (InterfazServidorCliente) mireg.lookup(nombreServidor);

            System.out.println("Bienvenido al servidor de donaciones");
            System.out.println("Seleccione una opcion: ");

            Scanner sc = new Scanner(System.in);
            int opcion = 0;
            
            do {
                imprimirMenuPrincipal();
                System.out.print("Opción: ");
                opcion = sc.nextInt();
                System.out.println();

                if(opcion < 1 || opcion > 3)
                    System.out.println("La opción seleccionada no es válida");
                
            } while (opcion < 1 || opcion > 3);

            switch (opcion) {
                case 1:
                    System.out.print("Introduzca el nombre: ");
                    cliente = sc.nextLine();
                    cliente = sc.nextLine();

                    if(servidor.registrado(cliente)) {
                        System.out.println("El nombre introducido ya está registrado, pruebe con otro");
                        System.out.println ("Intentelo de nuevo, saliendo...");
                    } else {
                        System.out.print("Introduzca la contraseña: ");
                        password = sc.nextLine();
                        System.out.println();

                        if (servidor.realizarRegistro(cliente, password)) {
                            System.out.println(cliente + " se ha registrado");
                            sesionIniciada = true;
                        }
                        else
                            System.out.println("No se ha podido registrar el cliente");
                    }
                    break;

                case 2:
                    System.out.print("Introduzca el nombre: ");
                    cliente = sc.nextLine();
                    cliente = sc.nextLine();

                    System.out.print("Introduzca la contraseña: ");
                    password = sc.nextLine();

                    if (!servidor.identificarCliente(cliente, password).equals("")) {
                        System.out.println("El cliente se ha identificado");
                        sesionIniciada = true;
                    }
                    else
                        System.out.println("No se ha podido identificar el cliente");

                    break;

                case 3:
                        System.out.println("Saliendo...");
                    break;
            }

            System.out.println();

            if (sesionIniciada) {
                System.out.println("Menú de operaciones: ");
                do {
                    imprimirMenuOperaciones();
                    System.out.print("Opción: ");
                    opcion = sc.nextInt();
                    System.out.println();
    
                    if(opcion < 1 || opcion > 6)
                        System.out.println("La opción seleccionada no es válida");
                    
                } while (opcion < 1 || opcion > 6);

                switch (opcion) {
                    case 1:
                        System.out.print("Introduzca la cantidad a donar: ");
                        cantidad = sc.nextFloat();
                        servidor.realizarDonacion(cliente, cantidad);
                        System.out.println(cliente + " ha donado " + cantidad + " al servidor");
                        break;
                    
                    case 2:
                        System.out.println("Se ha donado en total al servidor " + servidor.obtenerSubtotalDonado());
                        break;
                    
                    case 3:
                        System.out.println(cliente + " ha donado en total al servidor " + servidor.obtenerDonacionCliente(cliente));
                        break;
                    
                    case 4:
                        System.out.println("La donación más grande de " + cliente + " es " + servidor.obtenerDonacionMaximaCliente(cliente));
                        break;
                    
                    case 5:
                        int vecesDonadas = servidor.obtenerNumeroDonacionesCliente(cliente);

                        if(vecesDonadas == 0)
                            System.out.println(cliente + " todavia no ha hecho ninguna donación");
                        else if (vecesDonadas == 1)
                            System.out.println(cliente + " ha donado 1 vez");
                        else
                            System.out.println(cliente + " ha donado " + vecesDonadas + " veces");

                        break;
                    
                    case 6:
                        ArrayList<Float> historial = servidor.obtenerHistorialDonaciones(cliente);
                        imprimirHistorialDonaciones(cliente, historial);
                        break;
                }
            }

        } catch(NotBoundException | RemoteException e) {
            System.err.println("Exception del sistema: " + e);
        }
    }
}