package pl.edu.pwr.pkuchnowski.doryw.services;

import pl.edu.pwr.pkuchnowski.doryw.entities.JobCategoryEntity;

import java.util.List;

public interface JobCategoryService {
    JobCategoryEntity getJobCategoryEntityByName(String name);
    List<JobCategoryEntity> getAllJobCategories();
}
