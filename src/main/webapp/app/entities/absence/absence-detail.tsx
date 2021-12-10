import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './absence.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AbsenceDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const absenceEntity = useAppSelector(state => state.absence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="absenceDetailsHeading">
          <Translate contentKey="darkotechApp.absence.detail.title">Absence</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.id}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="darkotechApp.absence.image">Image</Translate>
            </span>
          </dt>
          <dd>
            {absenceEntity.image ? (
              <div>
                {absenceEntity.imageContentType ? (
                  <a onClick={openFile(absenceEntity.imageContentType, absenceEntity.image)}>
                    <img src={`data:${absenceEntity.imageContentType};base64,${absenceEntity.image}`} style={{ maxHeight: '30px' }} />
                  </a>
                ) : null}
                <span>
                  {absenceEntity.imageContentType}, {byteSize(absenceEntity.image)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="time">
              <Translate contentKey="darkotechApp.absence.time">Time</Translate>
            </span>
          </dt>
          <dd>{absenceEntity.time ? <TextFormat value={absenceEntity.time} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="darkotechApp.absence.employee">Employee</Translate>
          </dt>
          <dd>{absenceEntity.employee ? absenceEntity.employee.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/absence" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/absence/${absenceEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AbsenceDetail;
