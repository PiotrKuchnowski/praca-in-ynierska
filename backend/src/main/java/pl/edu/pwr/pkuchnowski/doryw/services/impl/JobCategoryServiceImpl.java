package pl.edu.pwr.pkuchnowski.doryw.services.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.pwr.pkuchnowski.doryw.entities.JobCategoryEntity;
import pl.edu.pwr.pkuchnowski.doryw.repositories.JobCategoryRepository;
import pl.edu.pwr.pkuchnowski.doryw.services.JobCategoryService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class JobCategoryServiceImpl implements JobCategoryService {

    private final JobCategoryRepository jobCategoryRepository;

    @Override
    public JobCategoryEntity getJobCategoryEntityByName(String name) {
        Optional<JobCategoryEntity> jobCategoryEntity = jobCategoryRepository.findByName(name);
        if (jobCategoryEntity.isEmpty()) {
            JobCategoryEntity newCategory = new JobCategoryEntity();
            newCategory.setName(name);
            newCategory = jobCategoryRepository.save(newCategory);
            return newCategory;
        }
        return jobCategoryEntity.get();
    }

    @Override
    public List<JobCategoryEntity> getAllJobCategories() {
        return jobCategoryRepository.findAll();
    }
}
