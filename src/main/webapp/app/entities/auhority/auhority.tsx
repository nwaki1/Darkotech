import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './auhority.reducer';
import { IAuhority } from 'app/shared/model/auhority.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Auhority = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const auhorityList = useAppSelector(state => state.auhority.entities);
  const loading = useAppSelector(state => state.auhority.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="auhority-heading" data-cy="AuhorityHeading">
        <Translate contentKey="darkotechApp.auhority.home.title">Auhorities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="darkotechApp.auhority.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="darkotechApp.auhority.home.createLabel">Create new Auhority</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {auhorityList && auhorityList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="darkotechApp.auhority.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="darkotechApp.auhority.auhorityName">Auhority Name</Translate>
                </th>
                <th>
                  <Translate contentKey="darkotechApp.auhority.permission">Permission</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {auhorityList.map((auhority, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${auhority.id}`} color="link" size="sm">
                      {auhority.id}
                    </Button>
                  </td>
                  <td>{auhority.auhorityName}</td>
                  <td>
                    {auhority.permissions
                      ? auhority.permissions.map((val, j) => (
                          <span key={j}>
                            <Link to={`permission/${val.id}`}>{val.permissionName}</Link>
                            {j === auhority.permissions.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${auhority.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${auhority.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${auhority.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="darkotechApp.auhority.home.notFound">No Auhorities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Auhority;
