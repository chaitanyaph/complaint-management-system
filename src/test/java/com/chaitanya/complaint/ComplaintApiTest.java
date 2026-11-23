package com.chaitanya.complaint;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ComplaintApiTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper json;

    private long create(String url, String body) throws Exception {
        String response = mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body))
                .andReturn().getResponse().getContentAsString();
        return json.readTree(response).get("id").asLong();
    }

    private String email(String prefix) {
        return prefix + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    private long newCustomer() throws Exception {
        return create("/api/customers", "{\"name\":\"Nina\",\"email\":\"" + email("c") + "\"}");
    }

    private long newEmployee() throws Exception {
        return create("/api/employees", "{\"name\":\"Omar\",\"email\":\"" + email("e") + "\",\"department\":\"Support\"}");
    }

    private long newComplaint(long customer) throws Exception {
        return create("/api/complaints",
                "{\"customerId\":" + customer + ",\"title\":\"Late delivery\",\"description\":\"Order never arrived\"}");
    }

    private void json(String method, String url, String body, int expected) throws Exception {
        var builder = method.equals("PUT") ? put(url) : patch(url);
        mvc.perform(builder.contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().is(expected));
    }

    @Test
    void aComplaintMovesThroughTheWholeLifecycle() throws Exception {
        long complaint = newComplaint(newCustomer());
        long employee = newEmployee();

        mvc.perform(get("/api/complaints/" + complaint)).andExpect(jsonPath("$.status").value("OPEN"));

        json("PUT", "/api/complaints/" + complaint + "/assign", "{\"employeeId\":" + employee + "}", 200);
        mvc.perform(get("/api/complaints/" + complaint))
                .andExpect(jsonPath("$.status").value("ASSIGNED"))
                .andExpect(jsonPath("$.assignedToId").value(employee));

        json("PATCH", "/api/complaints/" + complaint + "/status", "{\"status\":\"IN_PROGRESS\"}", 200);
        json("PUT", "/api/complaints/" + complaint + "/resolve", "{\"resolution\":\"Refund issued\"}", 200);
        mvc.perform(get("/api/complaints/" + complaint))
                .andExpect(jsonPath("$.status").value("RESOLVED"))
                .andExpect(jsonPath("$.resolution").value("Refund issued"))
                .andExpect(jsonPath("$.resolvedAt").isNotEmpty());

        json("PATCH", "/api/complaints/" + complaint + "/status", "{\"status\":\"CLOSED\"}", 200);
        json("PUT", "/api/complaints/" + complaint + "/assign", "{\"employeeId\":" + employee + "}", 409);
    }

    @Test
    void invalidTransitionsAreRejected() throws Exception {
        long complaint = newComplaint(newCustomer());

        json("PATCH", "/api/complaints/" + complaint + "/status", "{\"status\":\"CLOSED\"}", 409);
        json("PUT", "/api/complaints/" + complaint + "/resolve", "{\"resolution\":\"Too early\"}", 409);
        json("PATCH", "/api/complaints/" + complaint + "/status", "{\"status\":\"RESOLVED\"}", 409);
    }

    @Test
    void resolvingNeedsAResolutionText() throws Exception {
        long complaint = newComplaint(newCustomer());
        json("PUT", "/api/complaints/" + complaint + "/resolve", "{\"resolution\":\"\"}", 400);
    }

    @Test
    void complaintsCanBeFilteredByStatusAndEmployee() throws Exception {
        long customer = newCustomer();
        long employee = newEmployee();
        long assigned = newComplaint(customer);
        long untouched = newComplaint(customer);
        json("PUT", "/api/complaints/" + assigned + "/assign", "{\"employeeId\":" + employee + "}", 200);

        mvc.perform(get("/api/complaints").param("employeeId", String.valueOf(employee)))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(assigned));

        mvc.perform(get("/api/complaints").param("status", "OPEN"))
                .andExpect(jsonPath("$[*].status", everyItem(is("OPEN"))))
                .andExpect(jsonPath("$[*].id", hasItem((int) untouched)))
                .andExpect(jsonPath("$[*].id", not(hasItem((int) assigned))));
    }

    @Test
    void unknownReferencesAreNotFoundAndBadInputGetsFieldErrors() throws Exception {
        mvc.perform(post("/api/complaints").contentType(MediaType.APPLICATION_JSON)
                .content("{\"customerId\":987654,\"title\":\"x\"}")).andExpect(status().isNotFound());
        mvc.perform(post("/api/complaints").contentType(MediaType.APPLICATION_JSON).content("{\"title\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fields.customerId").exists())
                .andExpect(jsonPath("$.fields.title").exists());
    }
}
