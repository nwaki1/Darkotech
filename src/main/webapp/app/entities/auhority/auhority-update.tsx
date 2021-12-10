import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IPermission } from 'app/shared/model/permission.model';
import { getEntities as getPermissions } from 'app/entities/permission/permission.reducer';
import { getEntity, updateEntity, createEntity, reset } from './auhority.reducer';
import { IAuhority } from 'app/shared/model/auhority.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AuhorityUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const permissions = useAppSelector(state => state.permission.entities);
  const auhorityEntity = useAppSelector(state => state.auhority.entity);
  const loading = useAppSelector(state => state.auhority.loading);
  const updating = useAppSelector(state => state.auhority.updating);
  const updateSuccess = useAppSelector(state => state.auhority.updateSuccess);
  const handleClose = () => {
    props.history.push('/auhority');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPermissions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...auhorityEntity,
      ...values,
      permissions: mapIdList(values.permissions),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...auhorityEntity,
          permissions: auhorityEntity?.permissions?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="darkotechApp.auhority.home.createOrEditLabel" data-cy="AuhorityCreateUpdateHeading">
            <Translate contentKey="darkotechApp.auhority.home.createOrEditLabel">Create or edit a Auhority</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="auhority-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('darkotechApp.auhority.auhorityName')}
                id="auhority-auhorityName"
                name="auhorityName"
                data-cy="auhorityName"
                type="text"
              />
              <ValidatedField
                label={translate('darkotechApp.auhority.permission')}
                id="auhority-permission"
                data-cy="permission"
                type="select"
                multiple
                name="permissions"
              >
                <option value="" key="0" />
                {permissions
                  ? permissions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.permissionName}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/auhority" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AuhorityUpdate;
