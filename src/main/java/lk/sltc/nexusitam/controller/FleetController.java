package lk.sltc.nexusitam.controller;

import lk.sltc.nexusitam.dto.AssetResponseDTO;
import lk.sltc.nexusitam.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping({"/api/fleet", "/fleet"})
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class FleetController {

    private final AssetService assetService;

    // =========================================================================
    // 1. Browser HTML Template View
    // =========================================================================

    /**
     * Browser endpoint: renders the Acer Aspire Go 14 Fleet Tracker Thymeleaf template.
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String viewFleetHtml(Model model) {
        List<AssetResponseDTO> laptops = assetService.getLaptopFleet();

        long available = laptops.stream().filter(l -> l.getStatus() == lk.sltc.nexusitam.model.enums.AssetStatus.AVAILABLE).count();
        long transferred = laptops.stream().filter(l -> l.getStatus() == lk.sltc.nexusitam.model.enums.AssetStatus.TRANSFER).count();
        long faulty = laptops.stream().filter(l -> l.getStatus() == lk.sltc.nexusitam.model.enums.AssetStatus.NOT_WORKING).count();
        long quarantined = laptops.stream().filter(l -> Boolean.TRUE.equals(l.getIsExamQuarantine())).count();
        long withBag = laptops.stream().filter(l -> Boolean.TRUE.equals(l.getHasBag())).count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("model", "Acer Aspire Go 14 Fleet (Series BAIT-100)");
        summary.put("totalUnits", laptops.size());
        summary.put("availableUnits", available);
        summary.put("transferredUnits", transferred);
        summary.put("faultyUnits", faulty);
        summary.put("quarantinedForExam", quarantined);
        summary.put("unitsWithBag", withBag);

        model.addAttribute("laptops", laptops);
        model.addAttribute("summary", summary);
        model.addAttribute("activeTab", "fleet");
        return "fleet";
    }

    /**
     * Web Form: Toggle quarantine and redirect with flash message.
     */
    @PostMapping(value = "/laptops/{id}/quarantine", headers = "content-type!=application/json")
    public String toggleExamQuarantineForm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            AssetResponseDTO res = assetService.toggleExamQuarantine(id);
            String statusDesc = Boolean.TRUE.equals(res.getIsExamQuarantine()) ? "locked for exam pool" : "released to available pool";
            redirectAttributes.addFlashAttribute("successMessage", "Unit " + res.getAssetTag() + " " + statusDesc + ".");
        } catch (Exception ex) {
            log.error("Failed to toggle quarantine on unit ID {}: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Operation failed: " + ex.getMessage());
        }
        return "redirect:/fleet";
    }

    // =========================================================================
    // 2. REST API Endpoints (JSON)
    // =========================================================================

    @ResponseBody
    @GetMapping(value = {"", "/laptops"}, produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<List<AssetResponseDTO>> getLaptopFleet() {
        return ResponseEntity.ok(assetService.getLaptopFleet());
    }

    @ResponseBody
    @PostMapping(value = "/laptops/{id}/quarantine", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AssetResponseDTO> toggleExamQuarantine(@PathVariable Long id) {
        return ResponseEntity.ok(assetService.toggleExamQuarantine(id));
    }

    @ResponseBody
    @GetMapping(value = "/summary", produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<Map<String, Object>> getFleetSummary() {
        List<AssetResponseDTO> laptops = assetService.getLaptopFleet();

        long available = laptops.stream().filter(l -> l.getStatus() == lk.sltc.nexusitam.model.enums.AssetStatus.AVAILABLE).count();
        long transferred = laptops.stream().filter(l -> l.getStatus() == lk.sltc.nexusitam.model.enums.AssetStatus.TRANSFER).count();
        long faulty = laptops.stream().filter(l -> l.getStatus() == lk.sltc.nexusitam.model.enums.AssetStatus.NOT_WORKING).count();
        long quarantined = laptops.stream().filter(l -> Boolean.TRUE.equals(l.getIsExamQuarantine())).count();
        long withBag = laptops.stream().filter(l -> Boolean.TRUE.equals(l.getHasBag())).count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("model", "Acer Aspire Go 14 Fleet (Series BAIT-100)");
        summary.put("totalUnits", laptops.size());
        summary.put("availableUnits", available);
        summary.put("transferredUnits", transferred);
        summary.put("faultyUnits", faulty);
        summary.put("quarantinedForExam", quarantined);
        summary.put("unitsWithBag", withBag);

        return ResponseEntity.ok(summary);
    }
}
