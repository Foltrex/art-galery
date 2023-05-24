package com.scnsoft.art.service;

import com.scnsoft.art.contoller.SupportThreadFilter;
import com.scnsoft.art.dto.SupportFilter;
import com.scnsoft.art.entity.Support;
import com.scnsoft.art.entity.SupportThread;
import com.scnsoft.art.repository.SupportRepository;
import com.scnsoft.art.repository.SupportThreadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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

    public Map<UUID, Support> findOpPosts(List<UUID> uuidStream) {
        if(uuidStream == null || uuidStream.isEmpty()) {
            return new HashMap<>();
        }
        return supportRepository.findOpPosts(uuidStream).stream().collect(Collectors.toMap(
                Support::getThreadId,
                s -> s,
                (s1, s2) -> s1
        ));
    }
}
