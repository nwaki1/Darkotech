import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Auhority from './auhority';
import AuhorityDetail from './auhority-detail';
import AuhorityUpdate from './auhority-update';
import AuhorityDeleteDialog from './auhority-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AuhorityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AuhorityUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AuhorityDetail} />
      <ErrorBoundaryRoute path={match.url} component={Auhority} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AuhorityDeleteDialog} />
  </>
);

export default Routes;
