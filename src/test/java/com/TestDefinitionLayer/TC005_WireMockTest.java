package com.TestDefinitionLayer;

import com.api.rest.ApiActions;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Link("https://jira.cloud.com")
@Feature("MockApi1")
@Feature("MockApi2")
public class TC005_WireMockTest extends ApiActions {
    WireMockServer wireMockServer;

    @BeforeTest
    public void setupServices() {
        wireMockServer = new WireMockServer(8082);
        wireMockServer.start();
        setupStub();
        RestAssured.baseURI = "http://localhost:8082";
    }

    @AfterTest
    public void tearDownServices() {
        wireMockServer.stop();
    }

    public void setupStub() {
        setupMockData(wireMockServer, "/api/branches", "json/branches.json");
        setupMockData(wireMockServer, "/api/bank", "json/bank.json");
        setupMockData(wireMockServer, "/api/atms", "json/atms.json");
    }

    @Severity(SeverityLevel.MINOR)
    @Test(description = "E2E test for status code")
    @Description("Get StatusCOde")
    @Story("Test StatusCode")
    public void testStatusCodePositive() {
        Response response = httpGet("/api/branches");
        Assert.assertEquals(getStatusCode(response) /*actual value*/, 200 /*expected value*/, "Correct status code returned");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Negative Test")
    @Description("Test negative scenario")
    @Story("Test StatusCode")
    public void testStatusCodeNegative() {
        Response response = httpGet("/api/v1/branches");
        Assert.assertEquals(getStatusCode(response) /*actual value*/, 404 /*expected value*/, "Correct status code returned");
    }

    @Severity(SeverityLevel.MINOR)
    @Test(description = "E2E test for all branches")
    @Description("Get All Branch")
    @Story("Test Branches")
    public void TestBranches() {
        Response response = httpGet("/api/branches");
        Assert.assertEquals(getStatusCode(response) /*actual value*/, 200 /*expected value*/, "Correct status code returned");
        String title = (String) jsonPathEvaluator(response, "Data.Branches[0].BranchIdentification");
        Assert.assertEquals("Belfast City Branch", title);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(description = "E2E test for all banks")
    @Description("Get Bank")
    @Story("Test Bank")
    public void TestBank() {
        Response response = httpGet("/api/bank");
        Assert.assertEquals(getStatusCode(response) /*actual value*/, 200 /*expected value*/, "Correct status code returned");
        String title = (String) jsonPathEvaluator(response, "short_name");
        Assert.assertEquals("The Royal Bank of Scotland", title);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(description = "E2E test for atm")
    @Description("Get Atm")
    @Story("Test Atm")
    public void TestAtm() {
        Response response = httpGet("/api/atms");
        Assert.assertEquals(getStatusCode(response) /*actual value*/, 200 /*expected value*/, "Correct status code returned");
        Assert.assertEquals(getContentType(response) /*actual value*/, "application/json" /*expected value*/, "Correct status code returned");
        String title = (String) jsonPathEvaluator(response, "Data.Atm[0].AtmServices[0]");
        Assert.assertEquals("CashWithdrawal", title);
    }
}
