package com.algaworks.ecommerce.jpql;

import com.algaworks.ecommerce.EntityManagerTest;
import com.algaworks.ecommerce.dto.ProdutoDTO;
import com.algaworks.ecommerce.model.Cliente;
import com.algaworks.ecommerce.model.Pedido;
import com.algaworks.ecommerce.model.Produto;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.PdfMerger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.ParameterMode;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPOutputStream;

public class BasicoJPQLTest extends EntityManagerTest {


    private static final Logger logger = LogManager.getLogger(BasicoJPQLTest.class);


    @Test
    public void usarDistinct() {
        String jpql = "select distinct p from Pedido p " +
                " join p.itens i join i.produto pro " +
                " where pro.id in (1, 2, 3, 4) ";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);

        List<Pedido> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());
        System.out.println(lista.size());
    }

    @Test
    public void jpqlEstudar() {


        StoredProcedureQuery storedProcedureQuery = entityManager
                .createStoredProcedureQuery("BuscarProdutosPorNome", Produto.class);
        storedProcedureQuery.registerStoredProcedureParameter("nomeProduto", String.class, ParameterMode.IN);
        storedProcedureQuery.setParameter("nomeProduto", "Kindle");

        List<Produto> resultado = storedProcedureQuery.getResultList();

        logger.info("testando logger...."+new Date());
        logger.error("testando logger...."+ new Date());
        resultado.forEach(x -> System.out.println("ID: " + x.getId() + " Nome: " + x.getNome()));

    }

    // Método para comprimir os bytes e salvar como arquivo comprimido
    public static void compressAndSaveAsFile(byte[] data, String outputPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(outputPath);
             GZIPOutputStream gzipOS = new GZIPOutputStream(fos)) {
            gzipOS.write(data);
        }
    }

    private static List<InputStream> loadBase64StreamsFromFolder(String folderPath) {
        List<InputStream> base64Streams = new ArrayList<>();
        try {
            // Caminho para a pasta que contém os arquivos base64
            Files.list(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .forEach(filePath -> {
                        try {
                            InputStream base64Stream = new FileInputStream(filePath.toFile());
                            base64Streams.add(base64Stream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64Streams;
    }

    @Test
    public void unificarDocumentos() {
        String folderPath = "E:/Downloads/base64teste";  // Pasta onde os arquivos base64 estão localizados

        List<InputStream> base64Streams = loadBase64StreamsFromFolder(folderPath);  // Carrega os streams de base64 do disco

        String outputFilePath = "C:/temp/arquivo-unificado.pdf";
        String compressedOutputFilePath = "C:/temp/arquivo-unificado-comprimido.pdf.gz";

        try {
            PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputFilePath));
            PdfMerger merger = new PdfMerger(pdfDoc);

            for (InputStream base64Stream : base64Streams) {
                Base64.Decoder decoder = Base64.getMimeDecoder();
                InputStream decodedStream = decoder.wrap(base64Stream);

                PdfDocument tempPdf = new PdfDocument(new PdfReader(decodedStream));
                merger.merge(tempPdf, 1, tempPdf.getNumberOfPages());
                tempPdf.close();

                // Fecha o stream de base64
                base64Stream.close();
            }

            pdfDoc.close();
            System.out.println("PDFs unidos com sucesso em " + outputFilePath);

            // Lê o arquivo unificado e comprime, depois salva como um novo arquivo comprimido
            byte[] pdfData = Files.readAllBytes(Paths.get(outputFilePath));
            compressAndSaveAsFile(pdfData, compressedOutputFilePath);

            System.out.println("Arquivo comprimido salvo com sucesso em " + compressedOutputFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void ordenarResultados() {
        String jpql = "select c from Cliente c order by c.nome asc"; // desc

        TypedQuery<Cliente> typedQuery = entityManager.createQuery(jpql, Cliente.class);

        List<Cliente> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(c -> System.out.println(c.getId() + ", " + c.getNome()));
    }

    @Test
    public void projetarNoDTO() {
        String jpql = "select new com.algaworks.ecommerce.dto.ProdutoDTO(id, nome) from Produto";

        TypedQuery<ProdutoDTO> typedQuery = entityManager.createQuery(jpql, ProdutoDTO.class);
        List<ProdutoDTO> lista = typedQuery.getResultList();
        Assert.assertFalse(lista.isEmpty());

        lista.forEach(p -> System.out.println(p.getId() + ", " + p.getNome()));
    }

    @Test
    public void projetarOResultado() {
        String jpql = "select id, nome from Produto";

        TypedQuery<Object[]> typedQuery = entityManager.createQuery(jpql, Object[].class);
        List<Object[]> lista = typedQuery.getResultList();

        Assert.assertTrue(lista.get(0).length == 2);

        lista.forEach(arr -> System.out.println(arr[0] + ", " + arr[1]));
    }

    @Test
    public void selecionarUmAtributoParaRetorno() {
        String jpql = "select p.nome from Produto p";

        TypedQuery<String> typedQuery = entityManager.createQuery(jpql, String.class);
        List<String> lista = typedQuery.getResultList();
        Assert.assertTrue(String.class.equals(lista.get(0).getClass()));

        String jpqlCliente = "select p.cliente from Pedido p";
        TypedQuery<Cliente> typedQueryCliente = entityManager.createQuery(jpqlCliente, Cliente.class);
        List<Cliente> listaClientes = typedQueryCliente.getResultList();
        Assert.assertTrue(Cliente.class.equals(listaClientes.get(0).getClass()));
    }

    @Test
    public void buscarPorIdentificador() {
        // entityManager.find(Pedido.class, 1)

        TypedQuery<Pedido> typedQuery = entityManager
                .createQuery("select p from Pedido p where p.id = 1", Pedido.class);

        Pedido pedido = typedQuery.getSingleResult();
        Assert.assertNotNull(pedido);

//        List<Pedido> lista = typedQuery.getResultList();
//        Assert.assertFalse(lista.isEmpty());
    }

    @Test
    public void mostrarDiferencaQueries() {
        String jpql = "select p from Pedido p where p.id = 1";

        TypedQuery<Pedido> typedQuery = entityManager.createQuery(jpql, Pedido.class);
        Pedido pedido1 = typedQuery.getSingleResult();
        Assert.assertNotNull(pedido1);

        Query query = entityManager.createQuery(jpql);
        Pedido pedido2 = (Pedido) query.getSingleResult();
        Assert.assertNotNull(pedido2);

//        List<Pedido> lista = query.getResultList();
//        Assert.assertFalse(lista.isEmpty());
    }
}
