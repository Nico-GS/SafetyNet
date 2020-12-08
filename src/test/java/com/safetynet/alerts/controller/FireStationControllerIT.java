package com.safetynet.alerts.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * FireStationController integration tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Tests modifie ApplicationContext
public class FireStationControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext WebContext;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(WebContext).build();
    }

    @Test
    @Tag("POST")
    @DisplayName("Add Address OK")
    void existingFireStationAddAddressStatusIsCreated() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(APPLICATION_JSON)
                .content("{\"address\": \"NEW ADDRESS\",\"station\": \"1\"}")
                .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("POST")
    @DisplayName("Add Address ERROR - Address already exist FireStation 4")
    void AddAddressErrorAddressAlreadyExistFireStation4()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"address\": \"489 Manchester St\",\"station\": \"4\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("POST")
    @DisplayName("Add Address ERROR - Address already exist FireStation 3")
    void AddAddressErrorAddressAlreadyExistFireStation3()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"address\": \"748 Townings Dr\",\"station\": \"3\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("POST")
    @DisplayName("Add Address ERROR - Address already exist FireStation 2")
    void AddAddressErrorAddressAlreadyExistFireStation2()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"address\": \"892 Downing Ct\",\"station\": \"2\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("POST")
    @DisplayName("Add Address ERROR - Address already exist FireStation 1")
    void AddAddressErrorAddressAlreadyExist()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/firestation")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"address\": \"947 E. Rose Dr\",\"station\": \"1\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("PUT")
    @DisplayName("Update Address OK")
    void updateSuccessWithFireStation2AndCheckIsOk()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                .contentType(APPLICATION_JSON)
                .content("{\"address\": \"892 Downing Ct\",\"station\": \"2\"}")
                .accept(APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("PUT")
    @DisplayName("Update Address ERROR with unknown Station number")
    void updateAddressErrorWithUnknownStationNumber() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/firestation")
                .contentType(APPLICATION_JSON) // APPLICATION_JSON_VALUE
                .content("{\"address\": \"Unknown address\",\"station\": \"1\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete existing address FireStation OK")
    void deleteExistingAddressFireStationThenReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/firestation")
                .contentType(APPLICATION_JSON).param("address", "951 LoneTree Rd")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("Delete non existing address - ERROR")
    void deleteNonExistingAddressError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/firestation")
                .contentType(APPLICATION_JSON).param("address", "Saint Pierre de Soucy City")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("FirestationNumber")
    @DisplayName("Firestation Number - ERROR")
    void givenUnknowStationNumber_whenRequest_thenReturnNotFound()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
                .contentType(APPLICATION_JSON).param("stationNumber", "499")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("PhoneAlert")
    @DisplayName("PhoneAlert OK with correct Station Number")
    void checkIfPhoneAlertOkWithCorrectStationNumber()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/phoneAlert")
                .contentType(APPLICATION_JSON).param("firestation", "1"))
                .andExpect(status().isOk()).andExpect(content().string(
                "[\"841-874-6512\",\"841-874-8547\",\"841-874-7462\",\"841-874-7784\",\"841-874-7784\",\"841-874-7784\"]"));
    }

    @Test
    @Tag("PhoneAlert")
    @DisplayName("PhoneAlert ERROR with Unknown Station Number '20'")
    public void checkErrorPhoneAlertWithUnknownStationNumber()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/firestation")
                .contentType(APPLICATION_JSON).param("stationNumber", "20")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("Fire")
    @DisplayName("Valid Address when Fire return OK")
    void validAddressWhenFireReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/fire")
                .contentType(APPLICATION_JSON)
                .param("address", "951 LoneTree Rd"))
                .andExpect(status().isOk()).andExpect(content().string(
                "[{\"stationNumber\":\"2\",\"firstName\":\"Eric\",\"lastName\":\"Cadigan\",\"age\":75,\"phoneNumber\":\"841-874-7458\",\"medications\":[\"tradoxidine:400mg\"],\"allergies\":[]}]"));
    }

    @Test
    @Tag("Fire")
    @DisplayName("Unknown Address when Fire return not found - ERROR")
    void unknownAddressWhenFireReturnNotFoundError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/fire")
                .contentType(APPLICATION_JSON).param("address", "INCONNU"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @Tag("Flood")
    @DisplayName("Flood two valid Station Entered '2-3' and return - OK")
    void floodTwoValidStationEnteredAndReturnOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations")
                .contentType(APPLICATION_JSON).param("stations", "2,3"))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("Flood")
    @DisplayName("Flood Unknown Station and return not found '33' - ERROR")
    void floodUnknownStationAndReturnNotFoundError() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/flood/stations")
                .contentType(APPLICATION_JSON).param("stations", "33"))
                .andExpect(status().isNotFound());
    }
}
