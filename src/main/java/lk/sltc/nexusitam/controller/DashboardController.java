package lk.sltc.nexusitam.controller;

import lk.sltc.nexusitam.dto.DashboardStatsDTO;
import lk.sltc.nexusitam.dto.HandoverResponseDTO;
import lk.sltc.nexusitam.service.DashboardService;
import lk.sltc.nexusitam.service.HandoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping({"/api/dashboard", "/dashboard", "/"})
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DashboardController {

    private final DashboardService dashboardService;
    private final HandoverService handoverService;

    /**
     * Browser endpoint: renders the executive dashboard Thymeleaf template.
     */
    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    public String viewDashboard(Model model) {
        DashboardStatsDTO stats = dashboardService.getDashboardStats();
        List<HandoverResponseDTO> recentHandovers = handoverService.getRecentHandoverLogs();

        model.addAttribute("stats", stats);
        model.addAttribute("recentHandovers", recentHandovers);
        model.addAttribute("activeTab", "dashboard");
        return "dashboard";
    }

    /**
     * REST endpoint: returns telemetry stats as JSON.
     */
    @ResponseBody
    @GetMapping(value = {"/stats", ""}, produces = {MediaType.APPLICATION_JSON_VALUE, "*/*"})
    public ResponseEntity<DashboardStatsDTO> getStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }
}
