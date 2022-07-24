package fr.esgi.projectservice.data.repository;

import fr.esgi.projectservice.data.entity.BranchEntity;
import fr.esgi.projectservice.data.entity.FileEntity;
import fr.esgi.projectservice.data.entity.ModifiedFileEntity;
import fr.esgi.projectservice.domain.model.ModifiedFileTypeEnum;
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

    List<ModifiedFileEntity> getAllByFileEntity_IdAndCommitEntityNotNull(Long fileId);
}
