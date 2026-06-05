# estado

Microservicio de **seguimiento de despacho** del marketplace Paris (Caso 1, DSY1103).

## Responsabilidad
Gestionar el estado del envío de cada venta y su trazabilidad (historial de cambios).
Lo consulta el cliente; lo actualiza el vendedor al despachar.

## Puerto
`8087`

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/v1/envios` | Crea el seguimiento de una venta pagada |
| GET | `/api/v1/envios/{id}` | Obtiene un seguimiento |
| GET | `/api/v1/envios/venta/{ventaId}` | Seguimiento de una venta |
| GET | `/api/v1/envios/cliente/{clienteId}` | Seguimientos de un cliente |
| PATCH | `/api/v1/envios/{id}/estado` | Actualiza el estado (vendedor) y lo registra en el historial |
| GET | `/api/v1/envios/{id}/historial` | Historial de cambios de estado |

### Ejemplo POST `/api/v1/envios`
```json
{ "ventaId": 1001 }
```
### Ejemplo PATCH `/api/v1/envios/{id}/estado`
```json
{ "estado": "ENVIADO", "comentario": "Despachado por courier" }
```

## Reglas de negocio
- La venta debe existir y estar `PAGADA` (validación cruzada vía WebClient a `ventas`).
- Una venta tiene un único seguimiento (si no → 422).
- No se modifica un envío en estado terminal (`ENTREGADO` / `CANCELADO`).

## Comunicación (WebClient)
- `GET ventas/api/v1/ventas/{ventaId}` → valida venta, toma `clienteId` y `proveedorId`.

## Base de datos
BD **independiente** en Neon (`estadodb`). Variables de entorno:
```
ESTADO_DB_URL, ESTADO_DB_USER, ESTADO_DB_PASS
VENTAS_URL (default http://localhost:8085)
```

## Modelo de datos
```
seguimiento (1) ──< (N) historial_estado
```
- `EstadoEnvio`: PENDIENTE, PREPARANDO, ENVIADO, EN_REPARTO, ENTREGADO, CANCELADO
- Nº seguimiento: `ENV-<ventaId>`
