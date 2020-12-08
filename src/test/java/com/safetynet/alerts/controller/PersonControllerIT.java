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
 * PersonController integration tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PersonControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext WebContext;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(WebContext).build();
    }

    @Test
    @Tag("CreatePerson")
    @DisplayName("Create Person OK when all informations are correct")
    void createPersonOkWhenAllInformationsAreCorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(APPLICATION_JSON)
                .content("{\"firstName\": \"Nicolas\",\"lastName\": \"Gros\",\"address\": \"Chemin des Richards\",\"city\": \"Saint Pierre de Soucy\",\"zip\": \"73800\",\"phone\": \"0642876413\",\"email\": \"nicolasgros@protonmail.com\"}")
                .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    @Tag("CreatePerson")
    @DisplayName("Create Person ERROR when Person already exist")
    void createPersonErrorWhenPersonAlreadyExist() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/person")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"firstName\":\"Peter\",\"lastName\":\"Duncan\",\"address\":\"644 Gershwin Cir\",\"city\":\"Culver\",\"zip\":\"97451\",\"phone\":\"841-874-6512\",\"email\":\"jaboyd@email.com\"}"))
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("UpdatePerson")
    @DisplayName("Update Person OK when Person exist")
    void updatePersonOkWhenPersonExist() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(APPLICATION_JSON)
                .content("{\"firstName\": \"Foster\",\"lastName\": \"Shepard\",\"address\": \"748 Townings Dr\",\"city\": \"Culver\",\"zip\": \"97451\",\"phone\": \"841-874-6544\",\"email\": \"jaboyd@email.com\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Tag("UpdatePerson")
    @DisplayName("Update Person ERROR when Person not found")
    void updatePersonErrorWhenPersonNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/person")
                .contentType(APPLICATION_JSON)
                .content("{\"firstName\":\"Nicolas\",\"lastName\":\"Gros\",\"address\":\"Le Paradis\",\"city\":\"StationLunaire\",\"zip\":\"00110011\",\"phone\":\"101-888-6666\",\"email\":\"mailinconnu@inconnu.com\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("DeletePerson")
    @DisplayName("Delete Person OK if Person in the list")
    void deletePersonOkIfPersonInTheList() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(APPLICATION_JSON)
                .content("{\"firstName\": \"Lily\",\"lastName\": \"Cooper\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Tag("DeletePerson")
    @DisplayName("Delete Person ERROR when Person is Not Found")
    void deletePersonErrorWhenPersonIsNotFound() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/person")
                .contentType(APPLICATION_JSON)
                .content("{\"firstName\": \"Nicolas\",\"lastName\": \"Gros\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("CommunityEmail")
    @DisplayName("Valid city entry 'Culver' - CommunityEmail")
    void validCityEntryCulverCommunityEmail() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                .contentType(APPLICATION_JSON).param("city", "Culver"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "[\"jaboyd@email.com\",\"drk@email.com\",\"tenz@email.com\",\"jaboyd@email.com\",\"jaboyd@email.com\",\"drk@email.com\",\"tenz@email.com\",\"jaboyd@email.com\",\"jaboyd@email.com\",\"tcoop@ymail.com\",\"lily@email.com\",\"soph@email.com\",\"ward@email.com\",\"zarc@email.com\",\"reg@email.com\",\"jpeter@email.com\",\"jpeter@email.com\",\"aly@imail.com\",\"bstel@email.com\",\"ssanw@email.com\",\"bstel@email.com\",\"clivfd@ymail.com\",\"gramps@email.com\"]"))
                .andExpect(jsonPath("$.length()", is(23)));
    }

    @Test
    @Tag("CommunityEmail")
    @DisplayName("Bad City entry : Paris")
    void badCityEntryParisCommunityEmail()
            throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/communityEmail")
                .contentType(APPLICATION_JSON).param("city", "Paris"))
                .andExpect(status().isNotFound()).andExpect(content().string(
                "[\"Not found : Paris\"]"));
    }

    @Test
    @Tag("PersonInfo")
    @DisplayName("PersonInfo - LastName is CORRECT")
    void personInfoLastNameIsCorrect() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/personInfo")
                .contentType(APPLICATION_JSON).param("firstName", "Tessa")
                .param("lastName", "Carman")).andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1)))
                .andExpect(content().string(
                        "[{\"firstName\":\"Tessa\",\"lastName\":\"Carman\",\"age\":8,\"address\":\"834 Binoc Ave\",\"city\":\"Culver\",\"zip\":\"97451\",\"email\":\"tenz@email.com\",\"medications\":[],\"allergies\":[]}]"));
    }

    @Test
    @Tag("PersonInfo")
    @DisplayName("PersonInfo - LastName is Unknown")
    void personInfoLastNameUnknown() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/personInfo")
                .contentType(APPLICATION_JSON).param("firstName", "Nicolas")
                .param("lastName", "Gros")).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()", is(0)));
    }

    @Test
    @Tag("ChildAlert")
    @DisplayName("ChildAlert OK - Address with Childs)")
    void childAlertAddressWithChilds() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
                .contentType(APPLICATION_JSON)
                .param("address", "1509 Culver St"))
                .andExpect(status().isOk()).andExpect(content().string(
                "[{\"firstName\":\"John\",\"lastName\":\"Boyd\",\"age\":36},{\"firstName\":\"Jacob\",\"lastName\":\"Boyd\",\"age\":31},{\"firstName\":\"Tenley\",\"lastName\":\"Boyd\",\"age\":8},{\"firstName\":\"Roger\",\"lastName\":\"Boyd\",\"age\":3},{\"firstName\":\"Felicia\",\"lastName\":\"Boyd\",\"age\":34}]"));
    }

    @Test
    @Tag("ChildAlert")
    @DisplayName("ChildAlert - Address without Childs")
    void childAlertAddressWithoutChilds() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/childAlert")
                .contentType(APPLICATION_JSON).param("address", "29 15th St"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.length()", is(0)));
    }

}
