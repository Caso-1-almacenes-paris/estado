package cl.paris.estado.exception;

/** Se lanza al violar una regla de negocio (seguimiento duplicado, estado terminal). -> HTTP 422 */
public class BusinessException extends RuntimeException {

    public BusinessException(String mensaje) {
        super(mensaje);
    }
}
