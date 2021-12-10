import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './absence.reducer';
import { IAbsence } from 'app/shared/model/absence.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const Absence = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const absenceList = useAppSelector(state => state.absence.entities);
  const loading = useAppSelector(state => state.absence.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="absence-heading" data-cy="AbsenceHeading">
        <Translate contentKey="darkotechApp.absence.home.title">Absences</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="darkotechApp.absence.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="darkotechApp.absence.home.createLabel">Create new Absence</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {absenceList && absenceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="darkotechApp.absence.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="darkotechApp.absence.image">Image</Translate>
                </th>
                <th>
                  <Translate contentKey="darkotechApp.absence.time">Time</Translate>
                </th>
                <th>
                  <Translate contentKey="darkotechApp.absence.employee">Employee</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {absenceList.map((absence, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${absence.id}`} color="link" size="sm">
                      {absence.id}
                    </Button>
                  </td>
                  <td>
                    {absence.image ? (
                      <div>
                        {absence.imageContentType ? (
                          <a onClick={openFile(absence.imageContentType, absence.image)}>
                            <img src={`data:${absence.imageContentType};base64,${absence.image}`} style={{ maxHeight: '30px' }} />
                            &nbsp;
                          </a>
                        ) : null}
                        <span>
                          {absence.imageContentType}, {byteSize(absence.image)}
                        </span>
                      </div>
                    ) : null}
                  </td>
                  <td>{absence.time ? <TextFormat type="date" value={absence.time} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{absence.employee ? <Link to={`employee/${absence.employee.id}`}>{absence.employee.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${absence.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${absence.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${absence.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="darkotechApp.absence.home.notFound">No Absences found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Absence;
