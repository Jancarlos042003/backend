package com.example.backend.service;

import com.example.backend.exceptions.ResourceNotFoundException;
import com.example.backend.model.DetalleOrden;
import com.example.backend.model.Orden;
import com.example.backend.repository.DetalleOrdenRepository;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void enviarBoletaCompra(Orden orden, String emailDestino) {
        try {
            // Construir cuerpo del correo
            String cuerpoCorreo = construirCuerpoCorreo(orden);

            // Generar el PDF de la boleta
            byte[] boletaPdf = generarBoletaPdf(orden);

            // Crear el mensaje de correo
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(emailDestino);
            helper.setSubject("Boleta de Compra - Orden " + orden.getIdOrdenPaypal());
            helper.setText(cuerpoCorreo, true); // True para indicar que el contenido es HTML
            helper.addAttachment("boleta_compra.pdf", new ByteArrayResource(boletaPdf));

            // Enviar el correo
            mailSender.send(mensaje);
            System.out.println("Correo enviado con éxito a " + emailDestino);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo electrónico", e);
        }
    }

    private String construirCuerpoCorreo(Orden orden) {
        // Formato amigable: Ejemplo --> "05 de diciembre de 2024, 02:49 AM"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, hh:mm a");

        // Formatear la fecha
        String fechaFormateada = orden.getFechaPago().format(formatter);

        return String.format(
                "<html>" +
                        "<body>" +
                        "<h2>Gracias por tu compra</h2>" +
                        "<p>Número de Orden: %s</p>" +
                        "<p>Fecha: %s</p>" +
                        "<p>Total: $%.2f</p>" +
                        "<p>Adjuntamos la boleta de compra en PDF</p>" +
                        "</body>" +
                        "</html>",
                orden.getIdOrdenPaypal(),
                fechaFormateada,
                orden.getMonto()
        );
    }

    private byte[] generarBoletaPdf(Orden orden) {
        try {
            // Para formatear la fecha a una manera mas amigable al usuario
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy, hh:mm a");

            // Obtener detalles de la orden
            List<DetalleOrden> detalleOrdenList = detalleOrdenRepository.findAllByIdOrdenPaypal(orden.getIdOrdenPaypal())
                    .orElseThrow(() -> new ResourceNotFoundException("Detalle de la orden no encontrada."));

            System.out.println(detalleOrdenList);

            // Crear un ByteArrayOutputStream para almacenar el PDF generado
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Crear un PdfWriter y un PdfDocument
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4, false);
            document.setMargins(36, 36, 36, 36); // Márgenes de 36 unidades en todos los lados

            // Añadir título y detalles de la orden
            document.add(new Paragraph("Boleta de Compra")
                    .setFontSize(18)
                    .setBold());
            document.add(new Paragraph("Número de Orden: " + orden.getIdOrdenPaypal()));
            document.add(new Paragraph("Fecha: " + orden.getFechaPago().format(formatter)));

            // Crear tabla para los detalles de la orden
            float[] columnWidths = {1, 1, 1, 1}; // Ancho relativo de columnas
            Table table = new Table(columnWidths);

            // Configurar el ancho de la tabla para que ocupe el 100% del ancho disponible
            table.setWidth(UnitValue.createPercentValue(100));

            // Celdas de encabezado con estilos
            table.addHeaderCell(new Cell().add(new Paragraph("LIBRO").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

            table.addHeaderCell(new Cell().add(new Paragraph("CANTIDAD").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

            table.addHeaderCell(new Cell().add(new Paragraph("PRECIO").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

            table.addHeaderCell(new Cell().add(new Paragraph("DESCUENTO").setBold())
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD)));

            // Llenar la tabla con datos
            for (DetalleOrden detalle : detalleOrdenList) {
                table.addCell(detalle.getLibro().getTitulo());
                table.addCell(String.valueOf(detalle.getCantidad()));

                // Formateo del precio con dos decimales
                table.addCell(String.format("$%.2f", detalle.getPrecio()));

                table.addCell(String.valueOf(detalle.getLibro().getDescuento()));
            }

            // Añadir tabla al documento
            document.add(table);

            // Añadir total
            document.add(new Paragraph("Total: $" + orden.getMonto()));

            // Cerrar el documento
            document.close();

            // Devolver el PDF como un array de bytes
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar PDF", e);
        }
    }

}
