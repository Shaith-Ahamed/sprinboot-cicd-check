package ma.projet.gestionprofesseurs.controllers;

import ma.projet.gestionprofesseurs.entities.Specialite;
import ma.projet.gestionprofesseurs.services.SpecialiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialite")
public class SpecialiteController {
    @Autowired
    private SpecialiteService specialiteService;

    @GetMapping
    public List<Specialite> findAllSpecialite() {
        return specialiteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Specialite specialite = specialiteService.findById(id);
        if (specialite == null) {
            return new ResponseEntity<Object>("Specialite with ID " + id + " not found", HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(specialite);
        }
    }

    @PostMapping
    public Specialite createSpecialite(@RequestBody Specialite specialite) {
        specialite.setId(0);
        return specialiteService.create(specialite);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSpecialite(@PathVariable int id, @RequestBody Specialite specialite) {

        if (specialiteService.findById(id) == null) {
            return new ResponseEntity<Object>("Specialite with ID " + id + " not found", HttpStatus.BAD_REQUEST);
        } else {
            specialite.setId(id);
            return ResponseEntity.ok(specialiteService.update(specialite));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSpecialite(@PathVariable int id) {
        Specialite specialite = specialiteService.findById(id);
        if (specialite == null) {
            return new ResponseEntity<Object>("Specialite with ID " + id + " not found", HttpStatus.BAD_REQUEST);
        } else {
            specialiteService.delete(specialite);
            return ResponseEntity.ok("Specialite has been deleted");
        }
    }
}