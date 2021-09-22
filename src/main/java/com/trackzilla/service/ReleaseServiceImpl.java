package com.trackzilla.service;

import com.trackzilla.entity.Release;
import com.trackzilla.repository.ReleaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReleaseServiceImpl implements ReleaseService {

    @Autowired
    private ReleaseRepository releaseRepository;

    @Override
    public List<Release> listReleases() {
        return releaseRepository.findAll();
    }

    @Override
    public Release save(Release rel) {
        return releaseRepository.save(rel);
    }

    @Override
    public void delete(long id) {
        releaseRepository.deleteById(id);
    }

}
