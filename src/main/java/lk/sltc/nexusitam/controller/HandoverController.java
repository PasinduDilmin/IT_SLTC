package lk.sltc.nexusitam.controller;

import jakarta.validation.Valid;
import lk.sltc.nexusitam.dto.AssetResponseDTO;
import lk.sltc.nexusitam.dto.HandoverRequestDTO;
import lk.sltc.nexusitam.dto.HandoverResponseDTO;
import lk.sltc.nexusitam.model.enums.AssetStatus;
import lk.sltc.nexusitam.model.enums.CampusLocation;
import lk.sltc.nexusitam.model.enums.HandoverStatus;
import lk.sltc.nexusitam.service.AssetService;
import lk.sltc.nexusitam.service.HandoverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping({"/api/handovers", "/handovers"})
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class HandoverController {

    private final HandoverService handoverService;
    private final AssetService assetService;

    // =========================================================================
    // 1. Browser HTML Template View & Form Submissions
    // =========================================================================

    /**
     * Browser endpoint: renders the Custody Voucher Ledger Thymeleaf template.
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String viewHandoversHtml(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<HandoverResponseDTO> handoverPage = handoverService.getHandoverLogs(PageRequest.of(page, size));
        List<AssetResponseDTO> availableAssets = assetService.getAssets(null, AssetStatus.AVAILABLE, null, null, null, null, PageRequest.of(0, 100)).getContent();

        long activeCount = handoverPage.getContent().stream().filter(h -> h.getStatus() == HandoverStatus.ACTIVE).count();
        long returnedCount = handoverPage.getContent().stream().filter(h -> h.getStatus() == HandoverStatus.RETURNED).count();

        model.addAttribute("handovers", handoverPage);
        model.addAttribute("availableAssets", availableAssets);
        model.addAttribute("campuses", CampusLocation.values());
        model.addAttribute("activeCount", activeCount);
        model.addAttribute("returnedCount", returnedCount);
        model.addAttribute("activeTab", "handovers");
        return "handovers";
    }

    /**
     * Web Form: Issue custody voucher and redirect with flash message.
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String initiateHandoverForm(
            @ModelAttribute HandoverRequestDTO request,
            RedirectAttributes redirectAttributes) {
        try {
            HandoverResponseDTO response = handoverService.initiateHandover(request);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Custody voucher issued for " + response.getCustodianName() + " (Token: " + response.getDigitalSignToken() + ")");
        } catch (Exception ex) {
            log.error("Failed to initiate handover via form: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Handover issuance failed: " + ex.getMessage());
        }
        return "redirect:/handovers";
    }

    /**
     * Web Form: Return asset check-in and redirect with flash message.
     */
    @PostMapping("/{id}/return")
    public String returnAssetForm(
            @PathVariable Long id,
            @RequestParam(required = false) String notes,
            RedirectAttributes redirectAttributes) {
        try {
            HandoverResponseDTO response = handoverService.returnAsset(id, notes);
            redirectAttributes.addFlashAttribute("successMessage", 
                    "Asset " + response.getAssetTag() + " returned and vault check-in confirmed.");
        } catch (Exception ex) {
            log.error("Failed to return asset on voucher ID {}: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Return check-in failed: " + ex.getMessage());
        }
        return "redirect:/handovers";
    }

    // =========================================================================
    // 2. REST API Endpoints (JSON)
    // =========================================================================

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HandoverResponseDTO> initiateHandover(@Valid @RequestBody HandoverRequestDTO request) {
        HandoverResponseDTO response = handoverService.initiateHandover(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @ResponseBody
    @PutMapping("/{id}/return")
    public ResponseEntity<HandoverResponseDTO> returnAsset(
            @PathVariable Long id,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(handoverService.returnAsset(id, notes));
    }

    @ResponseBody
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<Page<HandoverResponseDTO>> getHandoverLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(handoverService.getHandoverLogs(PageRequest.of(page, size)));
    }

    @ResponseBody
    @GetMapping("/recent")
    public ResponseEntity<List<HandoverResponseDTO>> getRecentLogs() {
        return ResponseEntity.ok(handoverService.getRecentHandoverLogs());
    }

    @ResponseBody
    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<HandoverResponseDTO>> getLogsByAsset(@PathVariable Long assetId) {
        return ResponseEntity.ok(handoverService.getLogsByAssetId(assetId));
    }
}
