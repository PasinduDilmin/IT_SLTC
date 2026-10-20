package lk.sltc.nexusitam.controller;

import lk.sltc.nexusitam.dto.LabStationDTO;
import lk.sltc.nexusitam.service.LabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/api/labs", "/labs"})
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LabController {

    private final LabService labService;

    // =========================================================================
    // 1. Browser HTML Template View
    // =========================================================================

    /**
     * Browser endpoint: renders the Computer Laboratory 01 Physical Matrix Thymeleaf template.
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String viewLabHtml(
            @RequestParam(required = false, defaultValue = "all") String row,
            Model model) {

        List<LabStationDTO> stations = labService.getLabWorkstations(row);
        Map<String, List<LabStationDTO>> matrix = labService.getLabMatrix();

        Map<String, Object> stats = buildLabStats();

        model.addAttribute("stations", stations);
        model.addAttribute("matrix", matrix);
        model.addAttribute("stats", stats);
        model.addAttribute("selectedRow", row);
        model.addAttribute("activeTab", "labs");
        return "labs";
    }

    // =========================================================================
    // 2. REST API Endpoints (JSON)
    // =========================================================================

    @ResponseBody
    @GetMapping(value = {"", "/computer-lab-01/stations"}, produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<List<LabStationDTO>> getLabStations(
            @RequestParam(required = false, defaultValue = "all") String row) {
        return ResponseEntity.ok(labService.getLabWorkstations(row));
    }

    @ResponseBody
    @GetMapping(value = "/computer-lab-01/matrix", produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<Map<String, List<LabStationDTO>>> getLabMatrix() {
        return ResponseEntity.ok(labService.getLabMatrix());
    }

    @ResponseBody
    @GetMapping(value = "/computer-lab-01/stats", produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<Map<String, Object>> getLabStats() {
        return ResponseEntity.ok(buildLabStats());
    }

    private Map<String, Object> buildLabStats() {
        List<LabStationDTO> all = labService.getLabWorkstations("all");

        Map<String, Object> stats = new HashMap<>();
        stats.put("labName", "Computer Laboratory 01 & DAIT Hardware Fleet");
        stats.put("campus", "Padukka Main Campus (Bldg B • Room L1-04)");
        stats.put("totalStations", all.size());
        stats.put("workstationModel", "Acer Veriton S2720G");
        stats.put("cpuSpecs", "Intel Core i7-14700 • 16GB DDR5 • 512GB NVMe");
        stats.put("osEnvironment", "Windows 11 Pro 23H2 (x64) Domain Active");
        stats.put("displaySpecs", "Acer 27\" Full HD LED Monitor + Acer USB Keyboard & Optical Mouse");
        stats.put("uplink", "Aruba WAC 303: CONNECTED | Uplink: 1.0 Gbps (VLAN 140)");
        stats.put("healthRate", "100% (0 Faults / 0 Offline)");
        return stats;
    }
}
