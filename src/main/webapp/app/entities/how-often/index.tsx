import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HowOften from './how-often';
import HowOftenDetail from './how-often-detail';
import HowOftenUpdate from './how-often-update';
import HowOftenDeleteDialog from './how-often-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HowOftenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HowOftenUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HowOftenDetail} />
      <ErrorBoundaryRoute path={match.url} component={HowOften} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={HowOftenDeleteDialog} />
  </>
);

export default Routes;
