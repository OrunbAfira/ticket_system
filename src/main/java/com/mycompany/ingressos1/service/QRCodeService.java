package com.mycompany.ingressos1.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * Serviço para geração de códigos QR
 * Utiliza a biblioteca ZXing para criar QR codes a partir de strings
 */
@Service
public class QRCodeService {

    private static final int QR_CODE_WIDTH = 300;
    private static final int QR_CODE_HEIGHT = 300;

    /**
     * Gera um código QR a partir do hash do ingresso
     * 
     * @param hashId hash único do ingresso
     * @return String codificada em Base64 da imagem PNG do QR code
     * @throws WriterException se houver erro ao escrever o QR code
     * @throws IOException se houver erro ao manipular a imagem
     */
    public String gerarQRCodeBase64(String hashId) throws WriterException, IOException {
        if (hashId == null || hashId.isEmpty()) {
            throw new IllegalArgumentException("Hash ID não pode estar vazio");
        }

        // Gerar QR code
        byte[] qrCodeImage = gerarQRCodeCustomizado(hashId, QR_CODE_WIDTH, QR_CODE_HEIGHT);
        String base64Image = Base64.getEncoder().encodeToString(qrCodeImage);

        return "data:image/png;base64," + base64Image;
    }

    /**
     * Gera um código QR com tamanho customizável e retorna como byte array
     * 
     * @param hashId hash único do ingresso
     * @param width largura do QR code
     * @param height altura do QR code
     * @return byte array da imagem PNG do QR code
     * @throws WriterException se houver erro ao escrever o QR code
     * @throws IOException se houver erro ao manipular a imagem
     */
    public byte[] gerarQRCodeCustomizado(String hashId, int width, int height) 
            throws WriterException, IOException {
        
        if (hashId == null || hashId.isEmpty()) {
            throw new IllegalArgumentException("Hash ID não pode estar vazio");
        }

        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Largura e altura devem ser maiores que 0");
        }

        // Criar o writer de QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        
        // Gerar a matriz de bits do QR code
        BitMatrix bitMatrix = qrCodeWriter.encode(
            hashId,
            BarcodeFormat.QR_CODE,
            width,
            height
        );

        // Converter BitMatrix para BufferedImage
        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        // Converter BufferedImage para byte array PNG
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", pngOutputStream);

        return pngOutputStream.toByteArray();
    }

    /**
     * Gera um código QR com tamanho customizável e retorna como Base64
     * 
     * @param hashId hash único do ingresso
     * @param width largura do QR code
     * @param height altura do QR code
     * @return String codificada em Base64 da imagem PNG do QR code
     * @throws WriterException se houver erro ao escrever o QR code
     * @throws IOException se houver erro ao manipular a imagem
     */
    public String gerarQRCodeBase64Customizado(String hashId, int width, int height) 
            throws WriterException, IOException {
        
        byte[] pngData = gerarQRCodeCustomizado(hashId, width, height);
        String base64Image = Base64.getEncoder().encodeToString(pngData);

        return "data:image/png;base64," + base64Image;
    }

    /**
     * Retorna a URL de um endpoint que gera um QR code dinamicamente
     * Útil para referências em templates
     * 
     * @param hashId hash único do ingresso
     * @return URL do endpoint QR code
     */
    public String obterURLQRCode(String hashId) {
        return "/api/qrcode/" + hashId;
    }
}
