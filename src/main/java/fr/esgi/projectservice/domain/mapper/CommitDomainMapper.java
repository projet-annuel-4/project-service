package fr.esgi.projectservice.domain.mapper;

import fr.esgi.projectservice.data.entity.CommitEntity;
import fr.esgi.projectservice.domain.model.Commit;
import fr.esgi.projectservice.request.CreateProjectRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component

public class CommitDomainMapper {

    private final ModelMapper modelMapper;
    private final BranchDomainMapper branchDomainMapper;

    @Autowired
    public CommitDomainMapper(ModelMapper modelMapper, BranchDomainMapper branchDomainMapper) {
        this.modelMapper = modelMapper;
        this.branchDomainMapper = branchDomainMapper;
    }

    public Commit convertCreateRequestToModel(CreateProjectRequest request) {
        return modelMapper.map(request, Commit.class);
    }

    public List<Commit> convertListEntitiesToListModels(List<CommitEntity> commitEntities){
        List<Commit> commits = new ArrayList<>();
        for (CommitEntity commitEntity : commitEntities){
            commits.add(convertEntityToModel(commitEntity,0));
        }
        return commits;
    }

    public Commit convertEntityToModel(CommitEntity commitEntity, int loop) {
        if (loop == 1) {
            return null;
        }
        if (commitEntity == null) {
            return null;
        }
        Commit commit = new Commit();
        commit.setId(commitEntity.getId());
        commit.setCreationDate(new Date());
        commit.setBranch(branchDomainMapper.convertEntityToModel(commitEntity.getBranch()));
        commit.setName(commitEntity.getName());
        commit.setChild(commitEntity.getChild() == null ? null : convertEntityToModel(commitEntity.getChild(), loop + 1));
        commit.setParent(commitEntity.getParent() == null ? null : convertEntityToModel(commitEntity.getParent(), loop + 1));

        return commit;
    }

    public CommitEntity convertModelToEntity(Commit commit, int loop) {
        if (loop == 1) {
            return null;
        }
        if (commit == null) {
            return null;
        }
        CommitEntity commitEntity = new CommitEntity();
        commitEntity.setId(commit.getId() == null ? null : commit.getId());
        commitEntity.setCreationDate(new Date());
        commitEntity.setBranch(branchDomainMapper.convertModelToEntity(commit.getBranch()));
        commitEntity.setName(commit.getName());
        commitEntity.setChild(commit.getChild() == null ? null : convertModelToEntity(commit.getChild(), loop + 1));
        commitEntity.setParent(commit.getParent() == null ? null : convertModelToEntity(commit.getParent(), loop + 1));

        return commitEntity;
    }

}
