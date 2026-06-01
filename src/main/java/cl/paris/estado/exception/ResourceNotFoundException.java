package cl.paris.estado.exception;

/** Se lanza cuando un recurso (seguimiento, venta) no existe. -> HTTP 404 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
