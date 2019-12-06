package com.municipality.katilimcivatandas.service;

import com.municipality.katilimcivatandas.domain.UserInfo;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link UserInfo}.
 */
public interface UserInfoService {

    /**
     * Save a userInfo.
     *
     * @param userInfo the entity to save.
     * @return the persisted entity.
     */
    UserInfo save(UserInfo userInfo);

    /**
     * Get all the userInfos.
     *
     * @return the list of entities.
     */
    List<UserInfo> findAll();


    /**
     * Get the "id" userInfo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserInfo> findOne(Long id);

    /**
     * Delete the "id" userInfo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
