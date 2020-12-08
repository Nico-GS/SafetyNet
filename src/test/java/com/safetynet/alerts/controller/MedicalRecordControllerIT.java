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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * PersonController integration tests
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MedicalRecordControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext WebContext;

    @BeforeEach
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(WebContext).build();
    }

    @Test
    @Tag("CREATE")
    @DisplayName("ERROR CREATE Unknown Person with MedicalRecord")
    void errorCreateUnknownPersonWithMedicalRecord() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/medicalRecord")
                .contentType(APPLICATION_JSON).content(" { \r\n"
                        + "     \"firstName\":\"Nicolas\", \r\n"
                        + "     \"lastName\":\"Gros\", \r\n"
                        + "     \"birthdate\":\"23/03/1993\", \r\n"
                        + "     \"medications\":[\"hydroxychloroquine:6350mg\", \"anticovid:1000mg\"], \r\n"
                        + "     \"allergies\":[\"fourmisdesneiges\"] \r\n"
                        + "     }")
                .accept(APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict());
    }

    @Test
    @Tag("UPDATE")
    @DisplayName("UPDATE Person OK with MedicalRecord")
    void updatePersonOkWithMedicalRecordAndReturnIsOk() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                .contentType(APPLICATION_JSON).content(" { \r\n"
                        + "     \"firstName\":\"Tessa\", \r\n"
                        + "     \"lastName\":\"Carman\", \r\n"
                        + "     \"birthdate\":\"03/03/1856\", \r\n"
                        + "     \"medications\":[\"NEW MEDICATION:1550mg\", \"doliprane:1000mg\"], \r\n"
                        + "     \"allergies\":[\"NEW ALLERGY\"] \r\n"
                        + "     }")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @Tag("UPDATE")
    @DisplayName("UPDATE Person ERROR with Unknown Person")
    void updatePersonErrorWithUnknownPerson() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/medicalRecord")
                .contentType(APPLICATION_JSON).content(" { \r\n"
                        + "     \"firstName\":\"JeanJacques\", \r\n"
                        + "     \"lastName\":\"Goldman\", \r\n"
                        + "     \"birthdate\":\"25/12/1945\", \r\n"
                        + "     \"medications\":[\"NEW MEDICATION:10mg\", \"doliprane:10mg\"], \r\n"
                        + "     \"allergies\":[\"NEW ALLERGY:araignees\"] \r\n"
                        + "     }"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE Person OK when is in the list")
    void deletePersonOkWhenMedicalRecordIsDeleteAndInTheList() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .param("firstName", "Tessa").param("lastName", "Carman"))
                .andExpect(status().isOk());
    }

    @Test
    @Tag("DELETE")
    @DisplayName("DELETE Person ERROR when Unknown Person")
    void deletePersonErrorWhenUnknownPerson() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/medicalRecord")
                .param("firstName", "Nicolas").param("lastName", "Gros"))
                .andExpect(status().isNotFound());
    }

}
