package com.lexisware.portafolio.dashboard.controllers;

import com.lexisware.portafolio.dashboard.dtos.DashboardStats;
import com.lexisware.portafolio.dashboard.dtos.UserProjectCount;
import com.lexisware.portafolio.dashboard.dtos.UserGrowthStats;
import com.lexisware.portafolio.dashboard.dtos.AdvisoryStatsDto;
import com.lexisware.portafolio.users.repositories.UserRepository;
import com.lexisware.portafolio.project.repositories.ProjectRepository;
import com.lexisware.portafolio.advisory.repositories.AdvisoryRepository;
import com.lexisware.portafolio.advisory.entities.AdvisoryEntity;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

        private final UserRepository userRepository;
        private final ProjectRepository projectRepository;
        private final AdvisoryRepository advisoryRepository;

        public DashboardController(UserRepository userRepository, ProjectRepository projectRepository,
                        AdvisoryRepository advisoryRepository) {
                this.userRepository = userRepository;
                this.projectRepository = projectRepository;
                this.advisoryRepository = advisoryRepository;
        }

        @GetMapping("/stats")
        public ResponseEntity<DashboardStats> getStats() {
                long programmers = userRepository.count();
                long projects = projectRepository.count();

                long pending = advisoryRepository.countByStatus(AdvisoryEntity.Status.pending);
                long approved = advisoryRepository.countByStatus(AdvisoryEntity.Status.approved);
                long rejected = advisoryRepository.countByStatus(AdvisoryEntity.Status.rejected);

                return ResponseEntity.ok(DashboardStats.builder()
                                .programmersCount(programmers)
                                .projectsCount(projects)
                                .advisoriesPending(pending)
                                .advisoriesApproved(approved)
                                .advisoriesRejected(rejected)
                                .build());
        }

        @GetMapping("/projects-by-user")
        public ResponseEntity<List<UserProjectCount>> getProjectStatsByUser() {
                return ResponseEntity.ok(projectRepository.countProjectsByUser());
        }

        @GetMapping("/user-growth")
        public ResponseEntity<List<UserGrowthStats>> getUserGrowth() {
                return ResponseEntity.ok(userRepository.countUsersByGrowth());
        }

        @GetMapping("/advisories-history")
        public ResponseEntity<List<AdvisoryStatsDto>> getAdvisoryHistory() {
                return ResponseEntity.ok(advisoryRepository.countAdvisoriesByMonth());
        }

        @GetMapping("/advisories-by-programmer")
        public ResponseEntity<List<AdvisoryStatsDto>> getAdvisoriesByProgrammer() {
                return ResponseEntity.ok(advisoryRepository.countAdvisoriesByProgrammer());
        }
}
