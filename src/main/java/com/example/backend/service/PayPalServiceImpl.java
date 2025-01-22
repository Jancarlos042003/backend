package com.example.backend.service;

import com.example.backend.dto.ItemOrdenLibroDTO;
import com.example.backend.dto.RespuestaPagoDTO;
import com.example.backend.dto.SolicitudOrdenDTO;
import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.DetalleOrden;
import com.example.backend.model.Libro;
import com.example.backend.model.Orden;
import com.example.backend.model.Usuario;
import com.example.backend.repository.DetalleOrdenRepository;
import com.example.backend.repository.LibroRepository;
import com.example.backend.repository.OrdenRepository;
import com.example.backend.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor // Genera automáticamente un constructor para los campos final
public class PayPalServiceImpl implements  PayPalService {
    @Value("${paypal.client.id}")
    private String clienteId;

    @Value("${paypal.secret}")
    private String clienteClave;

    @Value("${paypal.api.base.url}")
    private String urlBase;

    // Para hacer solicitudes HTTP. En este caso para comunicarse con la API de PayPal
    private final RestTemplate restTemplate;

    // Convertir objetos Java en JSON y viceversa.
    private final ObjectMapper objectMapper;

    private final UsuarioRepository usuarioRepository;
    private final OrdenRepository ordenRepository;
    private final LibroRepository libroRepository;
    private final DetalleOrdenRepository detalleOrdenRepository;

    // Servicio para enviar correos electrónicos
    private final EmailService emailService;

    private final Executor taskExecutor; // Permitiría ejecutar el envío de correos en un hilo separado (asincrono)

    @Override
    @Transactional // Aseguramos de que toda la operación de guardar la orden y los detalles esté dentro de una transacción
    public RespuestaPagoDTO crearOrden(SolicitudOrdenDTO solicitudOrden) {
        try {
            String tokenAcceso = obtenerTokenAcceso();

            // CONTRUCCION DEL CUERPO DE LA SOLICITUD

            // cuerpoOrden es un JSON que representa la orden que se enviará a PayPal
            ObjectNode cuerpoOrden = objectMapper.createObjectNode();
            cuerpoOrden.put("intent", "CAPTURE"); // indica que la intención de la orden es capturar fondos

            // cantidad es un nodo JSON que representa el monto total de la transacción
            ObjectNode cantidad = objectMapper.createObjectNode();
            cantidad.put("currency_code", "USD"); // Tipo de moneda
            cantidad.put("value", solicitudOrden.getTotal().toString());

            // Representa la unidad de compra( o "purchase unit") en PayPal
            ObjectNode unidadCompra = objectMapper.createObjectNode();
            unidadCompra.set("amount", cantidad);

            // Agregamos el unidadCompra al arreglo purchase_units(representa las unidades de compra que componen la orden)
            cuerpoOrden.putArray("purchase_units").add(unidadCompra);

            //CONFIGURACION DE LOS ENCABEZADOS HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenAcceso); // Se incluye el token de autencicacion en Bearer
            headers.setContentType(MediaType.APPLICATION_JSON); // Se especifica que el contenido de la solicitud es JSON

            // CREACION DE LA SOLICITUD HTTP
            // Se crear un objeto HttpEntity que contiene el cuerpo y los encabezados de la solicitud
            HttpEntity<String> entidad = new HttpEntity<>(cuerpoOrden.toString(), headers);

            // ENVIO DE LA SOLICITUD A PAYPAL PARA CREAR LA ORDEN
            ResponseEntity<JsonNode> respuesta = restTemplate.exchange(
                    urlBase + "/v2/checkout/orders",
                    HttpMethod.POST, // Se envia una solicitud POST a la API de PayPal
                    entidad,
                    JsonNode.class // La respuesta sera en un objeto JSON
            );

            // EXTRACCION DE DATOS DE LA RESPUESTA
            JsonNode cuerpoRespuesta = respuesta.getBody();
            String idOrdenPaypal = cuerpoRespuesta.get("id").asText(); // Se extrae el ID de la orden creada
            String estado = cuerpoRespuesta.get("status").asText(); // Se extrae el estado de la orden

            String urlAprobacion = StreamSupport.stream(cuerpoRespuesta.get("links").spliterator(), false)
                    .filter(link -> "approve".equals(link.get("rel").asText())) // Se busca en la lista de "links" el enlace de aprobacion("rel": "approve")
                    .map(link -> link.get("href").asText())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No se encontró el enlace de aprobación"));


            // CREA LA ORDEN PARA GUARDARLA EN LA BD

            // Verificamos que el usuario existe en la BD
            Usuario usuario = usuarioRepository.findById(solicitudOrden.getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

            // Creamos la orden
            Orden orden = Orden.builder()
            .idOrdenPaypal(idOrdenPaypal)
            .usuario(usuario)
            .monto(solicitudOrden.getTotal())
            .estado(estado)
            .fechaCreacion(LocalDateTime.now())
            .urlAprobacion(urlAprobacion)
            .build();


            //CREAMOS EL DETALLE DE LA ORDEN Y LA GUARDAMOS EN LA BD

            for(ItemOrdenLibroDTO item : solicitudOrden.getItems()){
                // Verificamos que el libro exista
                Libro libro = libroRepository.findById(item.getIdLibro())
                        .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con el ID: " + item.getIdLibro()));

                DetalleOrden detalleOrden = DetalleOrden.builder()
                        .idOrdenPaypal(idOrdenPaypal)
                        .libro(libro)
                        .cantidad(item.getCantidad())
                        .precio(item.getPrecio())
                        .descuento(item.getDescuento())
                        .direccion(solicitudOrden.getInformacionEnvio().getDireccion())
                        .telefono(solicitudOrden.getInformacionEnvio().getTelefono())
                        .build();

                detalleOrdenRepository.save(detalleOrden);
            }


            // Guardamos en la BD
            ordenRepository.save(orden);

            // RETORNO DEL RESULTADO
            // Crea y retorna la informacion de la orden, incluyendo la URL de la aprobacion
            return RespuestaPagoDTO.builder()
                    .idOrden(idOrdenPaypal)
                    .estado(estado)
                    .urlAprobacion(urlAprobacion)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error al crear la orden de PayPal: " + e.getMessage());
        }
    }

    // METODO QUE CONFIRMA LA CAPTURA DE FONDOS PARA UNA ORDEN ESPECIFICA
    @Override
    public RespuestaPagoDTO capturarOrden(String ordenId) {
        try {
            String tokenAcceso = obtenerTokenAcceso(); // Se obtiene el token de acceso

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(tokenAcceso); // Encabezados de autenticacion
            headers.setContentType(MediaType.APPLICATION_JSON); // Tipo de contenido JSON

            // Se crea la entidad HTTP sin cuerpo, ya que solo se necesitan encabezados para capturar la orden
            HttpEntity<String> entidad = new HttpEntity<>(headers);

            // Se envia una solicitud POST a la URL de captura de orden de PayPal
            ResponseEntity<JsonNode> respuesta = restTemplate.exchange(
                    urlBase + "/v2/checkout/orders/" + ordenId + "/capture",
                    HttpMethod.POST,
                    entidad,
                    JsonNode.class
            );

            JsonNode cuerpoRespuesta = respuesta.getBody();

            String nuevoEstado = cuerpoRespuesta.get("status").asText(); // Se extrae el nuevo estado de la orden

            // ACTUALIZAMOS EL ESTADO DE LA ORDEN
            Orden orden = ordenRepository.findByIdOrdenPaypal(ordenId)
                    .orElseThrow(() -> new ResourceNotFoundException("La orden no encontrada con el ID: " + ordenId));

            System.out.println(orden);

            orden.setEstado(nuevoEstado); // Actualizamos el estado
            orden.setFechaPago(LocalDateTime.now());
            ordenRepository.save(orden); // Guardamos en la BD

            // Se utiliza el Executor
            taskExecutor.execute(() -> emailService.enviarBoletaCompra(orden, orden.getUsuario().getUsername()));

            return RespuestaPagoDTO.builder()
                    .idOrden(ordenId) // Se asigna el ID de la orden
                    .estado(nuevoEstado) // Se asigna el nuevo estado de la orden
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error al capturar la orden de PayPal: " + e.getMessage());
        }
    }

    private String obtenerTokenAcceso() {
        // SE CREAN LOS ENCABEZADOS
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // Tipo de contenido que PayPal requiere para obtener tokens
        headers.setBasicAuth(clienteId, clienteClave); // Se configura los encabezados con Basic Auth usando el "clienteId" y "clienteSecreto"

        // SE DEFINE EL CUERPO DE LA SOLICITUD para obtener un token de acceso
        MultiValueMap<String, String> cuerpo = new LinkedMultiValueMap<>();
        cuerpo.add("grant_type", "client_credentials");

        // SE CREA UNA ENTIDAD HTTP con el cuerpo y los encabezados
        HttpEntity<MultiValueMap<String, String>> entidad = new HttpEntity<>(cuerpo, headers);

        // SE ENVIA LA SOLICITUD A PAYPAL para obtener un token de acceso
        ResponseEntity<JsonNode> respuesta = restTemplate.exchange(
                urlBase + "/v1/oauth2/token",
                HttpMethod.POST,
                entidad,
                JsonNode.class
        );

        // Log para verificar la respuesta
        String token = respuesta.getBody().get("access_token").asText(); // Extrae y retorna el "access_token" del cuerpo de la respuesta
        System.out.println("Token de acceso: " + token);

        return token;
    }
}
