package com.scnsoft.art.facade;

import com.scnsoft.art.contoller.SupportThreadFilter;
import com.scnsoft.art.dto.AccountType;
import com.scnsoft.art.dto.SupportDto;
import com.scnsoft.art.dto.SupportFilter;
import com.scnsoft.art.dto.SupportThreadDto;
import com.scnsoft.art.dto.mapper.SupportMapper;
import com.scnsoft.art.entity.Account;
import com.scnsoft.art.entity.FileInfo;
import com.scnsoft.art.entity.Support;
import com.scnsoft.art.entity.SupportThread;
import com.scnsoft.art.entity.SupportType;
import com.scnsoft.art.security.SecurityUtil;
import com.scnsoft.art.service.FileServiceImplFile;
import com.scnsoft.art.service.SupportService;
import com.scnsoft.art.service.user.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupportFacade {

    private final SupportService supportService;
    private final AccountService accountService;
    private final SupportMapper supportMapper;
    private final FileServiceImplFile fileServiceExternal;

    public List<SupportDto> findAll(SupportFilter filter, Sort sort) {
        Account account = accountService.findById(SecurityUtil.getCurrentAccountId());
        boolean isSystem = AccountType.SYSTEM.equals(account.getAccountType());
        if(filter.getThreadId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thread id is required filter");
        }
        if(isSystem) {
            supportService.findThread(filter.getThreadId())
                    .ifPresentOrElse(
                            (SupportThread thread) -> {
                                if (!thread.getAccountId().equals(account.getId())) {
                                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to that thread");
                                }
                            },
                            () -> {
                                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thread not found");
                            });
        }
        return supportService.findAllPosts(filter, sort).stream().map(supportMapper::mapToDto).collect(Collectors.toList());
    }

    public Page<SupportThreadDto> findThreads(SupportThreadFilter filter, Pageable pageable) {
        Account account = accountService.findById(SecurityUtil.getCurrentAccountId());
        boolean isSystem = AccountType.SYSTEM.equals(account.getAccountType());
        if(!isSystem) {
            filter.setAccountId(account.getId());
        }
        var out = supportService.findThreads(filter, pageable).map(supportMapper::mapToThreadDto);


        var opPosts = supportService.findOpPosts(
                out.stream().map(SupportThreadDto::getId).collect(Collectors.toList()));

        out.getContent().forEach(f -> {
            var opPost = opPosts.get(f.getId());
            if(opPost != null) {
                f.setPosts(Collections.singletonList(supportMapper.mapToDto(opPost)));
            } else {
                f.setPosts(new ArrayList<>());
            }
        });

        return out;
    }

    public SupportThreadDto createThread(SupportThreadDto dto) {
        Account account = accountService.findById(SecurityUtil.getCurrentAccountId());
        SupportThread thread = supportMapper.mapToThreadEntity(dto);
        thread.setId(null);
        thread.setAccountId(account.getId());
        thread.setCreatedAt(new Date());
        thread.setUpdatedAt(thread.getCreatedAt());
        SupportThread newThread = supportService.saveThread(thread);

        if(dto.getMessage() != null) {
            Support support = new Support();
            support.setAccountId(account.getId());
            support.setThreadId(newThread.getId());
            support.setType(SupportType.TEXT);
            support.setMessage(dto.getMessage());
            support.setCreatedAt(thread.getCreatedAt());
            supportService.save(support);
        }
        if(dto.getFiles() != null) {
            dto.getFiles().forEach(fileDto -> {
                FileInfo fileInfo = fileServiceExternal.findFileInfoById(fileDto.getId()).orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Media file not found");
                });

                Support support = new Support();
                support.setAccountId(account.getId());
                support.setThreadId(newThread.getId());
                support.setType(SupportType.MEDIA);
                support.setMessage(fileInfo.getId() + ":" + fileInfo.getMimeType() + ":" + fileInfo.getOriginalName());
                support.setCreatedAt(thread.getCreatedAt());
                supportService.save(support);

                fileInfo.setDirectory("/support/" + newThread.getId() + "/");
                fileServiceExternal.upstream(fileInfo, null);
            });

        }

        return supportMapper.mapToThreadDto(newThread);
    }

    public SupportDto createPost(SupportDto supportDto) {
        Account account = accountService.findById(SecurityUtil.getCurrentAccountId());
        Support post = supportMapper.mapToEntity(supportDto);
        post.setId(null);
        post.setAccountId(account.getId());
        post.setCreatedAt(new Date());
        if(post.getThreadId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cant create post, thread ID is required field");
        }

        supportService.findThread(post.getThreadId()).ifPresentOrElse(
                (thread) -> {
                    boolean isSystem = AccountType.SYSTEM.equals(account.getAccountType());
                    if(!isSystem) {
                        if (!account.getId().equals(thread.getAccountId())) {
                            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no access to that thread");
                        }
                    }
                    thread.setUpdatedAt(new Date());
                    if(!isSystem && thread.getStatus() == SupportThread.SupportThreadStatus.done) {
                        thread.setStatus(SupportThread.SupportThreadStatus.open);
                    }
                    supportService.saveThread(thread);
                },
                () -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "You can't create post to thread which does not exists");
                });

        return supportMapper.mapToDto(supportService.createPost(post));
    }

    public void delete(UUID id) {
        Account account = accountService.findById(SecurityUtil.getCurrentAccountId());
        Optional<SupportThread> threadOptional = supportService.findThread(id);
        if(threadOptional.isEmpty()) {
            return;
        }
        SupportThread thread = threadOptional.get();
        if(!thread.getAccountId().equals(account.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can delete only your own threads");
        }
        SupportFilter filter = new SupportFilter();
        filter.setThreadId(thread.getId());
        for (Support post : supportService.findAllPosts(filter, Sort.unsorted())) {
            if(SupportType.MEDIA.equals(post.getType())) {
                fileServiceExternal.deleteById(UUID.fromString(post.getMessage().split(":")[0]));
            }
            supportService.deletePost(post);
        }
        supportService.deleteThread(thread);
    }

    public Optional<SupportThreadDto> findThread(UUID id) {
        return supportService.findThread(id)
                .map(thread -> {
                    SupportThreadDto out = supportMapper.mapToThreadDto(thread);
                    SupportFilter filter = new SupportFilter();
                    filter.setThreadId(thread.getId());
                    out.setPosts(supportService.findAllPosts(filter, Sort.by(Sort.Order.asc(SupportThread.Fields.createdAt)))
                            .stream().map(supportMapper::mapToDto)
                            .toList());

                    return out;
                });
    }

    public SupportThreadDto updateThread(SupportThreadDto dto) {
        UUID accountId = SecurityUtil.getCurrentAccountId();
        Account account = accountService.findById(accountId);

        SupportThread thread = supportService.findThread(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Thread not found"));
        if(thread.getStatus() == dto.getStatus()) {
            return supportMapper.mapToThreadDto(thread);
        }
        thread.setStatus(dto.getStatus());

        if(!(account.getId().equals(thread.getAccountId()) || AccountType.SYSTEM.equals(account.getAccountType()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no access to that thread");
        }

        var now = new Date();

        Support support = new Support();
        support.setAccountId(accountId);
        support.setThreadId(thread.getId());
        support.setType(SupportType.SYSTEM);
        support.setMessage("STATUS_CHANGE:" + thread.getStatus().name());
        support.setCreatedAt(now);

        supportService.save(support);

        thread.setUpdatedAt(now);

        return supportMapper.mapToThreadDto(supportService.saveThread(thread));

    }
}
