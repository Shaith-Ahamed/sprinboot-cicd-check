package ma.projet.gestionprofesseurs.controllers;


import ma.projet.gestionprofesseurs.entities.Professeur;
import ma.projet.gestionprofesseurs.entities.Specialite;
import ma.projet.gestionprofesseurs.repository.SpecialiteRepository;
import ma.projet.gestionprofesseurs.services.ProfesseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/professeur")
public class ProfesseurController {
    @Autowired
    private ProfesseurService professeurService;
    @Autowired
    private SpecialiteRepository specialiteRepository;

    @GetMapping
    public List<Professeur> findAllProfesseur() {
        return professeurService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Professeur professeur = professeurService.findById(id);
        if (professeur == null) {
            return new ResponseEntity<Object>("Professeur with ID " + id + " not found", HttpStatus.BAD_REQUEST);
        } else {
            return ResponseEntity.ok(professeur);
        }
    }

    @PostMapping
    public Professeur createProfesseur(@RequestBody Professeur professeur) {
        professeur.setId(0);
        return professeurService.create(professeur);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProfesseur(@PathVariable int id, @RequestBody Professeur professeur) {

        if (professeurService.findById(id) == null) {
            return new ResponseEntity<Object>("Professeur with ID " + id + " not found", HttpStatus.BAD_REQUEST);
        } else {
            professeur.setId(id);
            return ResponseEntity.ok(professeurService.update(professeur));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProfesseur(@PathVariable int id) {
        Professeur professeur = professeurService.findById(id);
        if (professeur == null) {
            return new ResponseEntity<Object>("Professeur with ID " + id + " not found", HttpStatus.BAD_REQUEST);
        } else {
            professeurService.delete(professeur);
            return ResponseEntity.ok("Professeur has been deleted");
        }
    }

    @GetMapping("/specialite/{id}")
    public List<Professeur> findProfesseurBySpecialite(@PathVariable Integer id) {
        Specialite specialite = new Specialite();
        specialite.setId(id);
        return professeurService.findBySpecialite(specialite);
    }

    @GetMapping("/filterByDate")
    public List<Professeur> findByDateEmbaucheBetween(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateDebut, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateFin) {
        return professeurService.findByDateEmbaucheBetween(dateDebut, dateFin);
    }
}
