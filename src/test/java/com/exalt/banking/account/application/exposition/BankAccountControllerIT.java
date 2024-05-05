package com.exalt.banking.account.application.exposition;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.exalt.banking.account.application.service.AccountApplicationService;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.OperationType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankAccountControllerIT {

        @LocalServerPort
        private int port;

        @Autowired
        private AccountApplicationService service;

        @BeforeEach
        public void setUp() {
                RestAssured.port = port;
        }

        @Test
        void createAccount_with_valid_Json_should_return_created() throws IOException {
                String validJson = readJsonFile("src/test/resources/valid-bank-account-creation-request.json");

                Long accountId = given()
                                .contentType(ContentType.JSON)
                                .body(validJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.CREATED.value())
                                .body(is(notNullValue()))
                                .extract()
                                .response().as(Long.class);

                BankAccount createdAccount = service.getAccount(accountId);

                BigDecimal actualBalance = createdAccount.getBalance().setScale(2);
                BigDecimal actualOverdraftLimit = createdAccount.getOverdraftLimit().setScale(2);

                assertEquals(getExpectedBigDecimalFromJson(validJson, "balance"), actualBalance);
                assertEquals(getExpectedBigDecimalFromJson(validJson, "overdraftLimit"), actualOverdraftLimit);

        }

        @Test
        void createAccount_with_negative_balance_exceeding_overdraft_limit_should_return_bad_request_with_error_message()
                        throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-account-creation-request-with-negative-balance-exceeding-overdraft-limit.json");

                given()
                                .contentType(ContentType.JSON)
                                .header("Accept-Language", "en")
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("balance", equalTo(
                                                "La balance négative ne doit pas dépasser la limite de découvert"));
        }

        @Test
        void createAccount_with_negative_overdraft_limit_should_return_bad_request_with_error_message()
                        throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-account-creation-request-with-negative-overdraft-limit.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("overdraftLimit", equalTo("doit être supérieur ou égal à 0.0"));
        }

        @Test
        void createAccount_without_balance_should_return_bad_request_with_error_message() throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-account-creation-request-without-balance.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("balance", equalTo("ne doit pas être nul"));
        }

        @Test
        void createAccount_without_overdraft_limit_should_return_bad_request() throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/valid-bank-account-creation-request-without-overdraft-limit.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("overdraftLimit", equalTo("ne doit pas être nul"));

        }

        @Test
        void createOperation_with_valid_Json_should_add_operation_to_account() throws IOException {

                String accountJson = readJsonFile("src/test/resources/valid-bank-account-creation-request.json");
                Long accountId = given()
                                .contentType(ContentType.JSON)
                                .body(accountJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.CREATED.value())
                                .extract()
                                .response().as(Long.class);

                String operationJson = createSampleOperationJson(OperationType.CREDIT, new BigDecimal("100.00"));
                given()
                                .contentType(ContentType.JSON)
                                .body(operationJson)
                                .when()
                                .post("/accounts/" + accountId + "/operations")
                                .then()
                                .statusCode(HttpStatus.CREATED.value());

                BankAccount createdAccount = service.getAccount(accountId);
                assertEquals(createdAccount.getOperations().size(), 1);

        }

        private BigDecimal getExpectedBigDecimalFromJson(String json, String key)
                        throws JsonProcessingException, JsonMappingException {
                Map<String, Object> values = parseJsonRequest(json);
                return new BigDecimal(values.get(key).toString()).setScale(2);
        }

        private String readJsonFile(String filePath) throws IOException {
                return new String(Files.readAllBytes(Paths.get(filePath)));
        }

        private Map<String, Object> parseJsonRequest(String json) throws JsonProcessingException, JsonMappingException {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
                });
        }

        private String createSampleOperationJson(OperationType type, BigDecimal amount) {
                return String.format("{\"type\":\"%s\",\"amount\":%s}", type.name(), amount);
        }
}
