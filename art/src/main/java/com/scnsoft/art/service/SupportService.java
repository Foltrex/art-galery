package com.scnsoft.art.service;

import com.scnsoft.art.contoller.SupportThreadFilter;
import com.scnsoft.art.dto.SupportFilter;
import com.scnsoft.art.entity.Support;
import com.scnsoft.art.entity.SupportThread;
import com.scnsoft.art.repository.SupportRepository;
import com.scnsoft.art.repository.SupportThreadRepository;
import com.scnsoft.art.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SupportService {

    private final SupportThreadRepository supportThreadRepository;
    private final SupportRepository supportRepository;
    private final AccountService accountService;

    public Support save(Support support) {
        return supportRepository.save(support);
    }

    public Optional<SupportThread> findThread(UUID threadId) {
        return supportThreadRepository.findById(threadId);
    }

    public List<Support> findAllPosts(SupportFilter filter, Sort sort) {
        Specification<Support> specification = (root, cq, cb) ->
                cb.equal(root.get(Support.Fields.threadId), filter.getThreadId());
        return supportRepository.findAll(specification, sort);
    }

    public Page<SupportThread> findThreads(SupportThreadFilter filter, Pageable pageable) {
        Specification<SupportThread> specification = (root, cq, cb) -> cb.conjunction();

        if(filter.getAccountId() != null) {
            specification = specification.and((r, cq, cb) ->
                    cb.equal(r.get(SupportThread.Fields.accountId), filter.getAccountId()));
        }
        if(filter.getStatus() != null) {
            specification = specification.and((r, cq, cb) ->
                    cb.equal(r.get(SupportThread.Fields.status), filter.getStatus()));
        }
        return supportThreadRepository.findAll(specification, pageable);
    }

    public SupportThread saveThread(SupportThread thread) {
        return supportThreadRepository.save(thread);
    }

    public Support createPost(Support thread) {
        return supportRepository.save(thread);
    }

    public void deleteThread(SupportThread thread) {
        supportThreadRepository.delete(thread);
    }

    public void deletePost(Support post) {
        supportRepository.delete(post);
    }

    public Map<UUID, List<Support>> findPosts(List<UUID> idList) {
        if(idList == null || idList.isEmpty()) {
            return new HashMap<>();
        }

        var data = supportRepository.findPosts(idList);
        Map<UUID, List<Support>> out = new HashMap<>();
        for(Support s : data) {
            List<Support> list = out.computeIfAbsent(s.getThreadId(), k -> new ArrayList<>());
            list.add(s);
        }
        return out;
    }

    public Integer getUnanswered(UUID id) {
        boolean isSystemUser = accountService.isSystemUser();
        var open = isSystemUser ? supportRepository.getOpen(SupportThread.SupportThreadStatus.OPEN) : 0;
        var unanswered = isSystemUser
                ? supportRepository.getUnanswered(id)
                : supportRepository.getMyUnanswered(id);
        return open + unanswered;
    }

}
