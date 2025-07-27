package com.example.nurse_station.controller;

import com.example.nurse_station.model.Nurse;
import com.example.nurse_station.model.Station;
import com.example.nurse_station.repository.NurseRepository;
import com.example.nurse_station.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class StationViewController {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private NurseRepository nurseRepository;

    // 顯示所有站點
    @GetMapping("/stations")
    public String listStations(Model model) {
        model.addAttribute("stations", stationRepository.findAll());
        return "stations";
    }

    // 顯示新增站點表單（包含所有護士）
    @GetMapping("/stations/new")
    public String showStationForm(Model model) {
        model.addAttribute("station", new Station());
        model.addAttribute("nurses", nurseRepository.findAll());
        return "station_form";
    }

    // 顯示編輯站點表單（包含選取的護士）
    @GetMapping("/stations/edit/{id}")
    public String editStation(@PathVariable Long id, Model model) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("找不到站點"));
        model.addAttribute("station", station);
        model.addAttribute("nurses", nurseRepository.findAll());
        return "station_form";
    }

    // 儲存站點（新增或編輯）
    @PostMapping("/stations/save")
    public String saveStation(
            @ModelAttribute Station station,
            @RequestParam(value = "nurseIds", required = false) List<Long> nurseIds) {

        if (nurseIds != null) {
            Set<Nurse> nurses = new HashSet<>(nurseRepository.findAllById(nurseIds));
            station.setNurses(nurses);
        } else {
            station.setNurses(new HashSet<>());
        }

        stationRepository.save(station);
        return "redirect:/stations";
    }

    // 刪除站點
    @GetMapping("/stations/delete/{id}")
    public String deleteStation(@PathVariable Long id) {
        stationRepository.deleteById(id);
        return "redirect:/stations";
    }
}