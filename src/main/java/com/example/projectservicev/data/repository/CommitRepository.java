package com.example.projectservicev.data.repository;

import com.example.projectservicev.data.entity.BranchEntity;
import com.example.projectservicev.data.entity.CommitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommitRepository extends JpaRepository<CommitEntity, Long> {

    Optional<CommitEntity> findByBranchEntityAndChildIsNull(BranchEntity branchEntity);

    List<CommitEntity> findAllByBranchEntityId(Long idBranch);

    CommitEntity findByChild_Id(Long childId);


}
