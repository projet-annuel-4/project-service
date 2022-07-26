package fr.esgi.projectservice.data.repository;

import fr.esgi.projectservice.data.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<BranchEntity, Long> {

    BranchEntity getAllByProjectEntity_Id(Long projectId);
}
