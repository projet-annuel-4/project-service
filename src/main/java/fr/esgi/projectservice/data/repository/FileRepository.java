package fr.esgi.projectservice.data.repository;

import fr.esgi.projectservice.data.entity.CommitEntity;
import fr.esgi.projectservice.data.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    List<FileEntity> findAllByBranchEntityId(Long idBranch);

    List<FileEntity> findAllByCommitEntityCreation(CommitEntity commitEntity);

    FileEntity findByIdAndBranchEntity_Id(Long id, Long branchId);

    Optional<FileEntity> findByBranchEntity_IdAndNameEquals(Long branchId, String fileName);


}
