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
public class NurseViewController {

    @Autowired
    private NurseRepository nurseRepository;

    @Autowired
    private StationRepository stationRepository;

    // 顯示護士列表和新增表單
    @GetMapping("/nurses")
    public String listNurses(Model model) {
        List<Nurse> nurse = nurseRepository.findAll();
        model.addAttribute("nurses", nurseRepository.findAll());
        model.addAttribute("stations", stationRepository.findAll());
        model.addAttribute("nurse", new Nurse());
        model.addAttribute("nurseCount", nurse.size());
           return "nurses";
    }

    // 按下「修改」後載入該護士資料
    @GetMapping("/nurses/edit/{id}")
    public String editNurse(@PathVariable Long id, Model model) {
        Nurse nurse = nurseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid nurse ID: " + id));
        model.addAttribute("nurses", nurseRepository.findAll()); // 一樣要顯示所有
        model.addAttribute("stations", stationRepository.findAll());
        model.addAttribute("nurse", nurse); // 帶入要編輯的對象
        return "nurses";
    }

    // 儲存（新增或更新）
    @PostMapping("/nurses/save")
    public String saveOrUpdateNurse(
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam("name") String name,
            @RequestParam("employeeId") String employeeId,
            @RequestParam("experience") Integer experience,
            @RequestParam("stationIds") List<Long> stationIds
    ) {
        Nurse nurse = (id != null) ?
                nurseRepository.findById(id).orElse(new Nurse()) :
                new Nurse();

        nurse.setName(name);
        nurse.setEmployeeId(employeeId);
        nurse.setExperience(experience);

        Set<Station> stations = new HashSet<>(stationRepository.findAllById(stationIds));
        nurse.setStations(stations);

        nurseRepository.save(nurse);
        return "redirect:/nurses";
    }

    // 刪除
    @GetMapping("/nurses/delete/{id}")
    public String deleteNurse(@PathVariable Long id) {
        nurseRepository.deleteById(id);
        return "redirect:/nurses";
    }
}
