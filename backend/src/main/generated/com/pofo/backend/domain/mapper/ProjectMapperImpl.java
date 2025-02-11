package com.pofo.backend.domain.mapper;

import com.pofo.backend.domain.project.dto.response.ProjectDetailResponse;
import com.pofo.backend.domain.project.entity.Project;
import java.time.LocalDate;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-10T11:45:25+0900",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 21.0.2 (GraalVM Community)"

)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectDetailResponse projectToProjectDetailResponse(Project project) {
        if ( project == null ) {
            return null;
        }

        Long projectId = null;
        String name = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        int memberCount = 0;
        String position = null;
        String repositoryLink = null;
        String description = null;
        String imageUrl = null;

        projectId = project.getId();
        name = project.getName();
        startDate = project.getStartDate();
        endDate = project.getEndDate();
        memberCount = project.getMemberCount();
        position = project.getPosition();
        repositoryLink = project.getRepositoryLink();
        description = project.getDescription();
        imageUrl = project.getImageUrl();

        List<String> skillNames = project.getProjectSkills().stream().map(ps -> ps.getSkill().getName()).collect(java.util.stream.Collectors.toList());
        List<String> toolNames = project.getProjectTools().stream().map(pt -> pt.getTool().getName()).collect(java.util.stream.Collectors.toList());

        ProjectDetailResponse projectDetailResponse = new ProjectDetailResponse( projectId, name, startDate, endDate, memberCount, position, repositoryLink, description, imageUrl, skillNames, toolNames );

        return projectDetailResponse;
    }
}
