package com.Crpt.com;

import com.google.gson.Gson;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private static final String API_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final OkHttpClient client;
    private final Semaphore semaphore;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.client = createOkHttpClient(timeUnit, requestLimit);
        this.semaphore = new Semaphore(requestLimit);
    }

    public String createDocument(Document document, String signature) throws IOException, InterruptedException {
        semaphore.acquire();
        try {
            String json = new Gson().toJson(document);
            RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(requestBody)
                    .addHeader("Authorization", "Bearer " + signature)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Ошибка при создании документа: " + response.code());
                }
                return Objects.requireNonNull(response.body()).string();
            }
        } finally {
            semaphore.release();
        }
    }

    private OkHttpClient createOkHttpClient(TimeUnit timeUnit, int requestLimit) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public static class Document {
        final String discription;
        final String doc_id;
        final String doc_status;
        final String doc_type;
        final String importRequest;
        final String owner_inn;
        final String participant_inn;
        final String producer_inn;
        final Date production_date;
        final String production_type;
        final Date red_date;
        final String reg_number;
        final Products products;

        public Document(String discription, String doc_id, String doc_status, String doc_type,
                        String importRequest, String owner_inn, String participant_inn,
                        String producer_inn, Date production_date, String production_type,
                        Date red_date, String reg_number,Products products) {
            this.discription = discription;
            this.doc_id = doc_id;
            this.doc_status = doc_status;
            this.doc_type = doc_type;
            this.importRequest = importRequest;
            this.owner_inn = owner_inn;
            this.participant_inn = participant_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.production_type = production_type;
            this.red_date = red_date;
            this.reg_number = reg_number;
            this.products=products;
        }

        public static class DocumentBuilder {

            private String description;
            private String docId;
            private String docStatus;
            private String docType;
            private String importRequest;
            private String ownerInn;
            private String participantInn;
            private String producerInn;
            private Date productionDate;
            private String productionType;
            private Date redDate;
            private String regNumber;
            private Products products;

            public DocumentBuilder description(String description) {
                this.description = description;
                return this;
            }

            public DocumentBuilder docId(String docId) {
                this.docId = docId;
                return this;
            }

            public DocumentBuilder docStatus(String docStatus) {
                this.docStatus = docStatus;
                return this;
            }

            public DocumentBuilder docType(String docType) {
                this.docType = docType;
                return this;
            }

            public DocumentBuilder importRequest(String importRequest) {
                this.importRequest = importRequest;
                return this;
            }

            public DocumentBuilder ownerInn(String ownerInn) {
                this.ownerInn = ownerInn;
                return this;
            }

            public DocumentBuilder participantInn(String participantInn) {
                this.participantInn = participantInn;
                return this;
            }

            public DocumentBuilder producerInn(String producerInn) {
                this.producerInn = producerInn;
                return this;
            }

            public DocumentBuilder productionDate(Date productionDate) {
                this.productionDate = productionDate;
                return this;
            }

            public DocumentBuilder productionType(String productionType) {
                this.productionType = productionType;
                return this;
            }

            public DocumentBuilder redDate(Date redDate) {
                this.redDate = redDate;
                return this;
            }

            public DocumentBuilder regNumber(String regNumber) {
                this.regNumber = regNumber;
                return this;
            }
            public DocumentBuilder products(Products products){
                this.products = products;
                return this;
            }

            public Document build() {
                if (docId == null) {
                    throw new IllegalArgumentException("docId is mandatory");
                }

                return new Document(description, docId, docStatus, docType, importRequest,
                        ownerInn, participantInn, producerInn, productionDate, productionType,
                        redDate, regNumber, products);
            }
        }

    }

    public static class Products{
        final String certificate_document;
        final Date certificate_document_date;
        final String certificate_document_number;
        final String owner_inn;
        final String producer_inn;
        final Date production_date;
        final String tnved_code;
        final String uit_code;
        final String uitu_code;

        public Products(String certificate_document, Date certificate_document_date,
                        String certificate_document_number, String owner_inn, String producer_inn,
                        Date production_date, String tnved_code, String uit_code, String uitu_code) {
            this.certificate_document = certificate_document;
            this.certificate_document_date = certificate_document_date;
            this.certificate_document_number = certificate_document_number;
            this.owner_inn = owner_inn;
            this.producer_inn = producer_inn;
            this.production_date = production_date;
            this.tnved_code = tnved_code;
            this.uit_code = uit_code;
            this.uitu_code = uitu_code;
        }

        public static class ProductsBuilder {

            private String certificateDocument;
            private Date certificateDocumentDate;
            private String certificateDocumentNumber;
            private String ownerInn;
            private String producerInn;
            private Date productionDate;
            private String tnvedCode;
            private String uitCode;
            private String uituCode;

            public ProductsBuilder certificateDocument(String certificateDocument) {
                this.certificateDocument = certificateDocument;
                return this;
            }

            public ProductsBuilder certificateDocumentDate(Date certificateDocumentDate) {
                this.certificateDocumentDate = certificateDocumentDate;
                return this;
            }

            public ProductsBuilder certificateDocumentNumber(String certificateDocumentNumber) {
                this.certificateDocumentNumber = certificateDocumentNumber;
                return this;
            }

            public ProductsBuilder ownerInn(String ownerInn) {
                this.ownerInn = ownerInn;
                return this;
            }

            public ProductsBuilder producerInn(String producerInn) {
                this.producerInn = producerInn;
                return this;
            }

            public ProductsBuilder productionDate(Date productionDate) {
                this.productionDate = productionDate;
                return this;
            }

            public ProductsBuilder tnvedCode(String tnvedCode) {
                this.tnvedCode = tnvedCode;
                return this;
            }

            public ProductsBuilder uitCode(String uitCode) {
                this.uitCode = uitCode;
                return this;
            }

            public ProductsBuilder uituCode(String uituCode) {
                this.uituCode = uituCode;
                return this;
            }

            public Products build() {
                return new Products(certificateDocument, certificateDocumentDate, certificateDocumentNumber,
                        ownerInn, producerInn, productionDate, tnvedCode, uitCode, uituCode);
            }
        }

    }
}

