package fr.esgi.projectservice.domain.mapper;

import fr.esgi.projectservice.data.entity.BranchEntity;
import fr.esgi.projectservice.domain.model.Branch;
import fr.esgi.projectservice.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BranchDomainMapper {

    private final ModelMapper modelMapper;
    private final ProjectDomainMapper projectDomainMapper;

    public BranchDomainMapper(ModelMapper modelMapper, ProjectDomainMapper projectDomainMapper) {
        this.modelMapper = modelMapper;
        this.projectDomainMapper = projectDomainMapper;
    }

    public Branch convertCreateRequestToModel(CreateProjectRequest request) {
        return modelMapper.map(request, Branch.class);
    }

    public Branch convertEntityToModel(BranchEntity branchEntity) {
        if (branchEntity == null) {
            return null;
        }
        Branch branch = new Branch();
        branch.setId(branchEntity.getId());
        branch.setName(branchEntity.getName());
        branch.setCreationDate(branchEntity.getCreationDate());
        branch.setProject(projectDomainMapper.convertEntityToModel(branchEntity.getProjectEntity()));
        return branch;
    }

    public BranchEntity convertModelToEntity(Branch branch) {
        if (branch == null) {
            return null;
        }
        BranchEntity branchEntity = new BranchEntity();
        branchEntity.setId(branch.getId() == null ? null : branch.getId());
        branchEntity.setProjectEntity(projectDomainMapper.convertModelToEntity(branch.getProject()));
        branchEntity.setName(branch.getName());
        branchEntity.setCreationDate(branch.getCreationDate());
        return branchEntity;
    }
}
