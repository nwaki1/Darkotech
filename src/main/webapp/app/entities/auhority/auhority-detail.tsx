import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './auhority.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AuhorityDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const auhorityEntity = useAppSelector(state => state.auhority.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="auhorityDetailsHeading">
          <Translate contentKey="darkotechApp.auhority.detail.title">Auhority</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{auhorityEntity.id}</dd>
          <dt>
            <span id="auhorityName">
              <Translate contentKey="darkotechApp.auhority.auhorityName">Auhority Name</Translate>
            </span>
          </dt>
          <dd>{auhorityEntity.auhorityName}</dd>
          <dt>
            <Translate contentKey="darkotechApp.auhority.permission">Permission</Translate>
          </dt>
          <dd>
            {auhorityEntity.permissions
              ? auhorityEntity.permissions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.permissionName}</a>
                    {auhorityEntity.permissions && i === auhorityEntity.permissions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/auhority" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/auhority/${auhorityEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuhorityDetail;
