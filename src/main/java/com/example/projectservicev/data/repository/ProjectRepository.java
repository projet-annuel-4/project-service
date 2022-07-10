package com.example.projectservicev.data.repository;

import com.example.projectservicev.data.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    ProjectEntity getById(Long id);

    ProjectEntity findProjectEntitiesById(Long id);

    @Query("select r from ProjectEntity r join fetch r.groupEntity where r.id = ?1")
    Optional<ProjectEntity> findByIdWithGroup(Long id);
}
