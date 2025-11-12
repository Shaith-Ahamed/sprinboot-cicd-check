package ma.projet.gestionprofesseurs.repository;

import ma.projet.gestionprofesseurs.entities.Professeur;
import ma.projet.gestionprofesseurs.entities.Specialite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProfesseurRepository extends JpaRepository<Professeur, Integer> {

   // @Query("select p from Professeur p where p.specialite = ?1")
    //List<Professeur> findBySpecialite(Specialite specialite);

    //@Query("SELECT p FROM Professeur p WHERE p.dateEmbauche BETWEEN :dateDebut AND :dateFin")
    //List<Professeur> findByDateEmbaucheBetween(@Param("dateDebut") Date dateDebut, @Param("dateFin") Date dateFin);

    List<Professeur> findBySpecialite(Specialite specialite);
    List<Professeur> findByDateEmbaucheBetween(Date dateDebut, Date dateFin);


}
