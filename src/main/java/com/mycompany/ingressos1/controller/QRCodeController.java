package com.mycompany.ingressos1.controller;

import com.google.zxing.WriterException;
import com.mycompany.ingressos1.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Controller para geração de códigos QR
 * 
 * Endpoints para gerar e servir QR codes dinamicamente
 */
@RestController
@RequestMapping("/api/qrcode")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    /**
     * Gera um QR code PNG a partir do hash do ingresso
     * GET /api/qrcode/{hashId}
     * 
     * @param hashId hash único do ingresso
     * @param size tamanho do QR code (opcional, padrão: 300)
     * @return imagem PNG do QR code
     */
    @GetMapping("/{hashId}")
    public ResponseEntity<?> gerarQRCode(
            @PathVariable String hashId,
            @RequestParam(required = false, defaultValue = "300") int size) {

        try {
            if (hashId == null || hashId.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Hash ID não pode estar vazio");
            }

            // Gerar QR code com tamanho customizável
            byte[] qrCodeImage = qrCodeService.gerarQRCodeCustomizado(hashId, size, size);

            // Retornar como imagem PNG
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                    "inline; filename=\"qrcode_" + hashId + ".png\"")
                .body(qrCodeImage);

        } catch (WriterException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao gerar QR code: " + e.getMessage());
        }
    }

    /**
     * Retorna informações sobre como usar a API de QR codes
     * GET /api/qrcode/info
     */
    @GetMapping("/info")
    public ResponseEntity<?> info() {
        return ResponseEntity.ok(
            "Endpoints de QR Code:\n" +
            "- GET /api/qrcode/{hashId}?size=300 - Gera um QR code PNG\n" +
            "- size: tamanho do QR code em pixels (padrão: 300)\n" +
            "Exemplo: /api/qrcode/abc123def456?size=400"
        );
    }
}
