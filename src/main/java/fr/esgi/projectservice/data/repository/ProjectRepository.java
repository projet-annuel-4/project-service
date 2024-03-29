package fr.esgi.projectservice.data.repository;

import fr.esgi.projectservice.data.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    ProjectEntity getById(Long id);

    ProjectEntity findProjectEntitiesById(Long id);

    List<ProjectEntity> getAllByGroupEntity_IdAndDeletedFalse(Long groupId);

    @Query("select r from ProjectEntity r join fetch r.groupEntity where r.id = ?1")
    Optional<ProjectEntity> findByIdWithGroup(Long id);
}
