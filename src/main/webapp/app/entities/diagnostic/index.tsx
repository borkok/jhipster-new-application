import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Diagnostic from './diagnostic';
import DiagnosticDetail from './diagnostic-detail';
import DiagnosticUpdate from './diagnostic-update';
import DiagnosticDeleteDialog from './diagnostic-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DiagnosticUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DiagnosticUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DiagnosticDetail} />
      <ErrorBoundaryRoute path={match.url} component={Diagnostic} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DiagnosticDeleteDialog} />
  </>
);

export default Routes;
