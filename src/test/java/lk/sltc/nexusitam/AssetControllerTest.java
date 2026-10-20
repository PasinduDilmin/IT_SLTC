package lk.sltc.nexusitam;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.sltc.nexusitam.dto.AssetRequestDTO;
import lk.sltc.nexusitam.model.enums.AssetStatus;
import lk.sltc.nexusitam.model.enums.CampusLocation;
import lk.sltc.nexusitam.model.enums.DeviceType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnInitialAssetList() throws Exception {
        mockMvc.perform(get("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", not(empty())))
                .andExpect(jsonPath("$.totalElements", greaterThan(0)));
    }

    @Test
    void shouldFilterAssetsByStatus() throws Exception {
        mockMvc.perform(get("/api/assets?status=AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status", is("AVAILABLE")));
    }

    @Test
    void shouldFindAssetBySerial() throws Exception {
        mockMvc.perform(get("/api/assets/serial/NXJ0LSG00243403CC62N00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assetTag", is("NEX-LP-BAIT01")))
                .andExpect(jsonPath("$.model", is("Acer Aspire Go 14")));
    }

    @Test
    void shouldReturnDashboardStats() throws Exception {
        mockMvc.perform(get("/api/dashboard/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAssets", greaterThan(0)))
                .andExpect(jsonPath("$.computeUnits", greaterThan(0)))
                .andExpect(jsonPath("$.antivirusComplianceRate", notNullValue()));
    }

    @Test
    void shouldCreateNewAsset() throws Exception {
        AssetRequestDTO newAsset = AssetRequestDTO.builder()
                .assetTag("NEX-LP-TEST99")
                .serialNumber("TEST-SN-99999999")
                .model("Acer Aspire Go 14")
                .deviceType(DeviceType.LAPTOP)
                .status(AssetStatus.AVAILABLE)
                .campusLocation(CampusLocation.PADUKKA_MAIN)
                .department("IT Test")
                .processor("Intel Core i3")
                .ram("8GB")
                .storage("512GB")
                .build();

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAsset)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.assetTag", is("NEX-LP-TEST99")))
                .andExpect(jsonPath("$.serialNumber", is("TEST-SN-99999999")));
    }
}
