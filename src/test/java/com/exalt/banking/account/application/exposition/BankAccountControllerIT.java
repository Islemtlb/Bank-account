package com.exalt.banking.account.application.exposition;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.exalt.banking.account.application.service.AccountApplicationService;
import com.exalt.banking.account.domain.model.AccountStatement;
import com.exalt.banking.account.domain.model.BankAccount;
import com.exalt.banking.account.domain.model.BankAccountType;
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
                                .body("overdraftLimit", equalTo(
                                                "Le plafond de découvert est requis et doit être supérieur à 0 pour créer un compte COURANT"));
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
        void createAccount_without_type_should_return_bad_request_with_error_message() throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-account-creation-request-without-type.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("accountType", equalTo("ne doit pas être nul"));
        }

        @Test
        void createAccount_without_overdraft_limit_should_return_bad_request_when_type_is_current() throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/valid-bank-account-creation-request-without-overdraft-limit.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("overdraftLimit", equalTo(
                                                "Le plafond de découvert est requis et doit être supérieur à 0 pour créer un compte COURANT"));

        }

        @Test
        void createOperation_with_valid_Json_should_add_operation_to_account() throws IOException {

                Long accountId = createAnyCurrentAccount();

                String operationJson = createSampleOperationJson(OperationType.CREDIT, new BigDecimal("100.00"));
                createOperation(accountId, operationJson);

                BankAccount createdAccount = service.getAccount(accountId);
                assertEquals(1, createdAccount.getOperations().size());

        }

        @Test
        void createAccount_with_empty_json_body_should_return_bad_request_with_error_message() throws IOException {
                // Arrange
                String expectedErrorMessage = "{\"balance\":\"ne doit pas être nul\",\"accountType\":\"ne doit pas être nul\"}";
                String accountJson = readJsonFile(
                                "src/test/resources/bank-account-creation-request-with-empty-body-request.json");

                // Act
                Response response = given()
                                .contentType(ContentType.JSON)
                                .body(accountJson)
                                .when()
                                .post("/accounts");

                // Assert
                response.then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body(equalTo(expectedErrorMessage));
        }

        @Test
        void createAccount_current_with_negative_balance_should_return_bad_request_with_error_message()
                        throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-current-account-creation-request-with-negative-balance.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("overdraftLimit", equalTo(
                                                "Le plafond de découvert est requis et doit être supérieur à 0 pour créer un compte COURANT"));
        }

        @Test
        void createAccount_savings_with_negative_balance_should_return_bad_request_with_error_message()
                        throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-savings-account-creation-request-with-negative-balance.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("savingsDepositLimit", equalTo(
                                                "Le plafond de dépôt est requis et doit être supérieur à 0 pour créer un compte d'épargne"));
        }

        @Test
        void createAccount_savings_with_balance_exceeding_deposit_limit_should_return_bad_request_with_error_message()
                        throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-savings-account-request-creation-with-balance-exceeding-deposit-limit.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("balance", equalTo(
                                                "La balance ne doit pas dépasser le plafond de dépôt"));
        }

        @Test
        void createAccount_current_with_negative_balance_and_negative_overdraft_limit_should_return_bad_request_with_error_message()
                        throws IOException {
                String invalidJson = readJsonFile(
                                "src/test/resources/bank-current-account-creation-request-with-negative-balance-and-negative-overdraft-limit.json");

                given()
                                .contentType(ContentType.JSON)
                                .body(invalidJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .body("overdraftLimit", equalTo(
                                                "Le plafond de découvert est requis et doit être supérieur à 0 pour créer un compte COURANT"));
        }

        @Test
        void getMonthlyStatement_should_return_correct_statement() throws IOException {
                // Given
                Long accountId = createAnyCurrentAccount();
                createOperation(accountId, createSampleOperationJson(OperationType.CREDIT, new BigDecimal("100.00")));
                createOperation(accountId, createSampleOperationJson(OperationType.DEBIT, new BigDecimal("50.00")));

                // When
                AccountStatement statement = given()
                                .when()
                                .get("/accounts/" + accountId + "/statement")
                                .then()
                                .statusCode(HttpStatus.OK.value())
                                .extract()
                                .response().as(AccountStatement.class);

                // Then
                assertEquals(BankAccountType.CURRENT, statement.getAccountType());
                assertEquals(new BigDecimal("50.00").setScale(2), statement.getBalance().setScale(2));
                assertEquals(2, statement.getOperations().size());
                assertEquals(new BigDecimal("50.00").setScale(2),
                                statement.getOperations().get(0).getAmount().setScale(2));
                assertEquals(new BigDecimal("100.00").setScale(2),
                                statement.getOperations().get(1).getAmount().setScale(2));
        }

        private void createOperation(Long accountId, String operationJson1) {
                given()
                                .contentType(ContentType.JSON)
                                .body(operationJson1)
                                .when()
                                .post("/accounts/" + accountId + "/operations")
                                .then()
                                .statusCode(HttpStatus.CREATED.value());
        }

        private Long createAnyCurrentAccount() throws IOException {
                String validJson = readJsonFile("src/test/resources/valid-bank-account-creation-request.json");
                Long accountId = given()
                                .contentType(ContentType.JSON)
                                .body(validJson)
                                .when()
                                .post("/accounts")
                                .then()
                                .statusCode(HttpStatus.CREATED.value())
                                .extract()
                                .response().as(Long.class);
                return accountId;
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
