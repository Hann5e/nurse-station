package com.example.nurse_station.controller;

import com.example.nurse_station.model.Nurse;
import com.example.nurse_station.repository.NurseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nurses")
public class NurseApiController {

    @Autowired
    private NurseRepository nurseRepository;

    // 建立護士
    @PostMapping
    public Nurse createNurse(@RequestBody Nurse nurse) {
        return nurseRepository.save(nurse);
    }

    // 取得全部護士
    @GetMapping
    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    // 修改護士資料
    @PutMapping("/{id}")
    public Nurse updateNurse(@PathVariable Long id, @RequestBody Nurse updatedNurse) {
        Optional<Nurse> optional = nurseRepository.findById(id);
        if (optional.isPresent()) {
            Nurse nurse = optional.get();
            nurse.setName(updatedNurse.getName());
            nurse.setExperience(updatedNurse.getExperience());
            nurse.setEmployeeId(updatedNurse.getEmployeeId());
            return nurseRepository.save(nurse);
        } else {
            throw new RuntimeException("護士不存在");
        }
    }

    // 刪除護士
    @DeleteMapping("/{id}")
    public void deleteNurse(@PathVariable Long id) {
        nurseRepository.deleteById(id);
    }
}
