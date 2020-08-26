package com.iist.qlda.project.repository;

import com.iist.qlda.project.entity.PermissionEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class decentralizationRepositoryImlp implements decentralizationRepository   {
    @Override
    public List<PermissionEntity> findAll() {
        return null;
    }

    @Override
    public List<PermissionEntity> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<PermissionEntity> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<PermissionEntity> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(PermissionEntity permissionEntity) {

    }

    @Override
    public void deleteAll(Iterable<? extends PermissionEntity> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends PermissionEntity> S save(S s) {
        return null;
    }

    @Override
    public <S extends PermissionEntity> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<PermissionEntity> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends PermissionEntity> S saveAndFlush(S s) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<PermissionEntity> iterable) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public PermissionEntity getOne(Long aLong) {
        return null;
    }

    @Override
    public <S extends PermissionEntity> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends PermissionEntity> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends PermissionEntity> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends PermissionEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends PermissionEntity> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends PermissionEntity> boolean exists(Example<S> example) {
        return false;
    }
}
