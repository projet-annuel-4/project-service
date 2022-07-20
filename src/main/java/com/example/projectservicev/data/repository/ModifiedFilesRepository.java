package com.example.projectservicev.data.repository;

import com.example.projectservicev.data.entity.BranchEntity;
import com.example.projectservicev.data.entity.FileEntity;
import com.example.projectservicev.data.entity.ModifiedFileEntity;
import com.example.projectservicev.domain.model.ModifiedFileTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModifiedFilesRepository extends JpaRepository<ModifiedFileEntity, Long> {

    void deleteByBranchEntityAndFileEntity(BranchEntity branchEntity, FileEntity fileEntity);

    List<ModifiedFileEntity> getAllByBranchEntity(BranchEntity branchEntity);

    List<ModifiedFileEntity> getAllByBranchEntityAndModificationType(BranchEntity branchEntity, ModifiedFileTypeEnum type);

    List<ModifiedFileEntity> getAllByCommitEntityIsNull();

    List<ModifiedFileEntity> getAllByCommitEntity_Id(Long commitId);

    ModifiedFileEntity getByFileEntity_IdAndAndCommitEntityIsNull(Long fileId);
}
