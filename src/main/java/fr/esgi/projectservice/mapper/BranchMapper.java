package fr.esgi.projectservice.mapper;

import fr.esgi.projectservice.service.BranchService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@Component
public class BranchMapper {

    private final ModelMapper modelMapper;
    private final BranchService branchService;

    @Autowired
    public BranchMapper(ModelMapper modelMapper, BranchService branchService) {
        this.modelMapper = modelMapper;
        this.branchService = branchService;
    }

    public void createBranch(Long id) {
        branchService.createBranch(id);
    }

    public Long getBranchIdByProjectId(Long projectId){
        return branchService.getBranchIdByProjectId(projectId);
    }

    public void initBranch(Long idBranch, MultipartFile[] files) throws IOException, URISyntaxException, InterruptedException {

        branchService.initBranch(idBranch, files);

    }
}
