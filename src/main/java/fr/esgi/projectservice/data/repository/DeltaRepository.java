package fr.esgi.projectservice.data.repository;

import fr.esgi.projectservice.data.entity.CommitEntity;
import fr.esgi.projectservice.data.entity.DeltaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeltaRepository extends JpaRepository<DeltaEntity, Long> {


    List<DeltaEntity> findAllByFormCommitEntity(CommitEntity commitEntity);

    void deleteAllByFormCommitEntity(CommitEntity commitEntity);

    DeltaEntity getByFileSrc_IdAndAndFormCommitEntity_Id(Long fileId, Long commitId);

}
