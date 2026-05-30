package GestionAlojamiento.Exception;

public class UnautorizedExeption extends RuntimeException {
    public UnautorizedExeption(String message) {
        super(message);
    }
}
